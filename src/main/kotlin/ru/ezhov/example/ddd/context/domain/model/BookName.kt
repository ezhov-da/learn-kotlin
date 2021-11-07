package ru.ezhov.example.ddd.context.domain.model

import arrow.core.Validated

@JvmInline
value class BookName private constructor(val value: String) {
    companion object {
        private const val MAX_LENGTH = 100

        fun of(name: String): Validated<ModelErrors, BookName> {
            val errors = mutableListOf<String>()

            if (name.isEmpty()) errors.add("'name' must not be empty")
            if (name.length > MAX_LENGTH) errors.add("'name' must not be greater $MAX_LENGTH chars")

            return if (errors.isEmpty())
                Validated.Valid(BookName(name))
            else
                Validated.Invalid(ModelErrors(errors))
        }
    }
}
