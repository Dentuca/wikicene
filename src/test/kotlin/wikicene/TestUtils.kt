package wikicene

import wikicene.api.IndexationAnalyzerParams
import wikicene.api.QueryParams
import wikicene.api.WikiceneParams
import wikicene.lucene.Indexer
import wikicene.lucene.LuceneField
import wikicene.lucene.analysis.SupportedAnalyzerType
import wikicene.lucene.analysis.SupportedTokenFilter
import wikicene.lucene.analysis.SupportedTokenizer
import wikicene.lucene.query.QueryType
import wikicene.lucene.toLuceneDocument
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
    analyzerType: SupportedAnalyzerType = SupportedAnalyzerType.STANDARD,
    tokenizer: SupportedTokenizer? = null,
    tokenFilters: List<SupportedTokenFilter> = emptyList(),
    queryType: QueryType = QueryType.PARSED,
    queryFields: Set<LuceneField> = setOf(LuceneField.TITLE),
    maxEdits: Int? = null
): WikiceneParams {

    val indexationAnalyzerParams = IndexationAnalyzerParams(
        analyzerType = analyzerType,
        tokenizer = tokenizer,
        tokenFilters = tokenFilters
    )

    val queryParams = QueryParams(
        term = term,
        queryType = queryType,
        queryFields = queryFields,
        maxEdits = maxEdits
    )

    return WikiceneParams(
        indexationAnalyzerParams = indexationAnalyzerParams,
        queryParams = queryParams
    )
}

fun buildQueryParams(
    term: String,
    queryType: QueryType = QueryType.PARSED,
    queryFields: Set<LuceneField> = setOf(LuceneField.TITLE),
    maxEdits: Int? = null
) = QueryParams(
    term = term,
    queryType = queryType,
    queryFields = queryFields,
    maxEdits = maxEdits
)

fun Indexer.addArticles(vararg articles: Article) = withWriter {
    addDocuments(articles.map(Article::toLuceneDocument))
}
