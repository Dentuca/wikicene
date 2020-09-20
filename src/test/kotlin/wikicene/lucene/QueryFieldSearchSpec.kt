package wikicene.lucene

import org.amshove.kluent.shouldBeEmpty
import org.amshove.kluent.shouldBeEqualTo
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import wikicene.buildArticle
import wikicene.buildParams
import wikicene.lucene.query.QueryType

object QueryFieldSearchSpec : Spek({

    describe("when searching for articles") {

        val title = "Pokémon Gold"
        val summary = "A Japanese video game."
        val article = buildArticle(
            title = title,
            summary = summary
        )
        val searcher = TestSearcher(articles = listOf(article))

        describe("matching against the title field") {

            mapOf(
                "pokémon" to true,
                "gold" to true,
                "video" to false,
                "game" to false
            ).forEach { (term, shouldMatch) ->

                describe("with term '$term'") {

                    val params = buildParams(
                        term = term,
                        queryType = QueryType.TERM,
                        queryFields = setOf(LuceneField.TITLE)
                    )

                    val searchResults = searcher.search(params.queryParams)

                    if (shouldMatch) {
                        it("should have one result") {
                            searchResults.size shouldBeEqualTo 1
                        }
                    } else {
                        it("should have zero results") {
                            searchResults.shouldBeEmpty()
                        }
                    }
                }
            }
        }

        describe("matching against the summary field") {
            mapOf(
                "pokémon" to false,
                "gold" to false,
                "video" to true,
                "game" to true
            ).forEach { (term, shouldMatch) ->

                describe("with term '$term'") {

                    val params = buildParams(
                        term = term,
                        queryType = QueryType.TERM,
                        queryFields = setOf(LuceneField.SUMMARY)
                    )

                    val searchResults = searcher.search(params.queryParams)

                    if (shouldMatch) {
                        it("should have one result") {
                            searchResults.size shouldBeEqualTo 1
                        }
                    } else {
                        it("should have zero results") {
                            searchResults.shouldBeEmpty()
                        }
                    }
                }
            }
        }

        describe("matching against both the title and summary fields") {
            mapOf(
                "pokémon" to true,
                "gold" to true,
                "video" to true,
                "game" to true,
                "digimon" to false
            ).forEach { (term, shouldMatch) ->

                describe("with term '$term'") {

                    val params = buildParams(
                        term = term,
                        queryType = QueryType.TERM,
                        queryFields = setOf(LuceneField.TITLE, LuceneField.SUMMARY)
                    )

                    val searchResults = searcher.search(params.queryParams)

                    if (shouldMatch) {
                        it("should have one result") {
                            searchResults.size shouldBeEqualTo 1
                        }
                    } else {
                        it("should have zero results") {
                            searchResults.shouldBeEmpty()
                        }
                    }
                }
            }
        }
    }
})
