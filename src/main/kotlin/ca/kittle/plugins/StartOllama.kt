package ca.kittle.plugins

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.config.tryGetString
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import mu.KotlinLogging

private val logger = KotlinLogging.logger {}

fun Application.startOllama(): LlmConnection {
    val llmConnection =
        LlmConnection(
            HttpClient {
                install(Logging)
                install(HttpTimeout) {
                    requestTimeoutMillis = 180_000
                    socketTimeoutMillis = 180_000
                }
                install(HttpRequestRetry) {
                    maxRetries = 5
                    retryIf { request, response ->
                        !response.status.isSuccess()
                    }
                    delayMillis { retry ->
                        retry * retry * 1500L
                    }
                    modifyRequest { request ->
                        request.headers.append("x-retry-count", retryCount.toString())
                    }
                }
                install(ContentNegotiation) {
                    json(
                        Json {
                            prettyPrint = true
                            isLenient = true
                            ignoreUnknownKeys = true
                        },
                    )
                }
            },
            environment.config.tryGetString("llm.url") ?: System.getenv("LLM_URL"),
        )
    launch {
        logger.info { "Starting LLM model" }
        val started = loadLlmModel(llmConnection)
        if (!started) {
            error("Failed to start LLM model")
        }
    }
    return llmConnection
}

suspend fun loadLlmModel(llmConnection: LlmConnection): Boolean {
    val resp =
        llmConnection.llmClient.post("${llmConnection.llmUrl}generate") {
            setBody("{ \"model\": \"${llmConnection.model}\" }")
        }
    return resp.status == HttpStatusCode.OK
}

data class LlmConnection(
    val llmClient: HttpClient,
    val llmUrl: String,
    val model: String = DEFAULT_MODEL,
)

private const val DEFAULT_MODEL = "llama3"

@Serializable
data class GenerateRequest(
    val prompt: String,
    val model: String = DEFAULT_MODEL,
    val stream: Boolean = false,
)

@Serializable
data class GenerateResponse(
    val model: String,
    val response: String,
)
