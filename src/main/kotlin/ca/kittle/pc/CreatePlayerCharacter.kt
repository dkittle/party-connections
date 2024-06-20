package ca.kittle.pc

import ca.kittle.db.models.PlayerCharacterEntity
import ca.kittle.db.models.toDocument
import ca.kittle.pc.models.PlayerCharacter
import com.mongodb.client.MongoDatabase
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respondRedirect
import io.ktor.server.routing.Routing
import io.ktor.server.routing.post
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

context(MongoDatabase)
fun Routing.createPlayerCharacter() {
    post("/character") {
        val pc: PlayerCharacter = call.receive()
        createPlayerCharacter(pc)
        call.respondRedirect("/party/${pc.partyId}")
    }
}

context(MongoDatabase)
suspend fun createPlayerCharacter(pc: PlayerCharacter): Result<String> =
    withContext(Dispatchers.IO) {
        runCatching {
            val collection = this@MongoDatabase.getCollection(PlayerCharacterEntity.COLLECTION_NAME)
            collection.insertOne(pc.toDocument())
            pc.id
        }.onFailure {
            logger.error("Error creating player character", it)
        }
    }
