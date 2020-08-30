package wikicene.lucene.query

import org.apache.lucene.analysis.Analyzer
import org.apache.lucene.queryparser.classic.QueryParser
import org.apache.lucene.search.Query
import wikicene.lucene.LuceneField

class WikiceneQueryBuilder(
    private val term: String,
    private val queryType: QueryType,
    private val analyzer: Analyzer
) {

    fun build(): Query = when (queryType) {
        QueryType.PARSED -> buildParsedQuery()
    }

    private fun buildParsedQuery() = term.toQuery()

    private fun String.toQuery() = QueryParser(LuceneField.TITLE.fieldName, analyzer).parse(this)
}
