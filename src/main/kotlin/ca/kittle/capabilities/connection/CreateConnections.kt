package ca.kittle.capabilities.connection

import ca.kittle.capabilities.party.components.populateParty
import ca.kittle.plugins.StatusErrorMessage
import com.mongodb.client.MongoDatabase
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.html.body

context(MongoDatabase) fun Routing.createConnections() {
    post("/party/{id}/connections") {
        try {
            val party = call.parameters["id"]?.let { id ->
//                getPartyAndMembers(id).getOrThrow()
            }
            party?.apply {
                call.respondHtml {
                    body {
//                        populateParty(party)
                    }
                }
            } ?: call.respond(HttpStatusCode.NotFound, StatusErrorMessage("No party found with the given id"))
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, StatusErrorMessage("Failed to retrieve party and party members"))
        }
    }
}



private val SUMMARY_PROMPT =
    """Please summarize this dungeons and dragons character backstory in bullet
    point form. Do not leave out any important points. Return your response as a 
    list of bullet points without any explanation.
    """.trimMargin().plus("\n")
