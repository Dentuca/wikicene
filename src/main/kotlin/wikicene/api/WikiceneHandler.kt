package wikicene.api

import io.vertx.core.http.HttpServerResponse
import io.vertx.core.json.JsonObject
import wikicene.exception.StoreNotFoundException
import wikicene.lucene.store.StoreProvider
import wikicene.utils.timeIt

class WikiceneHandler(
    val params: WikiceneParams,
    val response: HttpServerResponse,
    private val stores: StoreProvider
) {

    fun execute() {

        val searcher = stores.get(params.storeType)?.searcher ?: throw StoreNotFoundException(
            "store ${params.storeType.name} not found"
        )

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
