package ru.ezhov.example.ddd.context.domain.model

import arrow.core.Validated
import java.time.LocalDate
import java.time.format.DateTimeParseException

@JvmInline
value class AuthorBirthday private constructor(val value: LocalDate) {
    companion object {
        fun of(birthday: String): Validated<ModelErrors, AuthorBirthday> {
            val errors = mutableListOf<String>()

            try {
                val birthdayDate = LocalDate.parse(birthday)

                val now = LocalDate.now()
                if (birthdayDate.isAfter(now) || birthdayDate.isEqual(now)) errors.add("'birthday' must not by now or after")

                if (errors.isEmpty()) {
                    return Validated.Valid(AuthorBirthday(birthdayDate))
                }
            } catch (ex: DateTimeParseException) {
                errors.add("birthday value '$birthday' parse exception ${ex.message}")
            }

            return Validated.Invalid(ModelErrors(errors))
        }
    }
}
