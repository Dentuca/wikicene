package wikicene.api

import io.vertx.core.http.HttpServerRequest
import wikicene.lucene.LuceneField
import wikicene.lucene.query.QueryType
import wikicene.lucene.store.StoreType

class WikiceneParams(
    val term: String,
    val storeType: StoreType,
    val queryType: QueryType,
    val queryFields: Set<LuceneField>,
    val maxEdits: Int?
) {

    companion object {

        private val defaultQuery = QueryType.PARSED

        fun from(ctx: HttpServerRequest): WikiceneParams {

            return WikiceneParams(
                term = ctx.getParam(SupportedParam.TERM),

                storeType = ctx.getAllParams(SupportedParam.STORE_TYPE)
                    .takeIf { it.isNotEmpty() }
                    ?.joinToString("_", transform = String::toUpperCase)
                    ?.let { StoreType.valueOf(it) }
                    ?: StoreType.STANDARD,

                queryType = ctx.getNullableParam(SupportedParam.QUERY_TYPE)?.let {
                    QueryType.valueOf(it.toUpperCase())
                } ?: defaultQuery,

                queryFields = ctx.getAllParams(SupportedParam.QUERY_FIELD).map {
                    LuceneField.valueOf(it.toUpperCase())
                }.takeIf { it.isNotEmpty() }?.toSet() ?: setOf(LuceneField.TITLE),

                maxEdits = ctx.getNullableParam(SupportedParam.MAX_EDITS)?.toInt()
            )
        }
    }
}
