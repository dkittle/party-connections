package ca.kittle.plugins

import ca.kittle.capabilities.party.createParty
import ca.kittle.capabilities.party.listParties
import ca.kittle.capabilities.party.showParty
import ca.kittle.capabilities.party.showPartyMembers
import ca.kittle.capabilities.pc.createPlayerCharacter
import ca.kittle.web.index
import com.mongodb.client.MongoDatabase
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


object StatusErrorMessage {
    operator fun invoke(message: String) = "<div>$message</div>"
}

context(MongoDatabase)
fun Application.configureRouting() {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respondText(text = "500: $cause", status = HttpStatusCode.InternalServerError)
        }
    }
    routing {
        // main pages
        index()
        // Party pages
        createParty()
        showParty()
        listParties()
        showPartyMembers()
        // Character pages
        createPlayerCharacter()
    }
}
