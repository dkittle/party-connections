package ca.kittle.capabilities.party

import ca.kittle.capabilities.party.models.Party
import ca.kittle.db.models.PartyEntity
import ca.kittle.db.models.toDocument
import ca.kittle.plugins.StatusErrorMessage
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

/**
 * Route to create a new Party
 */
context(MongoDatabase) fun Routing.createParty() {
    post("/party") {
        try {
            val id = CreatePartyUseCase(party = call.receive()).getOrThrow()
            call.respondRedirect("/party/$id")
        } catch (e: IllegalArgumentException) {
            call.respond(HttpStatusCode.BadRequest, StatusErrorMessage(e.message.orEmpty()))
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, StatusErrorMessage("Failed to create party"))
        }
    }
}

/**
 * Create Party Use-Case
 * Validation:
 * Party name cannot be blank
 * Action:
 * Takes a party name and either loads an existing party of that name
 * or creates the party with that name and returns it.
 * Response:
 * The id of the existing or new party
 */
object CreatePartyUseCase {
    context(MongoDatabase) suspend operator fun invoke(party: Party): Result<String> {
        // Validate
        require(party.name.value.isNotBlank()) { "Party name cannot be blank" }
        // Action
        val newParty = createParty(party).getOrThrow()
        // Build response
        return Result.success(newParty.id.value)
    }
}

context(MongoDatabase) suspend fun createParty(party: Party): Result<Party> =
    withContext(Dispatchers.IO) {
        runCatching {
            val collection = this@MongoDatabase.getCollection(PartyEntity.COLLECTION_NAME)
            val existingParty = collection.find(Filters.eq(Party::name.name, party.name.value)).first()
            if (existingParty != null) {
                PartyEntity.fromDocument(existingParty)
            } else {
                collection.insertOne(party.toDocument())
                party
            }
        }.onFailure {
            logger.error("Error creating party: ${it.message}", it)
        }
    }

// suspend fun update(party: Party): Document? =
//    withContext(Dispatchers.IO) {
//        val collection = dbConnection.getCollection(PartyEntity.COLLECTION_NAME)
//        collection.findOneAndReplace(Filters.eq("_id", party.id), party.toDocument())
//    }
//
// suspend fun delete(id: String): Document? =
//    withContext(Dispatchers.IO) {
//        val collection = dbConnection.getCollection(PartyEntity.COLLECTION_NAME)
//        collection.findOneAndDelete(Filters.eq("_id", id))
//    }
