package ru.ezhov.example.markdown

import java.io.File
import java.util.*

fun main() {
    listOf(
        File("G:\\Другие компьютеры\\Ноутбук\\sync\\docs\\summary\\001.Фундаментальный подход к программной архитектуре.it\\index_raw.md"),
        File("G:\\Другие компьютеры\\Ноутбук\\sync\\docs\\summary\\002.Завтрак с Сенекой.философия\\index_raw.md"),
    )
        .forEach { file ->
            val textFile = fromFile(file)

            val content = generateMarkdown(textFile)
            println(content)
            File(file.parent, "README.md").writeText(content)
        }
}

private fun testString() = """

        {{menu}}

        # Test
        ## 123123
        ### asasasdasdasd
        # Test
        ## asasdadsasd
        # Test
        # Test
        # Test
    """.trimIndent()

private fun generateMarkdown(text: String): String {
    val rawRows = textToRows(text)
    val rowsWithAnchors = addAnchorsToParagraphs(rawRows)
    val menu = createMenu(rowsWithAnchors)
    val replacedContentRows = replaceContent(rowsWithAnchors, ReplaceInfo(menu = menu))
    return buildContentRowsToContent(replacedContentRows)
}

private fun fromFile(file: File) = file.readText()

private fun textToRows(content: String): List<String> =
    content.split("\n").map { it.replace("\r", "") }

private fun addAnchorsToParagraphs(contentRows: List<String>): List<String> =
    contentRows.map { row ->
        if (row.startsWith("#")) {
            "$row ${generateAnchor()}"
        } else {
            row
        }
    }

private fun generateAnchor(): String {
    val uuid = UUID.randomUUID().toString()
    val shortUuid = uuid.substring(0, uuid.indexOf("-"))
    return "<a name=\"$shortUuid\"></a>"
}

private fun createMenu(contentRowsWithAnchors: List<String>): String {
    val paragraphs = contentRowsWithAnchors.filter { it.startsWith("#") }
    /*
    # Test <a name="again"></a>
    # Test Hello bkb rfr nfv <a name="again"></a>
    ## Test Hello bkb rfr nfv <a name="again"></a>
    ### Test Hello bkb rfr nfv <a name="again"></a>
     */
    val regexForAnchor = "(#+) (.+) <a name=\"(.+)\">".toRegex()
    val result = paragraphs.map { originText ->
        val index = originText.indexOf(" ")
        val symbols = originText.substring(0, index)
        val text = originText.substring(index + 1)

        val menuText = regexForAnchor
            .find(originText)
            ?.let { result ->
                val textFromGroup = result.groupValues[2]
                val anchor = result.groupValues[3]

                //[Полезные ссылки](#again)
                "[${textFromGroup}](#${anchor})"
            }
            ?: text

        "\t".repeat(symbols.length - 1) + "- " + menuText
    }

    return result.joinToString(separator = "\n")
}

private fun replaceContent(contentRows: List<String>, replaceInfo: ReplaceInfo) =
    contentRows.map { row ->
        row.replace("{{menu}}", replaceInfo.menu)
    }

private fun buildContentRowsToContent(contentRows: List<String>): String =
    contentRows.joinToString(separator = "\n")

data class ReplaceInfo(
    val menu: String
)
