package ru.ezhov.example.ddd.context.infrastructure

import org.junit.jupiter.api.Test

internal class InMemoryBookRepositoryTest {
    @Test
    suspend fun test() {
        val repository = InMemoryBookRepository()

        repository.books()
    }
}