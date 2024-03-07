package ru.ezhov.example.filetypedetect

import org.apache.tika.Tika
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import javax.swing.JLabel
import javax.swing.JPanel

fun main() {
    val image = BufferedImage(100, 100, BufferedImage.TYPE_INT_BGR)
    JPanel().apply {
        add(JLabel("Test"))
    }.graphics.drawImage(image, 100, 100, null)

    val filePng = File.createTempFile("test", "png")
    filePng.outputStream().use {
        ImageIO.write(image, "png", it)
    }

    // Путь к файлу, тип которого нужно определить
    val filePseudoZip = File.createTempFile("test", "zip")
    filePseudoZip.writeText("<script>alert(document.domain)</script>")
    val tika = Tika()

    listOf(filePng, filePseudoZip).forEach { file ->

        // Определение типа файла
        try {
            val fileType = tika.detect(file)
            println("${file.name}. Тип файла: " + fileType);
        } catch (e: Exception) {
            e.printStackTrace();
        }
    }
}
