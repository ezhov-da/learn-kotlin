package ru.ezhov.example.filetypedetect

import org.apache.tika.Tika
import java.io.File

fun main() {
    // Путь к файлу, тип которого нужно определить
    val fileJpg = File("./2150458458.jpg")
    val filePng = File("./123.png")
    val filePseudoZip = File.createTempFile("test", "zip")
    filePseudoZip.writeText("<script>alert(document.domain)</script>")
    val tika = Tika()

    listOf(fileJpg, filePng, filePseudoZip).forEach { file ->

        // Определение типа файла
        try {
            val fileType = tika.detect(file)
            println("${file.name}. Тип файла: " + fileType);
        } catch (e: Exception) {
            e.printStackTrace();
        }
    }
}
