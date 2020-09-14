package wikicene.lucene.store

class StoreProvider(private val stores: Map<StoreType, Store> = emptyMap()) {

    companion object {
        fun fromStoreTypes() = StoreProvider(
            StoreType
                .values()
                .toList()
                .associateWith(StoreType::buildMMapStore)
        )
    }

    fun get(storeType: StoreType) = stores[storeType]
}
