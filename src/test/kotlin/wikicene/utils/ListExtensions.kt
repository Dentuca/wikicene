package wikicene.utils

import org.amshove.kluent.shouldContainSame
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object ListExtensions : Spek ({

    describe("on retrieving all combinations from an empty list") {
        val items = listOf<String>()
        val expectedCombinations = listOf(
            emptyList<String>()
        )

        val combinations = items.allCombinations()

        it("should have only one combination") {
            combinations shouldContainSame expectedCombinations
        }
    }

    describe("on retrieving all combinations from a single-item list") {
        val items = listOf("a")
        val expectedCombinations = listOf(
            emptyList(),
            listOf("a")
        )

        val combinations = items.allCombinations()

        it("should have only one combination") {
            combinations shouldContainSame expectedCombinations
        }
    }

    describe("on retrieving all combinations from a list with multiple items") {
        val items = listOf("a", "b", "c")
        val expectedCombinations = listOf(
            emptyList(),
            listOf("a"), listOf("b"), listOf("c"),
            listOf("a", "b"), listOf("a", "c"), listOf("b", "c"),
            listOf("a", "b", "c")
        )

        val combinations = items.allCombinations()

        it("should have only one combination") {
            combinations shouldContainSame expectedCombinations
        }
    }
})
