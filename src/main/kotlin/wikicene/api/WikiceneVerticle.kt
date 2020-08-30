package wikicene.api

import io.vertx.core.AbstractVerticle
import io.vertx.core.Vertx
import io.vertx.ext.web.Router
import wikicene.lucene.Searcher

class WikiceneVerticle(private val searcher: Searcher) : AbstractVerticle() {

    override fun start() {

        val vertx = Vertx.vertx()
        val router = Router.router(vertx)

        router.route("/").handler { routingContext ->
            val params = WikiceneParams.from(routingContext.request())
            val handler = WikiceneHandler(
                params = params,
                searcher = searcher,
                response = routingContext.response()
            )
            handler.execute()
        }

        vertx.createHttpServer()
            .requestHandler(router)
            .listen(8080)
    }
}
