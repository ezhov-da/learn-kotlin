package ru.ezhov.example.mustache

import com.github.mustachejava.DefaultMustacheFactory
import java.io.StringReader
import java.io.StringWriter

fun main() {
    val template = """
        <h2>{{title}}</h2>
        <small>Created on {{createdOn}}</small>
        <p>{{text}}</p>
    """.trimIndent()

    val mf = DefaultMustacheFactory()
    val m = mf.compile(StringReader(template), "hz")

    val sw = StringWriter()
    val map = mapOf(
        "title" to "1111",
        "createdOn" to "22222",
        "text" to "3333",
    )

    m.execute(sw, map).flush()

    println(sw.toString())
}

