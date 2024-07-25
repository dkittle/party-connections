package ca.kittle.capabilities.party

import ca.kittle.capabilities.party.components.listOfParties
import ca.kittle.capabilities.party.models.Party
import ca.kittle.db.models.PartyEntity
import ca.kittle.plugins.StatusErrorMessage
import ca.kittle.util.executeUseCase
import ca.kittle.util.useCaseResponse
import com.mongodb.client.MongoDatabase
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.css.body
import kotlinx.html.FlowContent
import kotlinx.html.body
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

context(MongoDatabase) fun Routing.listParties() {
    get("/parties") {
        try {
            val parties = getParties().getOrThrow()
            call.respondHtml {
                body {
                    listOfParties(parties)
                }
            }
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, StatusErrorMessage("Failed to load parties"))
        }
    }
}


context(MongoDatabase) suspend fun getParties(): Result<List<Party>> =
    withContext(Dispatchers.IO) {
        runCatching {
            val collection = this@MongoDatabase.getCollection(PartyEntity.COLLECTION_NAME)
            collection.find().toList().map { PartyEntity.fromDocument(it) }
        }.onFailure {
            logger.error("Error getting list of all parties", it)
        }
    }
