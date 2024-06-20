package ca.kittle.plugins

import ca.kittle.party.createParty
import ca.kittle.party.listParties
import ca.kittle.party.showParty
import ca.kittle.pc.createPlayerCharacter
import ca.kittle.web.index
import com.mongodb.client.MongoDatabase
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respondText
import io.ktor.server.routing.routing

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
        // Character pages
        createPlayerCharacter()
    }
}
