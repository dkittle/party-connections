package ca.kittle.web

import ca.kittle.party.components.newPartyForm
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*
import kotlinx.html.*

fun Routing.index() {
    get("") {
        call.respondHtml {
            head {
                script {
                    src = "static/htmx.min.js"
                }
                script {
                    src = "static/json-enc.js"
                }
                meta {
                    name = "viewport"
                    content = "width=device-width, initial-scale=1.0"
                }
                script {
                    src = "static/tailwind.js"
                }
            }
            body {
                classes = BODY_STYLE
                div {
                    id = "page-content"
                    newPartyForm()
                }
            }
        }
    }
    static("/static") {
        resources("static")
    }
}
