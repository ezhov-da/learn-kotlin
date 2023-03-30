package ru.ezhov.example.lucene

import org.apache.lucene.analysis.ru.RussianAnalyzer
import org.apache.lucene.document.Document
import org.apache.lucene.store.RAMDirectory

// https://github.com/eugenp/tutorials/tree/master/lucene
// https://www.baeldung.com/lucene
// https://lucene.apache.org/core/4_0_0/core/org/apache/lucene/search/MultiPhraseQuery.html
fun main() {
    val inMemoryLuceneIndex = InMemoryLuceneIndex(RAMDirectory(), RussianAnalyzer())
    inMemoryLuceneIndex.indexDocument("Hello world", "Some hello world")
    inMemoryLuceneIndex.indexDocument("Абракадабра world", "Отсутствует поиск hello world")
    inMemoryLuceneIndex.indexDocument("world", "hello")
    inMemoryLuceneIndex.indexDocument("world", "http://rest/api стандарт word")
    inMemoryLuceneIndex.indexDocument("world", "http://rest/api стандарт бла")
    inMemoryLuceneIndex.indexDocument("world", "http://rest/api стандарт б")
    inMemoryLuceneIndex.indexDocument("world", "http://rest/api станда")
    inMemoryLuceneIndex.indexDocument("world", "Регламент проведения анализа логов")
    inMemoryLuceneIndex.indexDocument("world", "Регламент анализа проведения логов")

    var documents: List<Document> = inMemoryLuceneIndex.searchIndex("body", "so world")
    println(documents)

    documents = inMemoryLuceneIndex.searchIndex("body", "станда бла")
    println(documents)

    documents = inMemoryLuceneIndex.searchIndex("body", "станда")
    println(documents)

    documents = inMemoryLuceneIndex.searchIndex("body", "анализ логов")
    println(documents)

    documents = inMemoryLuceneIndex.searchIndex("body", "api")
    println(documents)

    documents = inMemoryLuceneIndex.searchIndex("body", "api станд")
    println(documents)
}
