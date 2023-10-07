package ru.ezhov.example.markdown

import java.io.File

fun main() {
    val rootFolder = File("G:\\Другие компьютеры\\Ноутбук\\sync\\docs\\summary")
    val identifyingFilesService = IdentifyingFilesService()
    val generateBookMarkdownService = GenerateBookMarkdownService()

    val fileForGenerate = identifyingFilesService.getFilesForGenerating(rootFolder)
    val generatedResult = generateBookMarkdownService.generateForRaw(fileForGenerate)
    generatedResult
        .forEach { (k, v) ->
            println(v)
            File(k.parent, "README.md").writeText(v)
        }

    val generateBookRootMarkdownService = GenerateBookRootMarkdownService()

    generateBookRootMarkdownService.generateRootMarkdown(rootFolder)
}
