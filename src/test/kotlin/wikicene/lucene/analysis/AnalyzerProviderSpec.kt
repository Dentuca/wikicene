package wikicene.lucene.analysis

import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeInstanceOf
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import kotlin.math.pow

object AnalyzerProviderSpec : Spek({

    describe("on accessing all analyzers in the provider") {

        val analyzerTypes = SupportedAnalyzerType.values().count()
        val tokenizers = SupportedTokenizer.values().count()
        val tokenFilters = SupportedTokenFilter.values().count()
        val expectedAnalyzerCount = analyzerTypes - 1 + tokenizers * 2.0.pow(tokenFilters).toInt()

        val allAnalyzers = AnalyzerProvider.all()

        it("should have '$expectedAnalyzerCount' analyzers") {
            allAnalyzers.count() shouldBeEqualTo expectedAnalyzerCount
        }
    }

    describe("on getting a non-custom analyzer from the provider given an analyzer id") {

        val analyzerType = SupportedAnalyzerType.STANDARD
        val analyzerId = AnalyzerId.from(analyzerType = analyzerType)

        val analyzer = AnalyzerProvider.get(analyzerId)

        it("should provide an analyzer of the expected type") {
            analyzer shouldBeInstanceOf StandardAnalyzer::class
        }
    }
})
