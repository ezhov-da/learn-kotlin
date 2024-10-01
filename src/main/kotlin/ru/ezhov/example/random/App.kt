package ru.ezhov.example.random

import org.apache.commons.lang3.RandomStringUtils



fun main() {
    val generatedString = RandomStringUtils.secure().nextAlphabetic(10)

    println(generatedString)
}
