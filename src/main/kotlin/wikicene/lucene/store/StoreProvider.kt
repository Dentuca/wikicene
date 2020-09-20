package wikicene.lucene.store

import wikicene.lucene.analysis.AnalyzerId
import wikicene.lucene.analysis.AnalyzerProvider

class StoreProvider(stores: List<Store>) {

    private val stores = stores.associateBy { it.analyzerId }

    companion object {
        internal fun createAllSupportedMMapStores() = StoreProvider(
            stores = AnalyzerProvider.all().map { MMapStore(analyzerId = it) }
        )
    }

    fun get(analyzerId: AnalyzerId) = stores[analyzerId]
}
