package ru.ezhov.example.markdown

import java.io.File
import java.io.FileFilter

class IdentifyingFilesService {
    fun getFilesForGenerating(rootFolder: File): List<File> =
        rootFolder
            .listFiles(
                FileFilter {
                    it.isDirectory && "^\\d+\\.".toRegex().find(it.name) != null
                }
            )
            ?.toList()
            .orEmpty()
}
