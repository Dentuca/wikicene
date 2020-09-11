package wikicene.api

import io.vertx.core.http.HttpServerResponse
import io.vertx.core.json.JsonObject
import wikicene.lucene.Searcher
import wikicene.utils.timeIt

class WikiceneHandler(
    val params: WikiceneParams,
    val searcher: Searcher,
    val response: HttpServerResponse
) {

    fun execute() {

        val (articles, time) = timeIt {
            searcher.search(params)
        }

        val jsonResponse = JsonObject.mapFrom(
            WikiceneResponse(
                articles = articles,
                time = time
            )
        )

        response
            // .putHeader("Access-Control-Allow-Origin", "*")
            .putHeader("content-type", "application/json")
            .end(jsonResponse.encode())
    }
}
