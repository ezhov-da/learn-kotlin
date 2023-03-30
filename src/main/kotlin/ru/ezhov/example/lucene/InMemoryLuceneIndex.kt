package ru.ezhov.example.lucene

import org.apache.lucene.analysis.Analyzer
import org.apache.lucene.document.Document
import org.apache.lucene.document.Field
import org.apache.lucene.document.SortedDocValuesField
import org.apache.lucene.document.TextField
import org.apache.lucene.index.DirectoryReader
import org.apache.lucene.index.IndexReader
import org.apache.lucene.index.IndexWriter
import org.apache.lucene.index.IndexWriterConfig
import org.apache.lucene.index.Term
import org.apache.lucene.queryparser.classic.ParseException
import org.apache.lucene.search.FuzzyQuery
import org.apache.lucene.search.IndexSearcher
import org.apache.lucene.search.Query
import org.apache.lucene.search.Sort
import org.apache.lucene.search.TopDocs
import org.apache.lucene.search.spans.SpanMultiTermQueryWrapper
import org.apache.lucene.search.spans.SpanNearQuery
import org.apache.lucene.store.Directory
import org.apache.lucene.util.BytesRef
import java.io.IOException


class InMemoryLuceneIndex(private val memoryIndex: Directory, private val analyzer: Analyzer) {
    fun indexDocument(title: String, body: String) {
        val indexWriterConfig = IndexWriterConfig(analyzer)
        try {
            val writer = IndexWriter(memoryIndex, indexWriterConfig)
            val document = Document()
            document.add(TextField("title", title.lowercase(), Field.Store.YES))
            document.add(TextField("body", body.lowercase(), Field.Store.YES))
            document.add(SortedDocValuesField("title", BytesRef(title)))
            writer.addDocument(document)
            writer.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun searchIndex(inField: String, queryString: String): List<Document> {
        try {
            // https://stackoverflow.com/questions/18100233/lucene-fuzzy-search-on-a-phrase-fuzzyquery-spanquery

            val query = queryString
                .split(" ")
                .let { array ->
                    when (array.size > 1) {
                        true -> {
                            val clauses = array.map { value ->
                                SpanMultiTermQueryWrapper(FuzzyQuery(Term(inField, value)))
                            }.toTypedArray()
                            SpanNearQuery(clauses, 5, true)
                        }

                        false -> {
                            FuzzyQuery(Term(inField, array[0]))
                        }
                    }
                }

//            val builder = PhraseQuery.Builder()
//            queryString.split(" ").forEach {
//                builder.add(Term(inField, it))
//            }
//            builder.setSlop(100)
//            val pq = builder.build()

            val indexReader: IndexReader = DirectoryReader.open(memoryIndex)
            val searcher = IndexSearcher(indexReader)
            val topDocs = searcher.search(query, 10)
            val documents: MutableList<Document> = ArrayList()
            for (scoreDoc in topDocs.scoreDocs) {
                documents.add(searcher.doc(scoreDoc.doc))
            }
            return documents
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return emptyList()
    }

    fun deleteDocument(term: Term?) {
        try {
            val indexWriterConfig = IndexWriterConfig(analyzer)
            val writter = IndexWriter(memoryIndex, indexWriterConfig)
            writter.deleteDocuments(term)
            writter.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun searchIndex(query: Query?): List<Document>? {
        try {
            val indexReader: IndexReader = DirectoryReader.open(memoryIndex)
            val searcher = IndexSearcher(indexReader)
            val topDocs = searcher.search(query, 10)
            val documents: MutableList<Document> = ArrayList()
            for (scoreDoc in topDocs.scoreDocs) {
                documents.add(searcher.doc(scoreDoc.doc))
            }
            return documents
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    fun searchIndex(query: Query?, sort: Sort?): List<Document>? {
        try {
            val indexReader: IndexReader = DirectoryReader.open(memoryIndex)
            val searcher = IndexSearcher(indexReader)
            val topDocs: TopDocs = searcher.search(query, 10, sort)
            val documents: MutableList<Document> = ArrayList()
            for (scoreDoc in topDocs.scoreDocs) {
                documents.add(searcher.doc(scoreDoc.doc))
            }
            return documents
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }
}
