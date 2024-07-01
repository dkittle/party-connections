package ca.kittle.capabilities.party

import ca.kittle.capabilities.party.models.Party
import ca.kittle.db.models.PartyEntity
import ca.kittle.db.models.toDocument
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respondRedirect
import io.ktor.server.routing.Routing
import io.ktor.server.routing.post
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

/**
 * Route to create a new Party
 */
context(MongoDatabase) fun Routing.createParty() {
    post("/party") {
        val party: Party = call.receive()
        val id = createParty(party).getOrThrow()
        call.respondRedirect("/party/$id")
    }
}

context(MongoDatabase) suspend fun createParty(party: Party): Result<String> =
    withContext(Dispatchers.IO) {
        runCatching {
            val collection = this@MongoDatabase.getCollection(PartyEntity.COLLECTION_NAME)
            collection.find(Filters.eq(Party::name.name, party.name)).first()
                ?.let(PartyEntity::fromDocument)?.id?.value
                ?: run {
                    collection.insertOne(party.toDocument())
                    party.id.value
                }
        }.onFailure {
            logger.error("Error creating party", it)
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
