package ca.kittle.party

import ca.kittle.party.components.populateParty
import ca.kittle.party.models.Party
import ca.kittle.party.models.PlayerCharacter
import ca.kittle.party.models.db.PartyEntity
import ca.kittle.party.models.db.PlayerCharacterEntity
import ca.kittle.plugins.dbConnection
import com.mongodb.client.model.Filters
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.css.body
import kotlinx.html.body

fun Routing.showParty() {
    get("/party/{id}") {
        val party =
            call.parameters["id"]?.let { id ->
                getParty(id)
            }
        party?.apply {
            val pcs = getPlayerCharacters(id)
            call.respondHtml {
                body {
                    populateParty(party, pcs)
                }
            }
        }
        call.respondText("No id value")
    }
}

suspend fun getParty(id: String): Party? =
    withContext(Dispatchers.IO) {
        val collection = dbConnection.getCollection(PartyEntity.COLLECTION_NAME)
        collection.find(Filters.eq("_id", id)).first()
            ?.let(PartyEntity::fromDocument)
    }

suspend fun getPlayerCharacters(id: String): List<PlayerCharacter> =
    withContext(Dispatchers.IO) {
        val collection = dbConnection.getCollection(PlayerCharacterEntity.COLLECTION_NAME)
        collection.find(Filters.eq(PlayerCharacter::partyId.name, id)).toList()
            .map { PlayerCharacterEntity.fromDocument(it) }
    }
