package ru.ezhov.example.diff

fun main() {
    println(num(T.A))
}

fun num(test: T): String =
        when (test) {
            T.A -> null
            T.B -> "2"
        } ?: throw RuntimeException("OOps")

enum class T { A, B }