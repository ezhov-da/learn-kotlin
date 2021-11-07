package ru.ezhov.example.ddd.context.infrastructure.dto

import kotlinx.serialization.Serializable

@Serializable
data class BookDto(val name: String, val author: AuthorDto, val isbn: String)
