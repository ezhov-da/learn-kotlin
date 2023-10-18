package ru.ezhov.example.gist

import java.io.File

fun main() {
    val file = File("C:\\Users\\DEzhov\\gists-test\\gist для обработки")
    val systemLineSeparator = System.getProperty("line.separator")
    file.walk().iterator().forEach { f ->
        if (f.isFile) {
            val text = f.readText() + systemLineSeparator + systemLineSeparator + "[[gist]]"
            f.writeText(text)
        }
    }
}

private fun replaceCode() {
    val file = File("C:\\Users\\DEzhov\\gists-test\\gist для обработки")

    val codeStartRegEx = "^(\\[code:\\])(\\w+)(\\[:code\\])\$".toRegex()
    val codeEndRegEx = "^\\[\\/code\\]\$".toRegex()

    val systemLineSeparator = System.getProperty("line.separator")

    file.walk().iterator().forEach { file ->
        if (file.isFile) {
            val lines = file.readLines().map { line ->
                if (codeStartRegEx.matches(line)) {
                    "```" + codeStartRegEx.matchEntire(line)!!.groupValues[2]
                } else if (codeEndRegEx.matches(line)) {
                    "```"
                } else {
                    line
                }
            }

            val resultText = lines.joinToString(separator = systemLineSeparator)

            file.writeText(resultText)
        }
    }
}
