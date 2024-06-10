package ca.kittle.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.html.*

fun Application.configureRouting() {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respondText(text = "500: $cause", status = HttpStatusCode.InternalServerError)
        }
    }
    fun Routing.index() {
        get {
            call.respondHtml {
                head {
                    script {
//                        src = "https://unpkg.com/htmx.org@1.9.10"
                        src = "static/assets/htmx.min.js"
                    }
                    meta {
                        name = "viewport"
                        content = "width=device-width, initial-scale=1.0"
                    }
                    link {
                        href = "static/assets/output.css"
                        rel = "stylesheet"
                    }
                }
                body {
                    classes = setOf("md:container", "md:mx-auto", "px-4")

                    button {
//                    attributes["hx-get"] = "https://jsonplaceholder.typicode.com/users"
                        attributes["hx-post"] = "/party"
//                    attributes["hx-trigger"] = "mouseover"
                        attributes["hx-swap"] = "outerHTML"
                        classes = setOf("bg-blue-500", "text-white", "px-3", "py-2", "rounded-lg")
                        +"Fetch Users"
                    }
                }
            }
        }
        static("/static") {
            resources("static")
        }
    }
}
