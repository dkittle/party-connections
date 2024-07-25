package ca.kittle.plugins

import ca.kittle.capabilities.backstory.consumePlayerCharacterUpdates
import ca.kittle.capabilities.backstory.generateBackstorySummary
import ca.kittle.capabilities.backstory.storeBackstorySummary
import com.mongodb.client.MongoDatabase
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationStopping
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

context(MongoDatabase, LlmConnection)
fun Application.processPCUpdates() {
    val applicationScope = CoroutineScope(this.coroutineContext + SupervisorJob())
    environment.monitor.subscribe(ApplicationStopping) {
        applicationScope.cancel()
    }
    applicationScope.launch {
        logger.info { "Starting player character update flow" }
        consumePlayerCharacterUpdates().flowOn(Dispatchers.IO).collect { pc ->
            val backstory = pc.backstory.value
            logger.info { "Summarizing ${pc.name.value}'s backstory" }
            val summary = generateBackstorySummary(backstory)
            logger.info { "Storing ${pc.name.value}'s backstory summary" }
            storeBackstorySummary(summary, pc.id.value)
        }
    }
}
