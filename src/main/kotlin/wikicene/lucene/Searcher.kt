package wikicene.lucene

import org.apache.lucene.analysis.Analyzer
import org.apache.lucene.index.DirectoryReader
import org.apache.lucene.search.IndexSearcher
import org.apache.lucene.search.Query
import org.apache.lucene.store.Directory
import wikicene.api.WikiceneParams
import wikicene.lucene.query.WikiceneQueryBuilder
import wikicene.model.Article

class Searcher(
    private val directory: Directory,
    private val analyzer: Analyzer
) {

    fun search(params: WikiceneParams): List<Article> {
        val query = WikiceneQueryBuilder(
            params = params,
            analyzer = analyzer
        ).build()

        return search(query)
    }

    fun search(query: Query, limit: Int = 5): List<Article> {
        val dirReader = DirectoryReader.open(directory)
        val indexSearcher = IndexSearcher(dirReader)

        val topResults = indexSearcher.search(query, limit)
        val articles = topResults.scoreDocs.map {
            indexSearcher.doc(it.doc).toArticle()
        }

        dirReader.close()

        return articles
    }
}
