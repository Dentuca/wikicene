package wikicene.lucene.analysis

import org.apache.lucene.analysis.Analyzer
import org.apache.lucene.analysis.core.KeywordAnalyzer
import org.apache.lucene.analysis.custom.CustomAnalyzer
import org.apache.lucene.analysis.standard.StandardAnalyzer
import wikicene.exception.AnalyzerNotFoundException
import wikicene.utils.allCombinations

data class AnalyzerId(private val id: String) {

    companion object {

        private const val SEP = "-"
        private const val TOKEN_FILTER_SEP = "_"

        fun from(analyzerType: SupportedAnalyzerType,
            tokenizer: SupportedTokenizer? = null,
            tokenFilters: List<SupportedTokenFilter>? = null
        ): AnalyzerId {
            val id = StringBuilder()
            id.append(analyzerType.id)
            if (analyzerType == SupportedAnalyzerType.CUSTOM) {
                id.append("${SEP}${tokenizer?.id}")
                id.append("${SEP}${tokenFilters?.joinToString(TOKEN_FILTER_SEP, transform = { it.id })}")
            }
            return AnalyzerId(id.toString())
        }
    }

    override fun toString() = id
}

object AnalyzerProvider {

    private val analyzers = mutableMapOf<AnalyzerId, Analyzer>()

    init {
        SupportedAnalyzerType.values().forEach { analyzerType -> when(analyzerType) {
            SupportedAnalyzerType.KEYWORD -> analyzerType.register(KeywordAnalyzer())
            SupportedAnalyzerType.STANDARD -> analyzerType.register(StandardAnalyzer())
            SupportedAnalyzerType.CUSTOM -> {
                SupportedTokenizer.values().forEach { tokenizer ->

                // custom analyzers with all combinations of the supported token filters
                SupportedTokenFilter.values()
                    .toList()
                    .allCombinations()
                    .map { tokenFilterCombination ->
                        val analyzer = buildCustomAnalyzer(tokenizer, tokenFilterCombination)
                        analyzerType.register(
                            analyzer = analyzer,
                            tokenizer = tokenizer,
                            tokenFilters = tokenFilterCombination
                        )
                    }
                }
            }
        }}
    }

    fun all() = analyzers.keys.toList()

    fun get(analyzerId: AnalyzerId) = analyzers[analyzerId] ?: throw AnalyzerNotFoundException(analyzerId)

    private fun SupportedAnalyzerType.register(
        analyzer: Analyzer,
        tokenizer: SupportedTokenizer? = null,
        tokenFilters: List<SupportedTokenFilter>? = null
    ) {
        val analyzerId = AnalyzerId.from(this, tokenizer, tokenFilters)
        analyzers[analyzerId] = analyzer
    }

    private fun buildCustomAnalyzer(
        tokenizer: SupportedTokenizer,
        tokenFilters: List<SupportedTokenFilter>
    ) = CustomAnalyzer.builder()
        .withTokenizer(tokenizer.id)
        .addTokenFilters(tokenFilters)
        .build()

    private fun CustomAnalyzer.Builder.addTokenFilters(tokenFilters: List<SupportedTokenFilter>) = apply {
        tokenFilters.forEach {
            addTokenFilter(it.id)
        }
    }
}
