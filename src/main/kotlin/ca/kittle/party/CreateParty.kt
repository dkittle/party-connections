package ca.kittle.party

import ca.kittle.party.models.Party
import ca.kittle.party.models.db.PartyEntity
import ca.kittle.party.models.db.toDocument
import ca.kittle.plugins.dbConnection
import com.mongodb.client.model.Filters
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// private val logger = KotlinLogging.logger {}

fun Routing.createParty() {
    post("/party") {
        val party: Party = call.receive()
        val id = createParty(party)
        call.respondRedirect("/party/$id")
    }
}

suspend fun createParty(party: Party): String =
    withContext(Dispatchers.IO) {
        val collection = dbConnection.getCollection(PartyEntity.COLLECTION_NAME)
        collection.find(Filters.eq(Party::name.name, party.name)).first()
            ?.let(PartyEntity::fromDocument)?.id
            ?: run {
                collection.insertOne(party.toDocument())
                party.id
            }
    }
