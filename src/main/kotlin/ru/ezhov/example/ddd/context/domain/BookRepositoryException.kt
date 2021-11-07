package ru.ezhov.example.ddd.context.domain

sealed class BookRepositoryException(message: String, cause: Exception? = null) : Exception(message, cause) {
    class Validation(message: String) : BookRepositoryException(message)
    class Generic(message: String) : BookRepositoryException(message)
}