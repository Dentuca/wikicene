package wikicene.lucene.store

import org.apache.lucene.store.ByteBuffersDirectory
import org.apache.lucene.store.Directory
import org.apache.lucene.store.MMapDirectory
import wikicene.ingestion.FileImporter
import wikicene.lucene.Indexer
import wikicene.lucene.Searcher
import wikicene.lucene.analysis.AnalyzerId
import wikicene.lucene.analysis.AnalyzerProvider
import wikicene.utils.timeThis
import java.io.File
import java.nio.file.Paths

private const val BASE_STORE_PATH = "lucene-store"

// wikipedia dump
private const val POPULATE_FROM_DUMP = true
private const val ARTICLES_DUMP_PATH = "random-articles.dump"

interface Store {
    val analyzerId: AnalyzerId
    val indexer: Indexer
    val searcher: Searcher
}

abstract class AbstractStore(
    override val analyzerId: AnalyzerId
) : Store {
    private val directory by lazy { createDirectory() }
    override val indexer by lazy { Indexer(directory, AnalyzerProvider.get(analyzerId)) }
    override val searcher by lazy { Searcher(directory, AnalyzerProvider.get(analyzerId)) }

    protected abstract fun createDirectory(): Directory
}

class MMapStore(
    analyzerId: AnalyzerId,
    subDir: String = analyzerId.toString()
) : AbstractStore(analyzerId) {

    private val path = "$BASE_STORE_PATH/$subDir"
    private val dirExistedOnCreation = File(path).exists()

    init {
        if (POPULATE_FROM_DUMP) {
            if (dirExistedOnCreation) {
                println("Found existing lucene store at '$path'")
            } else {
                println("No lucene store found at '$path'")
                timeThis("indexing all articles", FileImporter(ARTICLES_DUMP_PATH, indexer)::import)
            }
        }
    }

    override fun createDirectory(): Directory = MMapDirectory(Paths.get(path))
}

class InMemoryStore(analyzerId: AnalyzerId) : AbstractStore(analyzerId) {

    override fun createDirectory(): Directory = ByteBuffersDirectory()
}
