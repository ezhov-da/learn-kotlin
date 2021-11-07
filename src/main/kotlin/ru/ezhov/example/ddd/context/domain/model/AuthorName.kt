package ru.ezhov.example.ddd.context.domain.model

import arrow.core.Validated

@JvmInline
value class AuthorName private constructor(val value: String) {
    companion object {
        private const val MAX_LENGTH = 40

        fun of(name: String): Validated<ModelErrors, AuthorName> {
            val errors = mutableListOf<String>()

            if (name.isEmpty()) errors.add("'name' must not be empty")
            if (name.length > MAX_LENGTH) errors.add("'name' must not be greater $MAX_LENGTH chars")

            return if (errors.isEmpty())
                Validated.Valid(AuthorName(name))
            else
                Validated.Invalid(ModelErrors(errors))
        }
    }
}
