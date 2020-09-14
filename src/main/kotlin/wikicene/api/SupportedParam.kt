package wikicene.api

import io.vertx.core.http.HttpServerRequest

enum class SupportedParam(val param: String) {
    TERM("term"),
    STORE_TYPE("storeType"),
    QUERY_TYPE("queryType"),
    QUERY_FIELD("queryField"),
    MAX_EDITS("maxEdits")
}

fun HttpServerRequest.getParam(param: SupportedParam): String =
    getNullableParam(param) ?: throw IllegalAccessException("Param ${param.param} was not provided")

fun HttpServerRequest.getNullableParam(param: SupportedParam): String? = getParam(param.param)

fun HttpServerRequest.getAllParams(param: SupportedParam): List<String> = params().getAll(param.param)
