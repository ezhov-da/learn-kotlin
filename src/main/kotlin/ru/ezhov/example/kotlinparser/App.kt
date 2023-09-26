package ru.ezhov.example.kotlinparser

import kotlinx.ast.common.AstSource
import kotlinx.ast.grammar.kotlin.common.summary
import kotlinx.ast.grammar.kotlin.target.antlr.kotlin.KotlinGrammarAntlrKotlinParser

fun main() {
    val source = AstSource.File(
        "src/main/kotlin/ru/ezhov/example/kotlinparser/TestService.kt"
    )
    val kotlinFile = KotlinGrammarAntlrKotlinParser.parseKotlinFile(source)
    kotlinFile.summary(false)
        .onSuccess { astList ->
            astList.forEach(::println)
        }.onFailure { errors ->
            errors.forEach(::println)
        }
}
