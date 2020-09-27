package wikicene.api

import io.vertx.core.AbstractVerticle
import io.vertx.core.Vertx
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.StaticHandler
import wikicene.lucene.store.StoreProvider

class WikiceneVerticle(private val stores: StoreProvider) : AbstractVerticle() {

    override fun start() {

        val vertx = Vertx.vertx()
        val router = Router.router(vertx)

        val rootRoute = router.route("/")
        rootRoute.handler { routingContext ->
            val params = WikiceneParams.from(routingContext.request())
            WikiceneHandler(
                params = params,
                response = routingContext.response(),
                stores = stores
            ).execute()
        }

        rootRoute.failureHandler { frc ->
            println("Error when handling request: ${frc.failure()}")
            val failureResponse = JsonObject.mapFrom(
                FailureResponse(
                    error = frc.statusCode(),
                    message = frc.failure().message ?: ""
                )
            )

            // note: this response is not reaching the frontend, so this is probably
            // not the correct way to do this
            frc.response()
                .setStatusCode(frc.statusCode())
                .end(failureResponse.encode())
        }

        router.route("/frontend*").handler(StaticHandler.create("frontend"))

        vertx.createHttpServer()
            .requestHandler(router)
            .listen(8080)
    }
}

data class FailureResponse(
    val error: Int,
    val message: String
)
