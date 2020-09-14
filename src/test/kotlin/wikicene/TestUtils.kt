package wikicene

import wikicene.api.WikiceneParams
import wikicene.lucene.LuceneField
import wikicene.lucene.query.QueryType
import wikicene.lucene.store.StoreType
import wikicene.model.Article
import java.net.URI
import java.time.Instant

fun buildArticle(
    title: String,
    summary: String = "lorem ipsum",
    description: String = "",
    timestamp: Instant = Instant.now(),
    thumbnail: URI = URI("https://wikicene.com/image.png"),
    page: URI = URI("https://wikicene.com/article")
) = Article(
    title = title,
    summary = summary,
    description = description,
    timestamp = timestamp,
    thumbnail = thumbnail,
    page = page
)

fun buildParams(
    term: String,
    storeType: StoreType = StoreType.STANDARD,
    queryType: QueryType,
    queryFields: Set<LuceneField> = setOf(LuceneField.TITLE),
    maxEdits: Int? = null
) = WikiceneParams(
    term = term,
    storeType = storeType,
    queryType = queryType,
    queryFields = queryFields,
    maxEdits = maxEdits
)
