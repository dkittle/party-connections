package ca.kittle.web

import ca.kittle.capabilities.party.components.createPartyForm
import io.ktor.server.application.call
import io.ktor.server.html.respondHtml
import io.ktor.server.http.content.resources
import io.ktor.server.http.content.static
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import kotlinx.html.body
import kotlinx.html.button
import kotlinx.html.classes
import kotlinx.html.div
import kotlinx.html.head
import kotlinx.html.id
import kotlinx.html.meta
import kotlinx.html.script

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
                    createPartyForm()
                    button {
                        classes = BUTTON_STYLE
                        attributes["hx-get"] = "/parties"
                        attributes["hx-target"] = "#page-content"
                        type = kotlinx.html.ButtonType.button
                        +"List all parties"
                    }
                }
            }
        }
    }
    static("/static") {
        resources("static")
    }
}
