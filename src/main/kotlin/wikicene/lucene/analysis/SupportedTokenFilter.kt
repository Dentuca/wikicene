package wikicene.lucene.analysis

import org.apache.lucene.analysis.core.LowerCaseFilterFactory
import org.apache.lucene.analysis.en.EnglishMinimalStemFilterFactory
import org.apache.lucene.analysis.en.EnglishPossessiveFilterFactory
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilterFactory

enum class SupportedTokenFilter(val id: String) {
    ASCII_FOLDING(ASCIIFoldingFilterFactory.NAME),
    LOWERCASE(LowerCaseFilterFactory.NAME),
    ENGLISH_PLURAL_STEM(EnglishMinimalStemFilterFactory.NAME), // plural -> singular
    ENGLISH_POSSESSIVE_STEM(EnglishPossessiveFilterFactory.NAME) // removes trailing 's
}
