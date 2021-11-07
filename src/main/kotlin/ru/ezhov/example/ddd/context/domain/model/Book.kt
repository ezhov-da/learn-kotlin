package ru.ezhov.example.ddd.context.domain.model

/**
 * Использование value классов позволяет не валидировать data классы,
 * и делает приватный конструктор ненужным
 */
data class Book(
        val name: BookName,
        val author: Author,
        val isbn: Isbn,
)
