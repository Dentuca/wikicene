package wikicene.api

import io.vertx.core.AbstractVerticle
import io.vertx.core.Vertx
import io.vertx.ext.web.Router
import wikicene.api.frontend.FrontendHandler
import wikicene.lucene.Searcher

class WikiceneVerticle(private val searcher: Searcher) : AbstractVerticle() {

    override fun start() {

        val vertx = Vertx.vertx()
        val router = Router.router(vertx)

        router.route("/").handler { routingContext ->
            val params = WikiceneParams.from(routingContext.request())
            WikiceneHandler(
                params = params,
                searcher = searcher,
                response = routingContext.response()
            ).execute()
        }

        router.route("/frontend").handler { routingContext ->
            FrontendHandler(
                file = FrontendHandler.HOME_PAGE_FILE,
                response = routingContext.response()
            ).execute()
        }

        router.route("/frontend/:file").handler { routingContext ->
            FrontendHandler(
                file = routingContext.request().getParam("file"),
                response = routingContext.response()
            ).execute()
        }

        vertx.createHttpServer()
            .requestHandler(router)
            .listen(8080)
    }
}
