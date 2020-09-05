package wikicene.api.frontend

import io.vertx.core.http.HttpServerResponse
import wikicene.utils.readResource
import java.io.IOException

class FrontendHandler(
    private val file: String,
    private val response: HttpServerResponse
) {

    companion object {
        const val HOME_PAGE_FILE = "index.html"
        private const val FRONTEND_DIR = "frontend"
    }

    fun execute() {

        val path = "$FRONTEND_DIR/$file"
        val htmlResponse = readResource(path) ?: throw IOException("File $path not found")

        response
            .putHeader("content-type", "text/html")
            .end(htmlResponse)
    }
}
