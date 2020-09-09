package wikicene.lucene.query

import org.apache.lucene.analysis.Analyzer
import org.apache.lucene.queryparser.classic.QueryParser
import org.apache.lucene.search.BooleanClause
import org.apache.lucene.search.BooleanQuery
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

    fun build(): Query = BooleanQuery.Builder().apply {
        params.queryFields.forEach { field ->
            add(buildSingleQuery(params.queryType, field), BooleanClause.Occur.SHOULD)
        }
    }.build()

    private fun buildSingleQuery(queryType: QueryType, field: LuceneField) = when (queryType) {
        QueryType.PARSED -> buildParsedQuery(field)
        QueryType.TERM -> buildTermQuery(field)
        QueryType.PREFIX -> buildPrefixQuery(field)
        QueryType.WILDCARD -> buildWildcardQuery(field)
        QueryType.FUZZY -> buildFuzzyQuery(field)
    }

    private fun buildParsedQuery(field: LuceneField) = QueryParser(field.fieldName, analyzer).parse(params.term)
    private fun buildTermQuery(field: LuceneField) = TermQuery(field.buildTerm(params.term))
    private fun buildPrefixQuery(field: LuceneField) = PrefixQuery(field.buildTerm(params.term))
    private fun buildWildcardQuery(field: LuceneField) = WildcardQuery(field.buildTerm(params.term))
    private fun buildFuzzyQuery(field: LuceneField): FuzzyQuery {
        val luceneTerm = field.buildTerm(params.term)
        return when (params.maxEdits) {
            null -> FuzzyQuery(luceneTerm)
            else -> FuzzyQuery(luceneTerm, params.maxEdits)
        }
    }
}
