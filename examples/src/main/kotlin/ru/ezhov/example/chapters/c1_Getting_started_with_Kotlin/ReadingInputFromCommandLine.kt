package ru.ezhov.example.chapters.c1_Getting_started_with_Kotlin

fun main() {
    println("Enter two number")
    val (a, b) = readLine()!!.split(' ') // !! this operator use for NPE(NullPointerException)
    println("Max number is : ${maxNum(a.toInt(), b.toInt())}")
}

fun maxNum(a: Int, b: Int): Int {
    val max = if (a > b) {
        println("The value of a is $a")
        a
    } else {
        println("The value of b is $b")
        b
    }
    return max
}