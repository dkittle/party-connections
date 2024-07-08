package ca.kittle.capabilities.party

import ca.kittle.capabilities.party.components.populateParty
import ca.kittle.capabilities.party.models.Party
import ca.kittle.capabilities.party.models.PartyMember
import ca.kittle.capabilities.pc.models.PlayerCharacter
import ca.kittle.db.models.PartyEntity
import ca.kittle.db.models.PartyMemberEntity
import ca.kittle.db.models.PlayerCharacterEntity
import ca.kittle.plugins.StatusErrorMessage
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Aggregates
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Projections
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.html.body
import mu.KotlinLogging
import org.bson.Document
import org.bson.conversions.Bson

private val logger = KotlinLogging.logger {}

context(MongoDatabase) fun Routing.showParty() {
    get("/party/{id}") {
        try {
            val party = call.parameters["id"]?.let { id ->
                getPartyAndMembers(id).getOrThrow()
            }
            party?.apply {
                call.respondHtml {
                    body {
                        populateParty(party)
                    }
                }
            } ?: call.respond(HttpStatusCode.NotFound, StatusErrorMessage("No party found with the given id"))
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, StatusErrorMessage("Failed to retrieve party and party members"))
        }
    }
}

context(MongoDatabase) suspend fun getPartyAndMembers(id: String): Result<Party?> =
    runCatching {
        val party = getParty(id).getOrThrow()
        party?.let {
            val members = getPartyMembers(id).getOrThrow()
            party.copy(partyMembers = members)
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

context(MongoDatabase) suspend fun getPartyMembers(id: String): Result<List<PartyMember>> =
    withContext(Dispatchers.IO) {
        runCatching {
            val collection = this@MongoDatabase.getCollection(PlayerCharacterEntity.COLLECTION_NAME)
            val aggregates = listOf(
                matchStage(id),
                backstorySummaryLeftJoinStage,
                partyMemberProjection
            )
            collection.aggregate(aggregates).toList()
                .map { PartyMemberEntity.fromDocument(it) }
        }
    }.onFailure {
        logger.error("Error getting party members", it)
    }


fun matchStage(id: String) = Aggregates.match(Filters.eq(PlayerCharacter::partyId.name, id))

val backstorySummaryLeftJoinStage: Bson = Aggregates.lookup(
    "backstory-summaries",
    "_id",
    "pcId",
    "summaries"
)

val partyMemberProjection: Bson =
    Aggregates.project(
        Projections.fields(
            Projections.include(
                PlayerCharacter::name.name,
                PlayerCharacter::ancestry.name,
                PlayerCharacter::baseClass.name,
                PlayerCharacter::role.name
            ),
            Projections.computed(
                "backstorySummary",
                Document("\$arrayElemAt", listOf("\$summaries", 0))
            )
        )
    )

