package ca.kittle.capabilities.party

import ca.kittle.capabilities.party.components.listOfParties
import ca.kittle.capabilities.party.models.Party
import ca.kittle.db.models.PartyEntity
import com.mongodb.client.MongoDatabase
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.routing.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.html.body
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

context(MongoDatabase)
fun Routing.listParties() {
    get("/parties") {
        val parties = getParties().getOrThrow()
        call.respondHtml {
            body {
                listOfParties(parties)
            }
        }
    }
}

context(MongoDatabase)
suspend fun getParties(): Result<List<Party>> =
    withContext(Dispatchers.IO) {
        runCatching {
            val collection = this@MongoDatabase.getCollection(PartyEntity.COLLECTION_NAME)
            collection.find().toList().map { PartyEntity.fromDocument(it) }
        }.onFailure {
            logger.error("Error getting list of all parties", it)
        }
    }
