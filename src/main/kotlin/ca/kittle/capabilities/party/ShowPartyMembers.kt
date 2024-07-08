package ca.kittle.capabilities.party

import ca.kittle.capabilities.party.components.listPartyMembers
import ca.kittle.capabilities.party.models.PartyId
import ca.kittle.plugins.StatusErrorMessage
import com.mongodb.client.MongoDatabase
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.html.body
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

context(MongoDatabase) fun Routing.showPartyMembers() {
    get("/party/{id}/members") {
        try {
            val id = call.parameters["id"]
            val partyMembers = id?.let {
                getPartyMembers(id).getOrThrow()
            }
            partyMembers?.apply {
                call.respondHtml {
                    body {
                        listPartyMembers(PartyId(id), partyMembers)
                    }
                }
            } ?: call.respond(HttpStatusCode.NotFound, StatusErrorMessage("No party found with the given id"))
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, StatusErrorMessage("Failed to retrieve party and party members"))
        }
    }
}
