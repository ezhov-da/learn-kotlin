package ru.ezhov.example.autocomplete

import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.JTextField
import javax.swing.KeyStroke
import javax.swing.SwingUtilities
import javax.swing.UIManager

// https://stackabuse.com/example-adding-autocomplete-to-jtextfield/
fun main() {
    SwingUtilities.invokeLater {
        val COMMIT_ACTION = "commit";
        val mainTextField = JTextField(20)

        // Without this, cursor always leaves text field
        mainTextField.focusTraversalKeysEnabled = false
        // Our words to complete
        val keywords = listOf(
            "example",
            "autocomplete",
            "autotest",
            "stackabuse",
            "java",
        )
        val autoComplete = Autocomplete(mainTextField, keywords)
        mainTextField.getDocument().addDocumentListener(autoComplete)

        // Maps the tab key to the commit action, which finishes the autocomplete
        // when given a suggestion
        mainTextField.getInputMap().put(KeyStroke.getKeyStroke("TAB"), COMMIT_ACTION);
        mainTextField.getActionMap().put(COMMIT_ACTION, autoComplete.CommitAction());


        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
        } catch (ex: Throwable) {
            //
        }
        val frame = JFrame("_________")
        frame.add(JPanel().apply { add(mainTextField) })
        frame.setSize(1000, 600)
        frame.setLocationRelativeTo(null)
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.isVisible = true
    }
}
