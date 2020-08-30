package wikicene.lucene

import org.amshove.kluent.shouldBeEqualTo
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import wikicene.model.Article
import java.net.URI
import java.time.Instant

object LuceneExtensionsSpec : Spek({

    describe("on converting an article to a lucene document and back") {

        val originalArticle = Article(
            title = "Eevee",
            summary = "Eevee is a Pok\u00e9mon species in Nintendo and Game Freak's Pok\u00e9mon franchise.",
            description = "Pok\u00e9mon species",
            timestamp = Instant.now(),
            thumbnail = URI("https://upload.wikimedia.org/wikipedia/en/a/a9/Pok%C3%A9mon_Eevee_art.png"),
            page = URI("https://en.wikipedia.org/wiki/Eevee")
        )

        val luceneDocument = originalArticle.toLuceneDocument()
        val article = luceneDocument.toArticle()

        it("should have the original title") {
            article.title shouldBeEqualTo originalArticle.title
        }

        it("should have the original summary") {
            article.summary shouldBeEqualTo originalArticle.summary
        }

        it("should have the original description") {
            article.description shouldBeEqualTo originalArticle.description
        }

        it("should have the original timestamp") {
            article.timestamp shouldBeEqualTo originalArticle.timestamp
        }

        it("should have the original thumbnail") {
            article.thumbnail shouldBeEqualTo originalArticle.thumbnail
        }

        it("should have the original page") {
            article.page shouldBeEqualTo originalArticle.page
        }
    }
})
