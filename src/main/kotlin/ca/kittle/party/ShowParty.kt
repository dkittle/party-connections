package ca.kittle.party

import ca.kittle.db.models.PartyEntity
import ca.kittle.db.models.PlayerCharacterEntity
import ca.kittle.party.components.populateParty
import ca.kittle.party.models.Party
import ca.kittle.pc.models.PlayerCharacter
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters
import io.ktor.server.application.call
import io.ktor.server.html.respondHtml
import io.ktor.server.response.respondText
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.css.body
import kotlinx.html.body

context(MongoDatabase)
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

context(MongoDatabase)
suspend fun getParty(id: String): Party? =
    withContext(Dispatchers.IO) {
        val collection = this@MongoDatabase.getCollection(PartyEntity.COLLECTION_NAME)
        collection.find(Filters.eq("_id", id)).first()
            ?.let(PartyEntity::fromDocument)
    }

context(MongoDatabase)
suspend fun getPlayerCharacters(id: String): List<PlayerCharacter> =
    withContext(Dispatchers.IO) {
        val collection = this@MongoDatabase.getCollection(PlayerCharacterEntity.COLLECTION_NAME)
        collection.find(Filters.eq(PlayerCharacter::partyId.name, id)).toList()
            .map { PlayerCharacterEntity.fromDocument(it) }
    }
