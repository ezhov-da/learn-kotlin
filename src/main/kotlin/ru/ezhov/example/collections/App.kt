package ru.ezhov.example.collections

import kotlin.system.measureTimeMillis

val col = listOf(
    1, 3, 5, 6, 7, 8, 4, 5753, 23, 123, 2, 354734, 5734, 4, 446, 235, 125, 134, 3, 46, 24658, 3568
)

fun main() {
    println("partition " + col.partition { it.mod(2) == 0 })
    println("sum " + col.sum())
    println("average " + col.average())
    println("sumOf " + col.sumOf { it * 2 })

    col.chunked(5) {
        println("chunked $it")
    }

    val employeeIds = listOf(5, 8, 13, 21, 34, 55, 89)
    val daysInCompany = listOf(176, 145, 117, 92, 70, 51, 35, 22, 12, 5)
    println("zip employeeIds to daysInCompany" + employeeIds.zip(daysInCompany))
    println("zip daysInCompany to employeeIds" + daysInCompany.zip(employeeIds))

    val (employees, days) = employeeIds.zip(daysInCompany).unzip()
    println("unzip employees: $employees and days: $days")

    // Streams are lazy, collections are not
    val numbers = (1..1_000_000).toList()
    println("as stream " + measureTimeMillis {
        numbers.stream().map {
            it * it
        }
    }) // ~2ms

    println("not stream " + measureTimeMillis {
        numbers.map {
            it * it
        }
    }) // ~19ms

    val seq = generateSequence(1) { it + 1 }

    seq.take(100)
}
