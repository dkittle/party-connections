package ca.kittle.plugins

import ca.kittle.party.createParty
import ca.kittle.party.showParty
import ca.kittle.pc.createPlayerCharacter
import ca.kittle.web.index
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

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
        // Character pages
        createPlayerCharacter()
    }
}
