package ca.kittle.plugins

import ca.kittle.capabilities.backstory.subscribeToPlayerCharacterChanges
import ca.kittle.capabilities.backstory.generateBackstorySummary
import ca.kittle.capabilities.backstory.processPlayerCharacterOperations
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
        logger.info { "Stopping player character change flow" }
        applicationScope.cancel()
    }
    applicationScope.launch {
        logger.info { "Starting player character change flow" }
        processPlayerCharacterOperations(subscribeToPlayerCharacterChanges())
    }
}
