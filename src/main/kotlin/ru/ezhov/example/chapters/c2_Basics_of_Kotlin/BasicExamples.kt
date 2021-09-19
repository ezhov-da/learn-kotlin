package ru.ezhov.example.chapters.c2_Basics_of_Kotlin

// 1. The Unit return type declaration is optional for functions. The following codes are equivalent
fun printHello1(name: String?): Unit {
    if (name != null)
        println("Hello ${name}")
}

fun printHello2(name: String?) {
    //...
}

// 2. Single-Expression functions: When a function returns a single expression,
// the curly braces can be omitted and the body is specified after = symbol
fun double(x: Int): Int = x * 2

// Explicitly declaring the return type is optional when this can be inferred by the compiler
fun double1(x: Int) = x * 2

// 3. String interpolation: Using string values is easy
val num = 10
val s = "i = $num"

// 4. In Kotlin, the type system distinguishes between references that can hold null (nullable references) and those
// that can not (non-null references). For example, a regular variable of type String can not hold null:
var a: String = "abc"
// a = null // compilation error

// To allow nulls, we can declare a variable as nullable string, written String?:
var b: String? = "abc"
// b = null // ok

// 5. In Kotlin,== actually checks for equality of values. By convention, an expression like a == b is translated to
fun t() = a?.equals(b) ?: (b === null)