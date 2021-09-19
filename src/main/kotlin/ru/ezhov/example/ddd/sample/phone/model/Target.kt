@file:Suppress("DataClassPrivateConstructor")

package ru.ezhov.example.ddd.sample.phone.model

import arrow.core.Either

sealed class Target(val type: Type) {
    data class Phone private constructor(val value: String) : Target(type = Type.PHONE) {
        companion object {
            private const val VALID_PHONE = "\\d+"

            fun of(value: String): Either<ValidationErrors, Phone> {
                val errors = mutableListOf<String>()
                if (!VALID_PHONE.toRegex().matches(value)) {
                    errors.add("Bad phone '$value'. Phone must be equal '$VALID_PHONE'")
                }

                return when (errors.isEmpty()) {
                    true -> Either.Right(Phone(value = value))
                    false -> Either.Left(ValidationErrors(errors))
                }
            }
        }
    }

    data class Email private constructor(val value: String) : Target(type = Type.EMAIL) {
        companion object {
            private const val VALID_EMAIL = "\\w+@\\w+\\.\\w+"
            fun of(value: String): Either<ValidationErrors, Email> {
                val errors = mutableListOf<String>()
                if (!VALID_EMAIL.toRegex().matches(value)) {
                    errors.add("Bad email '$value'. Email must be equal '${VALID_EMAIL}'")
                }

                return when (errors.isEmpty()) {
                    true -> Either.Right(Email(value = value))
                    false -> Either.Left(ValidationErrors(errors))
                }
            }
        }
    }
}

data class ValidationErrors(val errors: List<String>)

enum class Type {
    PHONE,
    EMAIL
}

fun main() {
    printResult(Target.Phone.of("qweqweqweqweqwe"))
    printResult(Target.Phone.of("79564535352"))

    printResult(Target.Email.of("kb;dv80atdvasbdv"))
    printResult(Target.Email.of("test@test.ru"))
}

private fun <E, T> printResult(e: Either<E, T>) {
    when (e) {
        is Either.Left -> println(e.value)
        is Either.Right -> println(e.value)
    }
}