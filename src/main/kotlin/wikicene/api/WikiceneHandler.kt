package wikicene.api

import io.vertx.core.http.HttpServerResponse
import io.vertx.core.json.JsonObject
import wikicene.lucene.Searcher

class WikiceneHandler(
    val params: WikiceneParams,
    val searcher: Searcher,
    val response: HttpServerResponse
) {

    fun execute() {

        val jsonResponse = JsonObject.mapFrom(
            WikiceneResponse(
                articles = searcher.search(params)
            )
        )

        response
            // .putHeader("Access-Control-Allow-Origin", "*")
            .putHeader("content-type", "application/json")
            .end(jsonResponse.encode())
    }
}
