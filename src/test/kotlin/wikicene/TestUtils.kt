package wikicene

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
