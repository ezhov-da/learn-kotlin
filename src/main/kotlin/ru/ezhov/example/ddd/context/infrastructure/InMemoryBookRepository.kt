package ru.ezhov.example.ddd.context.infrastructure

import arrow.core.Either
import arrow.core.Validated
import arrow.core.valid
import arrow.core.zip
import arrow.typeclasses.Semigroup
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import mu.KotlinLogging
import ru.ezhov.example.ddd.context.domain.BookRepository
import ru.ezhov.example.ddd.context.domain.BookRepositoryException
import ru.ezhov.example.ddd.context.domain.model.Author
import ru.ezhov.example.ddd.context.domain.model.AuthorBirthday
import ru.ezhov.example.ddd.context.domain.model.AuthorName
import ru.ezhov.example.ddd.context.domain.model.Book
import ru.ezhov.example.ddd.context.domain.model.BookName
import ru.ezhov.example.ddd.context.domain.model.Isbn
import ru.ezhov.example.ddd.context.domain.model.ModelErrors
import ru.ezhov.example.ddd.context.infrastructure.dto.BookDto

private val logger = KotlinLogging.logger { }

class InMemoryBookRepository : BookRepository {
    private val jsonBooks = """
        [
        {"name":"One book","author":{"name":"Author name","birthday":"1921-09-19"},"isbn":"1-56619-909-3"},
        {"name":"Two book","author":{"name":"Author name","birthday":"1921-09-19"},"isbn":"1-56619-909-3"},
        {"name":"Three book","author":{"name":"Author name","birthday":"1921-09-19"},"isbn":"1-56619-909-3"},
        {"name":"Four book","author":{"name":"Author name","birthday":"1921-09-19"},"isbn":"1-56619-909-3"},
        {"name":"Five book","author":{"name":"Author name","birthday":"1921-09-19"},"isbn":"1-56619-909-3"},
        ]
    """.trimIndent()


    override suspend fun books(): Either<BookRepositoryException, List<Book>> {
        val booksDto = Json.decodeFromString<List<BookDto>>(jsonBooks)
        val books = mutableListOf<Book>()
        for (bookDto in booksDto) {
//            val validatedBook = BookName.of(bookDto.name).zip(
//                    Semigroup.nonEmptyList<ModelErrors>().valid().,
//                    AuthorName.of(bookDto.author.name),
//                    AuthorBirthday.of(bookDto.author.birthday),
//                    Isbn.of(bookDto.isbn)
//            ) { bookName, authorName, authorBirthday, isbn ->
//                Book(
//                        name = bookName,
//                        author = Author(
//                                name = authorName,
//                                birthday = authorBirthday
//                        ),
//                        isbn = isbn
//                )
//            }
//
//            when (validatedBook) {
//                is Validated.Valid -> books.add(validatedBook.value)
//                is Validated.Invalid -> logger.warn { validatedBook.value.all }
//            }

        }

        return Either.Right(books)
    }
}