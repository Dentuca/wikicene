package wikicene.api

import io.vertx.core.http.HttpServerRequest

enum class SupportedParam(val param: String) {
    TERM("term"),
    QUERY_TYPE("queryType"),
    MAX_EDITS("maxEdits")
}

fun HttpServerRequest.getParam(param: SupportedParam): String =
    getNullableParam(param) ?: throw IllegalAccessException("Param ${param.param} was not provided")

fun HttpServerRequest.getNullableParam(param: SupportedParam): String? = getParam(param.param)
