package ru.ezhov.example.ddd.context.domain.model

import arrow.core.Validated

@JvmInline
value class Isbn private constructor(val value: String) {
    companion object {
        private val ISBN_REGEXP = "^(?=(?:\\D*\\d){10}(?:(?:\\D*\\d){3})?\$)[\\d-]+\$".toRegex()

        fun of(isbn: String): Validated<ModelErrors, Isbn> {
            val errors = mutableListOf<String>()

            if (isbn.isEmpty()) errors.add("'isbn' must not be empty")
            if (!ISBN_REGEXP.matches(isbn)) errors.add("'isbn' must be format as ${ISBN_REGEXP.pattern}")

            return if (errors.isEmpty())
                Validated.Valid(Isbn(isbn))
            else
                Validated.Invalid(ModelErrors(errors))
        }
    }
}
