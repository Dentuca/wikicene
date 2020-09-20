package wikicene.lucene.analysis

import org.apache.lucene.analysis.core.KeywordTokenizerFactory
import org.apache.lucene.analysis.core.WhitespaceTokenizerFactory

enum class SupportedTokenizer(val id: String) {
    KEYWORD(KeywordTokenizerFactory.NAME),
    WHITESPACE(WhitespaceTokenizerFactory.NAME)
}
