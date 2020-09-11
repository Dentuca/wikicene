package wikicene.api

import wikicene.model.Article

class WikiceneResponse(
    val articles: List<Article>,
    val time: Int // duration of the query in milliseconds
)
