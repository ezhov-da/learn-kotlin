package ru.ezhov.example.collections

val col = listOf(
        1, 3, 5, 6, 7, 8, 4, 5753, 23, 123, 2, 354734, 5734, 4, 446, 235, 125, 134, 3, 46, 24658, 3568
)

fun main() {
    println(col.partition { it.mod(2) == 0 })
    println(col.sum())
    println(col.average())
    println(col.sumOf { it * 2 })
}