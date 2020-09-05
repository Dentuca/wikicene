package wikicene

import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.vertx.core.json.jackson.DatabindCodec
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.store.MMapDirectory
import wikicene.api.WikiceneVerticle
import wikicene.ingestion.FileImporter
import wikicene.lucene.Indexer
import wikicene.lucene.Searcher
import wikicene.utils.timeThis
import java.io.File
import java.nio.file.Paths

const val luceneStoragePath = "lucene-store"
const val articlesDumpPath = "random-articles.dump"

class App {

    fun deploy() {

        // check if the lucene directory exists. We need to do this here because the
        // directory is created automatically when we instantiate the mmap dir
        val luceneStoreExists = File(luceneStoragePath).exists()

        // setup the JSON mapper to work with kotlin classes
        DatabindCodec.mapper().registerKotlinModule()

        // setup lucene (move this out of here when doing multiple dirs/analyzers)
        val luceneDirectory = MMapDirectory(Paths.get(luceneStoragePath))
        val analyzer = StandardAnalyzer()
        val indexer = Indexer(luceneDirectory, analyzer)
        val searcher = Searcher(luceneDirectory, analyzer)

        // index data
        if (luceneStoreExists) {
            println("Found existing lucene store at '$luceneStoragePath'")
        } else {
            println("No lucene store found at '$luceneStoragePath'")
            timeThis("indexing all articles", FileImporter(articlesDumpPath, indexer)::import)
        }

        // start the API
        WikiceneVerticle(searcher).start()
    }
}

fun main() {
    App().deploy()
}
