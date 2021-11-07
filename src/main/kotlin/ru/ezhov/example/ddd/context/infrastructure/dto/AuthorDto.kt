package ru.ezhov.example.ddd.context.infrastructure.dto

import kotlinx.serialization.Serializable

@Serializable
data class AuthorDto(val name: String, val birthday: String)
