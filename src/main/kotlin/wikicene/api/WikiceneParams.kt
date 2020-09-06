package wikicene.api

import io.vertx.core.http.HttpServerRequest
import wikicene.lucene.query.QueryType

class WikiceneParams(
    val term: String,
    val queryType: QueryType
) {

    companion object {

        private val defaultQuery = QueryType.PARSED

        fun from(ctx: HttpServerRequest): WikiceneParams {
            return WikiceneParams(
                term = ctx.getParam(SupportedParam.TERM),
                queryType = ctx.getNullableParam(SupportedParam.QUERY_TYPE)?.let {
                    QueryType.valueOf(it.toUpperCase())
                } ?: defaultQuery
            )
        }
    }
}
