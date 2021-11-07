package ru.ezhov.example.ddd.context.domain

import arrow.core.Either
import ru.ezhov.example.ddd.context.domain.model.Book

interface BookRepository {
    suspend fun books(): Either<BookRepositoryException, List<Book>>
}