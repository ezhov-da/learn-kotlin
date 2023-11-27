package ru.ezhov.example.swingmarkdown

import org.commonmark.ext.gfm.tables.TablesExtension
import org.commonmark.node.Image
import org.commonmark.node.Node
import org.commonmark.parser.Parser
import org.commonmark.renderer.html.AttributeProvider
import org.commonmark.renderer.html.HtmlRenderer
import java.awt.Desktop
import java.io.IOException
import javax.swing.JEditorPane
import javax.swing.JFrame
import javax.swing.JScrollPane
import javax.swing.SwingUtilities
import javax.swing.UIManager
import javax.swing.event.HyperlinkEvent
import javax.swing.event.HyperlinkListener
import javax.swing.text.html.HTMLDocument
import javax.swing.text.html.HTMLFrameHyperlinkEvent


//https://simplesolution.dev/java-parse-markdown-to-html-using-commonmark/
fun main() {
    SwingUtilities.invokeLater {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
        } catch (ex: Throwable) {
            //
        }
        val frame = JFrame("_________")


        val pane = JEditorPane().apply {
            contentType = "text/html"
            text = resourceMarkdownToHtml("/index.md")
            isEditable = false
            addHyperlinkListener(createHyperLinkListener(this))
        }

        frame.add(JScrollPane(pane))

        frame.setSize(1000, 600)
        frame.setLocationRelativeTo(null)
        frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        frame.isVisible = true
    }
}

private fun resourceMarkdownToHtml(resource: String): String =
    markdownToHtml(
        ImageAttributeProvider::class.java.getResourceAsStream(resource)!!
            .use { it.bufferedReader().readText() }
    )

private fun markdownToHtml(markdown: String): String {
    // https://github.com/commonmark/commonmark-java
    val parser: Parser = Parser.builder().extensions(listOf(TablesExtension.create())).build()
    val document: Node = parser.parse(markdown)

    val renderer: HtmlRenderer = HtmlRenderer.builder()
        .attributeProviderFactory { ImageAttributeProvider() }
        .build()
    return renderer.render(document)
}

internal class ImageAttributeProvider : AttributeProvider {
    override fun setAttributes(node: Node, tagName: String, attributes: MutableMap<String, String>) {
        if (node is Image) {
            val src = attributes["src"]!!
            if (!src.startsWith("http")) {
                attributes["src"] = ImageAttributeProvider::class.java.getResource(src)!!.toExternalForm()
            }
        }
    }
}

private fun createHyperLinkListener(editorPane: JEditorPane): HyperlinkListener? {
    return HyperlinkListener { e ->
        if (e.eventType === HyperlinkEvent.EventType.ACTIVATED) {
            if (e is HTMLFrameHyperlinkEvent) {
                (editorPane.document as HTMLDocument).processHTMLFrameHyperlinkEvent(e)
            } else {
                try {
                    if (e.url?.toURI()?.isAbsolute == true) {
                        if (Desktop.isDesktopSupported()) {
                            Desktop.getDesktop().browse(e.url.toURI())
                        }
                    } else {
                        val description = e.description
                        if (description.startsWith("/") && description.endsWith(".md")) {
                            editorPane.text = resourceMarkdownToHtml(description)
                        } else {
                            editorPane.page = e.url
                        }
                    }
                } catch (ioe: IOException) {
                    println("IOE: $ioe")
                }
            }
        }
    }
}
