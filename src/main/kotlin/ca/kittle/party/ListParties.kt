package ca.kittle.party

import ca.kittle.db.models.PartyEntity
import ca.kittle.party.components.listOfParties
import ca.kittle.party.models.Party
import ca.kittle.plugins.dbConnection
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.css.body
import kotlinx.html.body

fun Routing.listParties() {
    get("/parties") {
        val parties = getParties()
        call.respondHtml {
            body {
                listOfParties(parties)
            }
        }
    }
}

suspend fun getParties(): List<Party> =
    withContext(Dispatchers.IO) {
        val collection = dbConnection.getCollection(PartyEntity.COLLECTION_NAME)
        collection.find().toList().map { PartyEntity.fromDocument(it) }
    }
