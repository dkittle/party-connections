package ca.kittle.plugins

import ca.kittle.backstory.BackstorySummary
import ca.kittle.db.models.BackstorySummaryEntity
import ca.kittle.db.models.PlayerCharacterEntity
import ca.kittle.db.models.toDocument
import ca.kittle.pc.models.PlayerCharacter
import ca.kittle.util.json
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Aggregates
import com.mongodb.client.model.Filters
import com.mongodb.client.model.changestream.FullDocument
import com.mongodb.client.model.changestream.OperationType
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

context(MongoDatabase, LlmConnection)
fun Application.processPCUpdates() {
    launch {
        logger.info { "Starting player character update flow" }
        consumePlayerCharacterUpdates().flowOn(Dispatchers.IO).collect { pc ->
            val backstory = pc.backstory
            logger.info { "Summarizing ${pc.name}'s backstory" }
            val summary = generateBackstorySummary(backstory)
            logger.info { "Storing ${pc.name}'s backstory" }
            storeBackstorySummary(summary, pc.id)
        }
    }
}

private const val QUOTES = "\"\"\""
private const val SUMMARY_PROMPT = """Please summarize this dungeons and dragons character backstory in bullet point form. Do not leave out any important points.\n"""

context(MongoDatabase, LlmConnection)
suspend fun consumePlayerCharacterUpdates(): Flow<PlayerCharacter> =
    flow {
        val pipeline =
            listOf(
                Aggregates.match(
                    Filters.`in`("operationType", mutableListOf("insert", "update", "delete")),
                ),
            )
        val collection = this@MongoDatabase.getCollection(PlayerCharacterEntity.COLLECTION_NAME)
        val changeStreamFlow =
            collection.watch(pipeline)
                .fullDocument(FullDocument.DEFAULT).asFlow().flowOn(Dispatchers.IO)
        logger.info { "Flow started" }
        changeStreamFlow.collect { event ->
            if (event.operationType != OperationType.DELETE) {
                event.fullDocument?.let { doc ->
                    val operation = "${event.operationType.name.lowercase()}${if (event.operationType.name.endsWith("e")) "d" else "ed"}"
                    val pc = PlayerCharacterEntity.fromDocument(doc)
                    logger.info { "${pc.name} $operation" }
                    emit(pc)
                }
            }
        }
    }

context(LlmConnection)
suspend fun generateBackstorySummary(
//    llmConnection: LlmConnection,
    backstory: String,
): String {
    val prompt = "$QUOTES $SUMMARY_PROMPT $backstory $QUOTES"
    val llmResponse =
        this@LlmConnection.llmClient.post("${this@LlmConnection.llmUrl}generate") {
            setBody(
                json.encodeToString(
                    GenerateRequest(
                        prompt,
                    ),
                ),
            )
        }
    return if (llmResponse.status == HttpStatusCode.OK) {
        val resp = llmResponse.body<GenerateResponse>()
        resp.response
    } else {
        logger.warn { "LLM failed to generate backstory summary" }
        backstory
    }
}

context(MongoDatabase)
suspend fun storeBackstorySummary(
    summary: String,
    pcId: String,
): Result<Boolean> =
    withContext(Dispatchers.IO) {
        runCatching {
            val collection = this@MongoDatabase.getCollection(BackstorySummaryEntity.COLLECTION_NAME)
            collection.insertOne(BackstorySummary(summary, pcId).toDocument()).wasAcknowledged()
        }.onFailure {
            logger.error(it) { "Failed to store backstory summary" }
        }
    }
