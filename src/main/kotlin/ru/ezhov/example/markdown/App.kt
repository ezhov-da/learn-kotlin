package ru.ezhov.example.markdown

import java.io.File

fun main() {
    var textFile = """
        - Hello
            - Hello 2
        # Test
        ## 123123
        ### asasasdasdasd
        # Test
        ## asasdadsasd
        # Test
        # Test
        # Test
    """.trimIndent()

    textFile = File("").readText()

    val paragraphs = textFile.split("\n").filter { it.startsWith("#") }
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

    val resultText = result.joinToString(separator = "\n")

    println(resultText)
}
