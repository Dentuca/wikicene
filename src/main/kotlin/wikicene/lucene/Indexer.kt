package wikicene.lucene

import org.apache.lucene.analysis.Analyzer
import org.apache.lucene.index.IndexWriter
import org.apache.lucene.index.IndexWriterConfig
import org.apache.lucene.store.Directory

class Indexer(
    private val directory: Directory,
    private val analyzer: Analyzer
) {

    private val indexConfig: IndexWriterConfig
        get() = IndexWriterConfig(analyzer).apply {
            openMode = IndexWriterConfig.OpenMode.CREATE
        }

    fun withWriter(block: IndexWriter.() -> Unit) {
        val writer = IndexWriter(directory, indexConfig)
        writer.block()
        writer.close()
    }
}
