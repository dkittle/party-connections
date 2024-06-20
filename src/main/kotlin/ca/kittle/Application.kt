package ca.kittle

import ca.kittle.plugins.configureDatabases
import ca.kittle.plugins.configureHTTP
import ca.kittle.plugins.configureRouting
import ca.kittle.plugins.configureSerialization
import ca.kittle.plugins.processPCUpdates
import ca.kittle.plugins.startOllama
import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import kotlinx.coroutines.launch
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureHTTP()
    configureSerialization()
    logger.info { "Configuring database" }
    val mongoConnection = configureDatabases()
    logger.info { "Start ollama" }
    val llmConnection = startOllama()
    logger.info { "Watch for PC updates" }
    launch {
        with(mongoConnection) {
            with(llmConnection) {
                processPCUpdates()
            }
        }
    }
    logger.info { "Configuring routing" }
    with(mongoConnection) {
        configureRouting()
    }
    logger.info { "Application started" }
}
