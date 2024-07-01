package ca.kittle.capabilities.party

import ca.kittle.capabilities.party.components.populateParty
import ca.kittle.capabilities.party.models.Party
import ca.kittle.capabilities.pc.models.PlayerCharacter
import ca.kittle.db.models.PartyEntity
import ca.kittle.db.models.PlayerCharacterEntity
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters
import io.ktor.server.application.call
import io.ktor.server.html.respondHtml
import io.ktor.server.response.respondText
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.html.body
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

context(MongoDatabase) fun Routing.showParty() {
    get("/party/{id}") {
        val party =
            call.parameters["id"]?.let { id ->
                getParty(id).getOrThrow()
            }
        party?.apply {
            val pcs = getPlayerCharacters(id.value).getOrThrow()
            call.respondHtml {
                body {
                    populateParty(party.copy(playerCharacters = pcs))
                }
            }
        }
        call.respondText("No id value")
    }
}

context(MongoDatabase) suspend fun getParty(id: String): Result<Party?> =
    withContext(Dispatchers.IO) {
        runCatching {
            val collection = this@MongoDatabase.getCollection(PartyEntity.COLLECTION_NAME)
            collection.find(Filters.eq("_id", id)).first()
                ?.let(PartyEntity::fromDocument)
        }
    }.onFailure {
        logger.error("Error getting party", it)
    }

context(MongoDatabase) suspend fun getPlayerCharacters(id: String): Result<List<PlayerCharacter>> =
    withContext(Dispatchers.IO) {
        runCatching {
            val collection = this@MongoDatabase.getCollection(PlayerCharacterEntity.COLLECTION_NAME)
            collection.find(Filters.eq(PlayerCharacter::partyId.name, id)).toList()
                .map { PlayerCharacterEntity.fromDocument(it) }
        }
    }.onFailure {
        logger.error("Error getting party's player characters", it)
    }
