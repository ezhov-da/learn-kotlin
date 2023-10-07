package ru.ezhov.example.markdown

import java.io.File
import java.io.FileFilter
import java.net.URLEncoder

class GenerateBookRootMarkdownService {

    fun generateRootMarkdown(rootFolder: File) {
        val folders = rootFolder.listFiles(
            FileFilter {
                it.isDirectory && "^\\d+\\.".toRegex().find(it.name) != null
            }
        )

        val folderInfos = folders!!.map {
            val values = it.name.split(".")
            FolderInfo(
                it.name,
                values[0],
                values[1],
                values[2],
            )
        }

        val stringBuilder = StringBuilder()
        stringBuilder.append("# Summary").append("\n")
        stringBuilder.append("\n")

        folderInfos.forEach { info ->
            val text = info.name
            val link = "/${URLEncoder.encode(info.fullName, Charsets.UTF_8).replace("+", "%20")}/README.md"
            stringBuilder.append("[$text]($link) [${info.tag}]").append("\n")
            stringBuilder.append("\n")
        }
        val content = stringBuilder.toString()

        println(content)

        File(rootFolder, "README.md").writeText(content)
    }

    private data class FolderInfo(
        val fullName: String,
        val number: String,
        val name: String,
        val tag: String,
    )

}
