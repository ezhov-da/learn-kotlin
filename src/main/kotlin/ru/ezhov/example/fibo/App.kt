package ru.ezhov.example.fibo

fun main() {
    val a = readln()!!.toInt()
    println(recursiveFib(a, array(50)))
}

private fun recursiveFib(n: Int, array: LongArray): Long = array[n]

private fun array(size: Int) = LongArray(size)
    .also { array ->
        array[0] = 0
        array[1] = 1
        (2 until size)
            .forEach { number ->
                array[number] = array[number - 1] + array[number - 2]
            }
    }
