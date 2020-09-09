package wikicene.lucene

import org.amshove.kluent.shouldBeEmpty
import org.amshove.kluent.shouldBeEqualTo
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import wikicene.buildArticle
import wikicene.buildParams
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

                    val params = buildParams(
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

                    val params = buildParams(
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

                    val params = buildParams(
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

            listOf(
                FuzzyQueryScenario("pokemon", 0, false),
                FuzzyQueryScenario("pokemon", 1, true),
                FuzzyQueryScenario("pokeman", 1, false),
                FuzzyQueryScenario("pokeman", 2, true),
                FuzzyQueryScenario("guld", 1, true),
                FuzzyQueryScenario("pokémon", 1, true),
                FuzzyQueryScenario("digimon", 1, false)
            ).forEach { (term, maxEdits, shouldMatch) ->

                describe("with term '$term'") {

                    val params = buildParams(
                        term = term,
                        queryType = QueryType.FUZZY,
                        maxEdits = maxEdits
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

private data class FuzzyQueryScenario(
    val term: String,
    val maxEdits: Int,
    val shouldMatch: Boolean
)
