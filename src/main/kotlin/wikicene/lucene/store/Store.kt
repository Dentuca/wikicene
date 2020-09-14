package wikicene.lucene.store

import org.apache.lucene.analysis.Analyzer
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.store.Directory
import org.apache.lucene.store.MMapDirectory
import wikicene.ingestion.FileImporter
import wikicene.lucene.Indexer
import wikicene.lucene.Searcher
import wikicene.utils.timeThis
import java.io.File
import java.nio.file.Paths

private const val BASE_STORE_PATH = "lucene-store"

// wikipedia dump
private const val POPULATE_FROM_DUMP = true
private const val ARTICLES_DUMP_PATH = "random-articles.dump"

interface Store {
    val luceneDirectory: Directory
    val indexer: Indexer
    val searcher: Searcher
}

class MMapStore(analyzer: Analyzer, subDir: String) : Store {

    private val path = "$BASE_STORE_PATH/$subDir"
    private val dirExistedOnCreation = File(path).exists()

    override val luceneDirectory = MMapDirectory(Paths.get(path))
    override val indexer = Indexer(luceneDirectory, analyzer)
    override val searcher = Searcher(luceneDirectory, analyzer)

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
}
