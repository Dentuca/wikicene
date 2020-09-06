package wikicene.lucene.query

import org.apache.lucene.analysis.Analyzer
import org.apache.lucene.queryparser.classic.QueryParser
import org.apache.lucene.search.FuzzyQuery
import org.apache.lucene.search.PrefixQuery
import org.apache.lucene.search.Query
import org.apache.lucene.search.TermQuery
import org.apache.lucene.search.WildcardQuery
import wikicene.lucene.LuceneField

class WikiceneQueryBuilder(
    private val term: String,
    private val queryType: QueryType,
    private val analyzer: Analyzer
) {

    fun build(): Query = when (queryType) {
        QueryType.PARSED -> term.toParsedQuery()
        QueryType.TERM -> term.toTermQuery()
        QueryType.PREFIX -> term.toPrefixQuery()
        QueryType.WILDCARD -> term.toWildcardQuery()
        QueryType.FUZZY -> term.toFuzzyQuery()
    }

    private fun String.toParsedQuery() = QueryParser(LuceneField.TITLE.fieldName, analyzer).parse(this)
    private fun String.toTermQuery() = TermQuery(LuceneField.TITLE.buildTerm(this))
    private fun String.toPrefixQuery() = PrefixQuery(LuceneField.TITLE.buildTerm(this))
    private fun String.toWildcardQuery() = WildcardQuery(LuceneField.TITLE.buildTerm(this))
    private fun String.toFuzzyQuery() = FuzzyQuery(LuceneField.TITLE.buildTerm(this))
}
