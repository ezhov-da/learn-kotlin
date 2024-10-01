package ru.ezhov.example.emojii

import java.awt.BorderLayout
import javax.swing.JEditorPane
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.SwingUtilities
import javax.swing.WindowConstants.EXIT_ON_CLOSE

fun main() {
    SwingUtilities.invokeLater {
        JFrame().apply {
            add(
                JPanel().apply {
                    add(JLabel("<html><span>\uD83D\uDE00</span>"))
                    add(JLabel("<html>\uD83D\uDC81\uD83C\uDFFF"))
                    add(JLabel("<html>\uD83E\uDDD1\u200D✈\uFE0F"))
                },
                BorderLayout.NORTH
            )
            add(
                JEditorPane().apply {
                    contentType = "text/html"
                    text = "\uD83E\uDD10 <br> " +
                        "\uD83E\uDD20 <br> " +
                        "\uD83E\uDD26\uD83C\uDFFC\u200D♂\uFE0F " +
                        "<br> \uD83E\uDDD1\uD83C\uDFFE\u200D⚕\uFE0F <br> " +
                        "\uD83D\uDD3A"
                },
                BorderLayout.CENTER
            )

            pack()
            setLocationRelativeTo(null)
            defaultCloseOperation = EXIT_ON_CLOSE
            isVisible = true
        }
    }
}
