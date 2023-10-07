package ru.ezhov.example.markdown

import java.io.File
import java.math.BigInteger
import java.security.MessageDigest


class GenerateBookMarkdownService {
    private val hashCreator: HashCreator = HashCreator()

    fun generateForRaw(text: String): String = generateMarkdown(text)

    fun generateForRaw(rootFoldersForFiles: List<File>): Map<File, String> =
        rootFoldersForFiles.associate { file ->
            val fileWithMd = File(file, "index_raw.md")
            val textFile = fromFile(fileWithMd)
            val content = generateMarkdown(textFile)
            fileWithMd to content
        }


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
                "$row ${generateAnchor(row)}"
            } else {
                row
            }
        }

    private fun generateAnchor(text: String): String {
        val hash = hashCreator.createMD5Hash(text)
        return "<a name=\"$hash\"></a>"
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

    // https://reflectoring.io/creating-hashes-in-java/
    class HashCreator {
        fun createMD5Hash(input: String): String {
            val md = MessageDigest.getInstance("MD5")
            // Compute message digest of the input
            val messageDigest = md.digest(input.toByteArray())
            return convertToHex(messageDigest)
        }

        private fun convertToHex(messageDigest: ByteArray): String {
            val bigint = BigInteger(1, messageDigest)
            var hexText: String = bigint.toString(16)
            while (hexText.length < 32) {
                hexText = "0$hexText"
            }
            return hexText
        }
    }
}
