package wikicene.ingestion

import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.vertx.core.json.jackson.DatabindCodec
import org.amshove.kluent.shouldBeEqualTo
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import wikicene.model.Article

object JsonUtilsSpec : Spek({

    describe("on importing a well formed article JSON") {

        // setup the JSON mapper to work with kotlin classes
        DatabindCodec.mapper().registerKotlinModule()

        val title = "Eevee"
        val summary = "Eevee is a Pok\u00e9mon species in Nintendo and Game Freak's Pok\u00e9mon franchise."
        val description = "Pok\u00e9mon species"
        val timestamp = "2020-08-18T19:07:08Z"
        val thumbnail = "https://upload.wikimedia.org/wikipedia/en/a/a9/Pok%C3%A9mon_Eevee_art.png"
        val page = "https://en.wikipedia.org/wiki/Eevee"

        val articleAsJson = """
            |{
            |  "title": "$title",
            |  "summary": "$summary",
            |  "description": "$description",
            |  "timestamp": "$timestamp",
            |  "thumbnail": "$thumbnail",
            |  "page": "$page"
            |}
        """.trimMargin()

        val article = articleAsJson.decodeJson<Article>()

        it("should have the correct title") {
            article.title shouldBeEqualTo title
        }

        it("should have the correct summary (without unicode artifacts)") {
            article.summary shouldBeEqualTo summary.replace("Pok\u00e9mon", "Pokémon")
        }

        it("should have the correct description (without unicode artifacts)") {
            article.description shouldBeEqualTo description.replace("Pok\u00e9mon", "Pokémon")
        }

        it("should have the correct timestamp") {
            article.timestamp.toString() shouldBeEqualTo timestamp
        }

        it("should have the correct thumbnail URI") {
            article.thumbnail.toString() shouldBeEqualTo thumbnail
        }

        it("should have the correct page URI") {
            article.page.toString() shouldBeEqualTo page
        }
    }
})
