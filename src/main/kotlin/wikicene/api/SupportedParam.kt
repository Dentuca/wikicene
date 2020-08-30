package wikicene.api

import io.vertx.core.http.HttpServerRequest

enum class SupportedParam(val param: String) {
    TERM("term")
}

fun HttpServerRequest.getParam(param: SupportedParam): String = getParam(param.param)
