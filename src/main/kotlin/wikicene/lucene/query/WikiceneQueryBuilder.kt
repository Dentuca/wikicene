package wikicene.lucene.query

import org.apache.lucene.analysis.Analyzer
import org.apache.lucene.queryparser.classic.QueryParser
import org.apache.lucene.search.FuzzyQuery
import org.apache.lucene.search.PrefixQuery
import org.apache.lucene.search.Query
import org.apache.lucene.search.TermQuery
import org.apache.lucene.search.WildcardQuery
import wikicene.api.WikiceneParams
import wikicene.lucene.LuceneField

class WikiceneQueryBuilder(
    private val params: WikiceneParams,
    private val analyzer: Analyzer
) {

    fun build(): Query = when (params.queryType) {
        QueryType.PARSED -> params.toParsedQuery()
        QueryType.TERM -> params.toTermQuery()
        QueryType.PREFIX -> params.toPrefixQuery()
        QueryType.WILDCARD -> params.toWildcardQuery()
        QueryType.FUZZY -> params.toFuzzyQuery()
    }

    private fun WikiceneParams.toParsedQuery() = QueryParser(LuceneField.TITLE.fieldName, analyzer).parse(term)
    private fun WikiceneParams.toTermQuery() = TermQuery(LuceneField.TITLE.buildTerm(term))
    private fun WikiceneParams.toPrefixQuery() = PrefixQuery(LuceneField.TITLE.buildTerm(term))
    private fun WikiceneParams.toWildcardQuery() = WildcardQuery(LuceneField.TITLE.buildTerm(term))
    private fun WikiceneParams.toFuzzyQuery(): FuzzyQuery {
        val luceneTerm = LuceneField.TITLE.buildTerm(term)
        return when (maxEdits) {
            null -> FuzzyQuery(luceneTerm)
            else -> FuzzyQuery(luceneTerm, maxEdits)
        }
    }
}
