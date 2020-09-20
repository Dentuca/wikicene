package wikicene.api

import io.vertx.core.http.HttpServerRequest
import wikicene.exception.MissingParamException
import wikicene.utils.kebabToScreamingSnake

enum class SupportedParam(val param: String) {
    TERM("term"),
    ANALYZER_TYPE("analyzerType"),
    TOKENIZER("tokenizer"),
    TOKEN_FILTER("tokenFilter"),
    QUERY_TYPE("queryType"),
    QUERY_FIELD("queryField"),
    MAX_EDITS("maxEdits")
}

fun HttpServerRequest.getParam(param: SupportedParam): String =
    getNullableParam(param) ?: throw MissingParamException(param)

fun HttpServerRequest.getNullableParam(param: SupportedParam): String? = getParam(param.param)

fun HttpServerRequest.getAllParams(param: SupportedParam): List<String> = params().getAll(param.param)

inline fun <reified T: Enum<T>> HttpServerRequest.getEnumParam(param: SupportedParam): T =
    getNullableEnumParam<T>(param) ?: throw MissingParamException(param)

inline fun <reified T: Enum<T>> HttpServerRequest.getNullableEnumParam(param: SupportedParam): T? =
    getParam(param.param)?.let {
        enumValueOf<T>(it.kebabToScreamingSnake())
    }

inline fun <reified T: Enum<T>> HttpServerRequest.getAllEnumParams(param: SupportedParam): List<T> =
    getAllParams(param).map {
        enumValueOf<T>(it.kebabToScreamingSnake())
    }
