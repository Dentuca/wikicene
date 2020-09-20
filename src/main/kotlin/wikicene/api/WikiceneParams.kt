package wikicene.api

import io.vertx.core.http.HttpServerRequest
import wikicene.lucene.LuceneField
import wikicene.lucene.analysis.AnalyzerId
import wikicene.lucene.analysis.SupportedAnalyzerType
import wikicene.lucene.analysis.SupportedTokenFilter
import wikicene.lucene.analysis.SupportedTokenizer
import wikicene.lucene.query.QueryType

class WikiceneParams(
    val indexationAnalyzerParams: IndexationAnalyzerParams,
    val queryParams: QueryParams
) {

    companion object {

        fun from(ctx: HttpServerRequest): WikiceneParams {
            val indexationAnalyzerParams = IndexationAnalyzerParams.from(ctx)
            val queryParams = QueryParams.from(ctx)
            return WikiceneParams(
                indexationAnalyzerParams = indexationAnalyzerParams,
                queryParams = queryParams
            )
        }
    }
}

class IndexationAnalyzerParams(
    val analyzerType: SupportedAnalyzerType,
    val tokenizer: SupportedTokenizer?,
    val tokenFilters: List<SupportedTokenFilter>
) {
    companion object {

        private val defaultAnalyzerType = SupportedAnalyzerType.STANDARD

        fun from(ctx: HttpServerRequest) = IndexationAnalyzerParams(
            analyzerType = ctx.getNullableEnumParam<SupportedAnalyzerType>(
                SupportedParam.ANALYZER_TYPE
            ) ?: defaultAnalyzerType,
            tokenizer = ctx.getNullableEnumParam<SupportedTokenizer>(SupportedParam.TOKENIZER),
            tokenFilters = ctx.getAllEnumParams(SupportedParam.TOKEN_FILTER)
        )
    }

    fun toAnalyzerId() = AnalyzerId.from(
        analyzerType = analyzerType,
        tokenizer = tokenizer,
        tokenFilters = tokenFilters
    )
}

class QueryParams(
    val term: String,
    val queryType: QueryType,
    val queryFields: Set<LuceneField>,
    val maxEdits: Int?
) {
    companion object {

        private val defaultQueryType = QueryType.PARSED
        private val defaultLuceneFields = setOf(LuceneField.TITLE)

        fun from(ctx: HttpServerRequest) = QueryParams(
            term = ctx.getParam(SupportedParam.TERM),
            queryType = ctx.getNullableEnumParam<QueryType>(SupportedParam.QUERY_TYPE) ?: defaultQueryType,
            queryFields = ctx.getAllEnumParams<LuceneField>(SupportedParam.QUERY_FIELD)
                .takeIf { it.isNotEmpty() }?.toSet() ?: defaultLuceneFields,
            maxEdits = ctx.getNullableParam(SupportedParam.MAX_EDITS)?.toInt()
        )
    }
}
