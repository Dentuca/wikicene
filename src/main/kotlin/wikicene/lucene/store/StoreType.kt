package wikicene.lucene.store

import org.apache.lucene.analysis.core.KeywordAnalyzer
import org.apache.lucene.analysis.standard.StandardAnalyzer

enum class StoreType {
    STANDARD,
    KEYWORD
    ;

    private val subDir = name.toLowerCase().replace("_", "-")

    fun buildMMapStore() = MMapStore(
        analyzer = buildAnalyzer(),
        subDir = subDir
    )

    private fun buildAnalyzer() = when (this) {
        STANDARD -> StandardAnalyzer()
        KEYWORD -> KeywordAnalyzer()
    }
}
