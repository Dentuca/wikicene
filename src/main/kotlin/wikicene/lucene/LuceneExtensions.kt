package wikicene.lucene

import org.apache.lucene.document.Document
import wikicene.model.Article
import java.net.URI
import java.time.Instant

fun Article.toLuceneDocument() = Document().apply {
    add(LuceneField.TITLE.instance.apply { setStringValue(title) } )
    add(LuceneField.SUMMARY.instance.apply { setStringValue(summary) } )
    add(LuceneField.DESCRIPTION.instance.apply { setStringValue(description) } )
    add(LuceneField.TIMESTAMP.instance.apply { setStringValue(timestamp.toString()) } )
    add(LuceneField.THUMBNAIL.instance.apply { setStringValue(thumbnail.toString()) } )
    add(LuceneField.PAGE.instance.apply { setStringValue(page.toString()) } )
}

fun Document.toArticle() = Article(
    title = get(LuceneField.TITLE.fieldName),
    summary = get(LuceneField.SUMMARY.fieldName),
    description = get(LuceneField.DESCRIPTION.fieldName),
    timestamp = Instant.parse(get(LuceneField.TIMESTAMP.fieldName)),
    thumbnail = URI(get(LuceneField.THUMBNAIL.fieldName)),
    page = URI(get(LuceneField.PAGE.fieldName))
)
