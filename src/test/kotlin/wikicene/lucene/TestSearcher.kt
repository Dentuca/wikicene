package wikicene.lucene

import org.apache.lucene.analysis.Analyzer
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.store.ByteBuffersDirectory
import wikicene.api.WikiceneParams
import wikicene.model.Article

class TestSearcher(
    private val articles: List<Article>,
    private val analyzer: Analyzer = StandardAnalyzer() // we may want to split into writeAnalyzer and readAnalyzer
) {
    private val inMemoryDirectory = ByteBuffersDirectory()
    private val indexer = Indexer(inMemoryDirectory, analyzer)
    private val searcher = Searcher(inMemoryDirectory, analyzer)

    init {
        indexer.withWriter {
            articles.forEach { article ->
                addDocument(article.toLuceneDocument())
            }
        }
    }

    fun search(params: WikiceneParams) = searcher.search(params)
}
