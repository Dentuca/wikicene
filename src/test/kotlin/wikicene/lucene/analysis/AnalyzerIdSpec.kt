package wikicene.lucene.analysis

import org.amshove.kluent.shouldBeEqualTo
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object AnalyzerIdSpec : Spek({

    describe("on comparing two analyzer id's built with the same parameters") {

        val analyzerType = SupportedAnalyzerType.CUSTOM
        val tokenizer = SupportedTokenizer.WHITESPACE
        val tokenFilters = listOf(
            SupportedTokenFilter.ASCII_FOLDING,
            SupportedTokenFilter.ENGLISH_POSSESSIVE_STEM
        )

        val analyzerId1 = AnalyzerId.from(
            analyzerType = analyzerType,
            tokenizer = tokenizer,
            tokenFilters = tokenFilters
        )

        val analyzerId2 = AnalyzerId.from(
            analyzerType = analyzerType,
            tokenizer = tokenizer,
            tokenFilters = tokenFilters
        )

        it("should be equal") {
            analyzerId1 shouldBeEqualTo analyzerId2
        }
    }

    describe("on converting an id of a custom analyzer to a string") {

        val analyzerType = SupportedAnalyzerType.CUSTOM
        val tokenizer = SupportedTokenizer.WHITESPACE
        val tokenFilters = listOf(
            SupportedTokenFilter.ASCII_FOLDING,
            SupportedTokenFilter.ENGLISH_POSSESSIVE_STEM
        )
        val expectedString = "${analyzerType.id}-${tokenizer.id}-${tokenFilters.joinToString("_", transform = {it.id})}"

        val analyzerId = AnalyzerId.from(
            analyzerType = analyzerType,
            tokenizer = tokenizer,
            tokenFilters = tokenFilters
        )

        val analyzerIdAsString = analyzerId.toString()

        it("should have the expected string '$expectedString'") {
            analyzerIdAsString shouldBeEqualTo expectedString
        }
    }
})
