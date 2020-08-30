package wikicene.ingestion

import io.vertx.core.json.Json

inline fun <reified T> String.decodeJson(): T = Json.decodeValue(this, T::class.java)
