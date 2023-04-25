package ru.ezhov.example.autocomplete

import java.awt.event.ActionEvent
import java.util.*
import javax.swing.AbstractAction
import javax.swing.JTextField
import javax.swing.SwingUtilities
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener
import javax.swing.text.BadLocationException


class Autocomplete(private val textField: JTextField, private val keywords: List<String>) :
    DocumentListener {
    private enum class Mode {
        INSERT, COMPLETION
    }

    private var mode = Mode.INSERT

    init {
        Collections.sort(keywords)
    }

    override fun changedUpdate(ev: DocumentEvent) {}
    override fun removeUpdate(ev: DocumentEvent) {}
    override fun insertUpdate(ev: DocumentEvent) {
        if (ev.length != 1) return
        val pos = ev.offset
        var content: String? = null
        try {
            content = textField.getText(0, pos + 1)
        } catch (e: BadLocationException) {
            e.printStackTrace()
        }

        // Find where the word starts
        var w: Int
        w = pos
        while (w >= 0) {
            if (!Character.isLetter(content!![w])) {
                break
            }
            w--
        }

        // Too few chars
        if (pos - w < 2) return
        val prefix = content!!.substring(w + 1).lowercase(Locale.getDefault())
        val n = Collections.binarySearch(keywords, prefix)
        if (n < 0 && -n <= keywords.size) {
            val match = keywords[-n - 1]
            if (match.startsWith(prefix)) {
                // A completion is found
                val completion = match.substring(pos - w)
                // We cannot modify Document from within notification,
                // so we submit a task that does the change later
                SwingUtilities.invokeLater(CompletionTask(completion, pos + 1))
            }
        } else {
            // Nothing found
            mode = Mode.INSERT
        }
    }

    inner class CommitAction : AbstractAction() {
        override fun actionPerformed(ev: ActionEvent) {
            if (mode == Mode.COMPLETION) {
                val pos = textField.selectionEnd
                val sb = StringBuffer(textField.text)
                sb.insert(pos, " ")
                textField.text = sb.toString()
                textField.caretPosition = pos + 1
                mode = Mode.INSERT
            } else {
                textField.replaceSelection("\t")
            }
        }
    }

    private inner class CompletionTask constructor(
        private val completion: String,
        private val position: Int
    ) : Runnable {
        override fun run() {
            val sb = StringBuffer(textField.text)
            sb.insert(position, completion)
            textField.text = sb.toString()
            textField.caretPosition = position + completion.length
            textField.moveCaretPosition(position)
            mode = Mode.COMPLETION
        }
    }
}
