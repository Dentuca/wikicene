package wikicene.ingestion

import wikicene.lucene.Indexer
import wikicene.lucene.toLuceneDocument
import wikicene.model.Article
import java.io.File

class FileImporter(
    private val articlesFilepath: String,
    private val indexer: Indexer
) {

     fun import() {
         indexer.withWriter {
             File(articlesFilepath).forEachLine { line ->
                 addDocument(line.decodeJson<Article>().toLuceneDocument())
             }
         }
    }
}
