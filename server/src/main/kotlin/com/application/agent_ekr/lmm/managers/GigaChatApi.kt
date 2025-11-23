package com.application.agent_ekr.lmm.managers

import com.application.agent_ekr.Utils.runCatchingSuspend
import com.application.agent_ekr.lmm.models.ChatCompletionsRequest
import com.application.agent_ekr.lmm.models.ChatCompletionsResponse
import com.application.agent_ekr.lmm.models.ModelsResponse
import com.application.agent_ekr.lmm.models.OauthRequest
import com.application.agent_ekr.lmm.models.OauthResponse
import com.application.agent_ekr.lmm.models.StreamChunk
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.append
import io.ktor.http.headers
import io.ktor.http.parameters
import io.ktor.util.logging.error
import io.ktor.utils.io.jvm.javaio.toInputStream
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json
import org.slf4j.Logger
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.UUID

class GigaChatApi(
    val httpClient: HttpClient,
    val oauthBase: OauthRequest,
    val logger: Logger,
) {
    var currentOauth: OauthResponse? = null

    val isTokenExpired: Boolean
        get() = System.currentTimeMillis() + 3000L >= (currentOauth?.expiresAt ?: 0)

    suspend fun getToken(): OauthResponse {
        if (isTokenExpired) {
            runCatchingSuspend {
                val response = httpClient.post(AUTH_ENDPOINT) {
                    headers {
                        append(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded)
                        append(HttpHeaders.Accept, ContentType.Application.Json)
                        append("RqUID", oauthBase.rqUid)
                        append(HttpHeaders.Authorization, "Bearer ${oauthBase.credentials}")
                    }
                    setBody(
                        parameters {
                            append("scope", oauthBase.scope.scope)
                        }
                    )
                }
                currentOauth = response.body()
            }
                .onFailure {
                    logger.error(it)
                    throw it
                }
        }

        return currentOauth ?: throw GigaChatApiException.TokenExpired()
    }

    suspend fun getModels(): ModelsResponse {
        val token = getToken().accessToken
        return runCatchingSuspend {
            val response = httpClient.post(MODELS_ENDPOINT) {
                headers {
                    append(HttpHeaders.Accept, ContentType.Application.Json.toString())
                    append(HttpHeaders.Authorization, "Bearer $token")
                }
            }
            response.body<ModelsResponse>()
        }
            .getOrElse {
                logger.error(it)
                throw it
            }
    }

    // Placeholder for counting tokens
    suspend fun countTokens(input: List<String>, model: String): Int {
        TODO("Implement countTokens()")
    }

    // Placeholder for checking AI-generated content
    suspend fun checkAI(text: String, model: String): Boolean {
        TODO("Implement checkAI()")
    }

    // Placeholder for generating embeddings
    suspend fun generateEmbeddings(input: List<String>): List<DoubleArray> {
        TODO("Implement generateEmbeddings()")
    }

    // Placeholder for uploading files
    suspend fun uploadFile(file: ByteArray): String {
        TODO("Implement uploadFile()")
    }

    // Placeholder for deleting files
    suspend fun deleteFile(fileId: String): Boolean {
        TODO("Implement deleteFile()")
    }

    // Placeholder for validating custom functions
    suspend fun validateFunction(functionDescription: Map<String, Any>): Boolean {
        TODO("Implement validateFunction()")
    }

    /**
     * Processes chat completions by sending a request to the GigaChat API.
     *
     * This method sends a chat request to the GigaChat server and returns the response.
     * It retrieves an access token, sets up the necessary headers, and posts the request.
     *
     * @param chatRequest The chat request object containing the messages and other parameters.
     * @return The chat response object containing the generated messages and metadata.
     * @throws Exception If there is an issue with the network connection or the API response.
     */
    suspend fun processChatCompletions(
        chatRequest: ChatCompletionsRequest
    ): ChatCompletionsResponse {
        val token = getToken().accessToken
        return runCatchingSuspend {
            val response = httpClient.post(CHAT_ENDPOINT) {
                headers {
                    append(HttpHeaders.Authorization, "Bearer $token")
                    append(HttpHeaders.Accept, ContentType.Application.Json)

                    append("X-Client-ID", oauthBase.clientId)
                    append("X-Request-ID", UUID.randomUUID().toString())
                    append("X-Session-ID", oauthBase.sessionId)
                }
                setBody(chatRequest)
            }
            response.body<ChatCompletionsResponse>()
        }.getOrElse {
            logger.error(it)
            throw it
        }
    }

    /**
     * Потоковая обработка чата (Server-Sent Events)
     *
     * @param chatRequest запрос
     * @return Flow потока ответов
     */
    suspend fun processChatCompletionsStream(
        chatRequest: ChatCompletionsRequest,
    ): Flow<StreamChunk> = flow {
        chatRequest.validate()
        val modifiedRequest = chatRequest.copy(stream = true)
        val token = getToken().accessToken
        logger.debug("Starting streaming chat completion. Model: ${modifiedRequest.model}")
        runCatchingSuspend {
            val response = httpClient.post(CHAT_ENDPOINT) {
                headers {
                    append(HttpHeaders.Authorization, "Bearer $token")
                    append(HttpHeaders.Accept, ContentType.Text.EventStream)
                    append("X-Client-ID", oauthBase.clientId)
                    append("X-Request-ID", UUID.randomUUID().toString())
                    append("X-Session-ID", oauthBase.sessionId)
                }
                setBody(modifiedRequest)
            }

            response.bodyAsChannel().toInputStream().use { inputStream ->
                val reader = BufferedReader(InputStreamReader(inputStream))
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    val currentLine = line ?: continue
                    if (currentLine.startsWith("data: ")) {
                        val data = currentLine.substring(6)
                        if (data == "[DONE]") {
                            logger.debug("Stream completed with [DONE] marker")
                            break
                        }

                        runCatching {
                            val chunk: StreamChunk = Json.decodeFromString(data)
                            emit(chunk)
                        }.onFailure {
                            logger.warn("Failed to parse stream chunk", it)
                        }
                    }
                }
            }
        }.onFailure {
            logger.error(it)
            throw it
        }
    }

    // Placeholder for retrieving available models
    suspend fun getAvailableModels(): List<String> {
        TODO("Implement getAvailableModels()")
    }

    // Placeholder for retrieving file information
    suspend fun getFileInfo(fileId: String): Map<String, Any> {
        TODO("Implement getFileInfo()")
    }

    // Placeholder for retrieving file content
    suspend fun getFileContent(fileId: String): ByteArray {
        TODO("Implement getFileContent()")
    }

    // Placeholder for monitoring token consumption
    suspend fun monitorTokenConsumption(model: String): Int {
        TODO("Implement monitorTokenConsumption()")
    }

    // Placeholder for retrieving account balance
    suspend fun getAccountBalance(): Double {
        TODO("Implement getAccountBalance()")
    }

    // Placeholder for submitting batch jobs
    suspend fun submitBatchJob(batchData: ByteArray): String {
        TODO("Implement submitBatchJob()")
    }

    // Placeholder for retrieving batch job status
    suspend fun getBatchJobStatus(jobId: String): Map<String, Any> {
        TODO("Implement getBatchJobStatus()")
    }


    companion object {
        private const val BASE_URL = "https://ngw.devices.sberbank.ru:9443/"
        private const val AUTH_ENDPOINT = "$BASE_URL/api/v2/oauth"
        private const val CHAT_ENDPOINT = "$BASE_URL/api/v1/chat/completions"
        private const val MODELS_ENDPOINT = "$BASE_URL/api/v1/models"
    }
}
