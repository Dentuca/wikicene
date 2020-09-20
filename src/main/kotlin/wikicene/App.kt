package wikicene

import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.vertx.core.json.jackson.DatabindCodec
import wikicene.api.WikiceneVerticle
import wikicene.lucene.store.StoreProvider

class App {

    fun deploy() {

        // setup the JSON mapper to work with kotlin classes
        DatabindCodec.mapper().registerKotlinModule()

        // build the stores/indexes
        val storeProvider = StoreProvider.createAllSupportedMMapStores()

        // start the API
        WikiceneVerticle(storeProvider).start()
    }
}

fun main() {
    App().deploy()
}
