package ru.ezhov.example.lucene

import org.apache.lucene.analysis.Analyzer
import org.apache.lucene.analysis.LowerCaseFilter
import org.apache.lucene.analysis.StopFilter
import org.apache.lucene.analysis.TokenStream
import org.apache.lucene.analysis.en.PorterStemFilter
import org.apache.lucene.analysis.miscellaneous.CapitalizationFilter
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.analysis.standard.StandardFilter
import org.apache.lucene.analysis.standard.StandardTokenizer


class MyCustomAnalyzer : Analyzer() {
    override fun createComponents(fieldName: String): TokenStreamComponents {
        val src = StandardTokenizer()
        var result: TokenStream? = StandardFilter(src)
        result = LowerCaseFilter(result)
        result = StopFilter(result, StandardAnalyzer.STOP_WORDS_SET)
        result = PorterStemFilter(result)
        result = CapitalizationFilter(result)
        return TokenStreamComponents(src, result)
    }
}
