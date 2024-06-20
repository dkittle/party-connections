package ca.kittle.party

import ca.kittle.db.models.PartyEntity
import ca.kittle.party.components.listOfParties
import ca.kittle.party.models.Party
import com.mongodb.client.MongoDatabase
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.routing.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.html.body

context(MongoDatabase)
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

context(MongoDatabase)
suspend fun getParties(): List<Party> =
    withContext(Dispatchers.IO) {
        val collection = this@MongoDatabase.getCollection(PartyEntity.COLLECTION_NAME)
        collection.find().toList().map { PartyEntity.fromDocument(it) }
    }
