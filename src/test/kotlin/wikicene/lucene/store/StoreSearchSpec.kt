package wikicene.lucene.store

import org.amshove.kluent.shouldBeEqualTo
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import wikicene.addArticles
import wikicene.buildArticle
import wikicene.buildQueryParams
import wikicene.lucene.analysis.AnalyzerId
import wikicene.lucene.analysis.SupportedAnalyzerType
import wikicene.lucene.analysis.SupportedTokenFilter
import wikicene.lucene.analysis.SupportedTokenizer
import wikicene.lucene.query.QueryType

object StoreSearchSpec : Spek({

    describe("with a store with a custom analyzer with whitespace tokenizer and ascii+lowercase filters") {

        val title = "Pokémon Gold"

        val analyzerId = AnalyzerId.from(
            analyzerType = SupportedAnalyzerType.CUSTOM,
            tokenizer = SupportedTokenizer.WHITESPACE,
            tokenFilters = listOf(
                SupportedTokenFilter.ASCII_FOLDING,
                SupportedTokenFilter.LOWERCASE
            )
        )
        val store = InMemoryStore(analyzerId)

        val article = buildArticle(title = title)
        store.indexer.addArticles(article)

        listOf(
            "pokemon" to true,
            "gold" to true
        ).forEach { (term, shouldMatch) ->

            describe("querying with term '$term'") {

                val queryParams = buildQueryParams(
                    term = term,
                    queryType = QueryType.TERM
                )

                val result = store.searcher.search(queryParams)

                if (shouldMatch) {
                    it("should have one result") {
                        result.count() shouldBeEqualTo 1
                    }
                } else {
                    it("should have no results") {
                        result.count() shouldBeEqualTo 0
                    }
                }
            }
        }
    }
})
