package wikicene.model

import java.net.URI
import java.time.Instant

class Article(
    val title: String,
    val summary: String,
    val description: String,
    val timestamp: Instant,
    val thumbnail: URI,
    val page: URI
)
