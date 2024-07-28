package ca.kittle.capabilities.backstory

import ca.kittle.capabilities.backstory.models.BackstorySummary
import ca.kittle.capabilities.pc.models.PlayerCharacter
import ca.kittle.db.models.BackstorySummaryEntity
import ca.kittle.db.models.PlayerCharacterEntity
import ca.kittle.db.models.toDocument
import ca.kittle.plugins.GenerateRequest
import ca.kittle.plugins.GenerateResponse
import ca.kittle.plugins.LlmConnection
import ca.kittle.util.QUOTES
import ca.kittle.util.json
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Aggregates
import com.mongodb.client.model.Filters
import com.mongodb.client.model.changestream.FullDocument
import com.mongodb.client.model.changestream.OperationType
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import mu.KotlinLogging
import java.util.*

private val logger = KotlinLogging.logger {}

context(MongoDatabase, LlmConnection)
suspend fun processPlayerCharacterOperations(flow: Flow<PlayerCharacter>) {
    logger.info { "Starting player character update flow" }
    flow.flowOn(Dispatchers.IO).collect { pc ->
        val backstory = pc.backstory.value
        logger.info { "Summarizing ${pc.name.value}'s backstory" }
        val summaryResponse = generateBackstorySummary(backstory)
        logger.info { "Cleaning up ${pc.name.value}'s backstory summary" }
        val summary = cleanupBackstorySummary(summaryResponse)
        logger.info { "Storing ${pc.name.value}'s backstory summary" }
        storeBackstorySummary(summary, pc.id.value)
    }
}

/**
 * Cleans up the given backstory summary by removing the first two lines.
 *
 * @param summaryResponse the original backstory summary response
 * @return the cleaned-up backstory summary
 */
fun cleanupBackstorySummary(summaryResponse: String): String {
    val lines = summaryResponse.lines()
    return if (lines.size < 2) summaryResponse
    else lines.drop(2).joinToString("\n")
}

/**
 * Watch the PlayerCharacter collection for inserts, updates, and deletes
 * summarize a character's backstory on inserts and updates
 * TODO remove a character's summarized backstory if they are deleted
 */
context(MongoDatabase, LlmConnection)
suspend fun subscribeToPlayerCharacterChanges(): Flow<PlayerCharacter> =
    flow {
        val pipeline =
            listOf(
                Aggregates.match(
                    Filters.`in`(
                        "operationType",
                        listOf(
                            OperationType.DELETE,
                            OperationType.INSERT,
                            OperationType.REPLACE,
                            OperationType.UPDATE
                        )
                            .map { it.name.lowercase(Locale.getDefault()) }
                    ),
                ),
            )
        val collection = this@MongoDatabase.getCollection(PlayerCharacterEntity.COLLECTION_NAME)
        val changeStreamFlow =
            collection.watch(pipeline)
                .fullDocument(FullDocument.DEFAULT).asFlow().flowOn(Dispatchers.IO)
        logger.info { "Player character subscription flow started" }
        changeStreamFlow.collect { event ->
            if (event.operationType != OperationType.DELETE) {
                event.fullDocument?.let { doc ->
                    val operation =
                        "${event.operationType.name}${if (event.operationType.name.endsWith("e")) "d" else "ed"}"
                            .lowercase()
                    val pc = PlayerCharacterEntity.fromDocument(doc)
                    logger.info { "${pc.name.value} $operation" }
                    emit(pc)
                }
            }
        }
    }

context(LlmConnection)
suspend fun generateBackstorySummary(backstory: String): String {
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

private val SUMMARY_PROMPT =
    """Please summarize this dungeons and dragons character backstory in bullet
    point form. Do not leave out any important points. Return your response as a 
    list of bullet points without any explanation.
    """.trimMargin().plus("\n")

