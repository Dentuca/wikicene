package wikicene.api

import io.vertx.core.http.HttpServerRequest
import wikicene.lucene.query.WikiceneQueryBuilder

class WikiceneParams(
    val term: String
) {

    companion object {
        fun from(ctx: HttpServerRequest): WikiceneParams {
            return WikiceneParams(
                term = ctx.getParam(SupportedParam.TERM)
            )
        }
    }
}
