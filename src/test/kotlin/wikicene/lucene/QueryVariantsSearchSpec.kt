package wikicene.lucene

import org.amshove.kluent.shouldBeEmpty
import org.amshove.kluent.shouldBeEqualTo
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import wikicene.api.WikiceneParams
import wikicene.buildArticle
import wikicene.lucene.query.QueryType

object QueryVariantsSearchSpec : Spek ({

    describe("when searching for articles") {

        val title = "Pokémon Gold"
        val article = buildArticle(title = title)
        val searcher = TestSearcher(articles = listOf(article))

        describe("using Term Query") {

            mapOf(
                "pokémon" to true,
                "gold" to true,
                "pokémon gold" to false,
                "pokém" to false,
                "go" to false,
                "digimon" to false
            ).forEach { (term, shouldMatch) ->

                describe("with term '$term'") {

                    val params = WikiceneParams(
                        term = term,
                        queryType = QueryType.TERM
                    )

                    val searchResults = searcher.search(params)

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

        describe("using Prefix Query") {

            mapOf(
                "pok" to true,
                "pokém" to true,
                "pokémon" to true,
                "go" to true,
                "gold" to true,
                "pokémon go" to false,
                "digi" to false
            ).forEach { (term, shouldMatch) ->

                describe("with term '$term'") {

                    val params = WikiceneParams(
                        term = term,
                        queryType = QueryType.PREFIX
                    )

                    val searchResults = searcher.search(params)

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

        describe("using Wildcard Query") {

            mapOf(
                "pok*" to true,
                "*mon" to true,
                "*ké*" to true,
                "go*" to true,
                "pokémon" to true,
                "digi*" to false
            ).forEach { (term, shouldMatch) ->

                describe("with term '$term'") {

                    val params = WikiceneParams(
                        term = term,
                        queryType = QueryType.WILDCARD
                    )

                    val searchResults = searcher.search(params)

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

        describe("using Fuzzy Query") {

            mapOf(
                "pokemon" to true,
                "pokéman" to true,
                "guld" to true,
                "pokémon" to true,
                "digimon" to false
            ).forEach { (term, shouldMatch) ->

                describe("with term '$term'") {

                    val params = WikiceneParams(
                        term = term,
                        queryType = QueryType.FUZZY
                    )

                    val searchResults = searcher.search(params)

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
