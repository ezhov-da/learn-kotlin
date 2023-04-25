package ru.ezhov.example.autocompletepopup

import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.Window
import java.awt.event.FocusEvent
import java.awt.event.FocusListener
import java.awt.event.KeyEvent
import java.awt.event.KeyListener
import java.util.*
import java.util.function.Function
import java.util.stream.Collectors
import javax.swing.AbstractListModel
import javax.swing.JFrame
import javax.swing.JList
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JTextField
import javax.swing.JWindow
import javax.swing.SwingUtilities
import javax.swing.WindowConstants
import javax.swing.border.EmptyBorder
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener


/**
 * @author Mikle Garin
 * @see https://stackoverflow.com/questions/45439231/implementing-autocomplete-with-jtextfield-and-jpopupmenu
 * https://stackoverflow.com/questions/45439231/implementing-autocomplete-with-jtextfield-and-jpopupmenu
 */
class AutocompleteField(
    /**
     * [Function] for text lookup.
     * It simply returns [List] of [String] for the text we are looking results for.
     */
    private val lookup: Function<String, List<String>>
) :
    JTextField(), FocusListener, DocumentListener, KeyListener {
    /**
     * [List] of lookup results.
     * It is cached to optimize performance for more complex lookups.
     */
    private val results: MutableList<String>

    /**
     * [JWindow] used to display offered options.
     */
    private val popup: JWindow

    /**
     * Lookup results [JList].
     */
    private val list: JList<*>

    /**
     * [.list] model.
     */
    private val model: ListModel

    /**
     * Constructs [AutocompleteField].
     *
     * @param lookup [Function] for text lookup
     */
    init {
        results = ArrayList()
        val parent = SwingUtilities.getWindowAncestor(this)
        popup = JWindow(parent)
        popup.type = Window.Type.POPUP
        popup.focusableWindowState = false
        popup.isAlwaysOnTop = true
        model = ListModel()
        list = JList(model)
        popup.add(object : JScrollPane(list) {
            override fun getPreferredSize(): Dimension {
                val ps = super.getPreferredSize()
                ps.width = this@AutocompleteField.width
                return ps
            }
        })
        addFocusListener(this)
        document.addDocumentListener(this)
        addKeyListener(this)
    }

    /**
     * Displays autocomplete popup at the correct location.
     */
    private fun showAutocompletePopup() {
        val los = this@AutocompleteField.locationOnScreen
        popup.setLocation(los.x, los.y + height)
        popup.isVisible = true
    }

    /**
     * Closes autocomplete popup.
     */
    private fun hideAutocompletePopup() {
        popup.isVisible = false
    }

    override fun focusGained(e: FocusEvent) {
        SwingUtilities.invokeLater {
            if (results.size > 0) {
                showAutocompletePopup()
            }
        }
    }

    private fun documentChanged() {
        SwingUtilities.invokeLater {

            // Updating results list
            results.clear()


            val array = text.toCharArray()
            var currentCaretPosition = caretPosition - 1
            val al = mutableListOf<Char>()
            while (currentCaretPosition > -1 && array[currentCaretPosition] != Char(32)) {
                al.add(array[currentCaretPosition])
                currentCaretPosition -= 1
            }

            al.reverse()
            if (al.isNotEmpty()) {
                results.addAll(lookup.apply(String(al.toCharArray())))
            }

            // Updating list view
            model.updateView()
            list.visibleRowCount = Math.min(results.size, 10)

            // Selecting first result
            if (results.size > 0) {
                list.selectedIndex = 0
            }

            // Ensure autocomplete popup has correct size
            popup.pack()

            // Display or hide popup depending on the results
            if (results.size > 0) {
                showAutocompletePopup()
            } else {
                hideAutocompletePopup()
            }
        }
    }

    override fun focusLost(e: FocusEvent) {
        SwingUtilities.invokeLater { hideAutocompletePopup() }
    }

    override fun keyPressed(e: KeyEvent) {
        if (e.keyCode == KeyEvent.VK_UP) {
            val index = list.selectedIndex
            if (index != -1 && index > 0) {
                list.selectedIndex = index - 1
            }
        } else if (e.keyCode == KeyEvent.VK_DOWN) {
            val index = list.selectedIndex
            if (index != -1 && list.model.size > index + 1) {
                list.selectedIndex = index + 1
            }
        } else if (e.keyCode == KeyEvent.VK_ENTER) {
            val text = list.selectedValue as? String
            if (text != null) {
                setText(text)
                caretPosition = text.length
            }
        } else if (e.keyCode == KeyEvent.VK_ESCAPE) {
            hideAutocompletePopup()
        }
    }

    override fun insertUpdate(e: DocumentEvent) {
        documentChanged()
    }

    override fun removeUpdate(e: DocumentEvent) {
        documentChanged()
    }

    override fun changedUpdate(e: DocumentEvent) {
        documentChanged()
    }

    override fun keyTyped(e: KeyEvent) {
        // Do nothing
    }

    override fun keyReleased(e: KeyEvent) {
        // Do nothing
    }

    /**
     * Custom list model providing data and bridging view update call.
     */
    private inner class ListModel : AbstractListModel<Any?>() {
        override fun getSize(): Int {
            return results.size
        }

        override fun getElementAt(index: Int): Any {
            return results[index]
        }

        /**
         * Properly updates list view.
         */
        fun updateView() {
            super.fireContentsChanged(this@AutocompleteField, 0, size)
        }
    }

    companion object {
        /**
         * Sample [AutocompleteField] usage.
         *
         * @param args run arguments
         */
        @JvmStatic
        fun main(args: Array<String>) {
            val frame = JFrame("Sample autocomplete field")

            // Sample data list
            val values = Arrays.asList("Frame", "Dialog", "Label", "Tree", "Table", "List", "Field")

            // Simple lookup based on our data list
            val lookup =
                Function { text: String ->
                    values.stream()
                        .filter { v: String ->
                            !text.isEmpty() && v.lowercase(
                                Locale.getDefault()
                            ).contains(text.lowercase(Locale.getDefault())) && v != text
                        }
                        .collect(Collectors.toList())
                }

            // Autocomplete field itself
            val field = AutocompleteField(lookup)
            field.columns = 15
            val border = JPanel(BorderLayout())
            border.border = EmptyBorder(50, 50, 50, 50)
            border.add(field)
            frame.add(border)
            frame.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
            frame.pack()
            frame.setLocationRelativeTo(null)
            frame.isVisible = true
        }
    }
}
