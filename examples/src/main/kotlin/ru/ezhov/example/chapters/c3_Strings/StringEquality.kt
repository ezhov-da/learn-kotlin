package ru.ezhov.example.chapters.c3_Strings

fun main() {
    val str1 = "Hello, World!"
    val str2 = "Hello," + " World!"
    println(str1 == str2) // Prints true

    // Referential equality is checked with === operator
    val str_1 = """
    |Hello, World!
    """.trimMargin()

    val str_2 = """
    #Hello, World!
    """.trimMargin("#")

    val str_3 = str_1
    println(str_1 == str_2) // Prints true
    println(str_1 === str_2) // Prints false WTF!!!!!! That true!!!!
    println(str_1 === str_3) // Prints true
}