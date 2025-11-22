package com.application.agent_ekr.lmm.managers

import com.application.agent_ekr.lmm.models.GigaChatClientScope
import com.application.agent_ekr.lmm.models.OauthRequest
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import java.util.UUID

class GigaChatManager(
    tokenGigaChat: String,
    scope: GigaChatClientScope
) : LlmManager {

    val api = GigaChatApi(
        HttpClient(CIO) {
            install(ContentNegotiation) {
                json(Json {
                    isLenient = true
                    ignoreUnknownKeys = true
                    encodeDefaults = true
                })
            }
            install(Logging) {
                level = LogLevel.INFO
            }
        },
        oauthBase = OauthRequest(
            rqUid = UUID.randomUUID().toString(),
            credentials = tokenGigaChat,
            scope = scope
        )
    )

    override fun countTokens(input: List<String>, model: String): Int {
        TODO("Implement countTokens()")
    }

    override fun checkAI(text: String, model: String): Boolean {
        TODO("Implement checkAI()")
    }

    override fun generateEmbeddings(input: List<String>): List<DoubleArray> {
        TODO("Implement generateEmbeddings()")
    }

    override fun uploadFile(file: ByteArray): String {
        TODO("Implement uploadFile()")
    }

    override fun deleteFile(fileId: String): Boolean {
        TODO("Implement deleteFile()")
    }

    override fun validateFunction(functionDescription: Map<String, Any>): Boolean {
        TODO("Implement validateFunction()")
    }

    override fun processChatCompletions(messages: List<Map<String, Any>>, model: String): String {
        TODO("Implement processChatCompletions()")
    }

    override fun getAvailableModels(): List<String> {
        TODO("Implement getAvailableModels()")
    }

    override fun getFileInfo(fileId: String): Map<String, Any> {
        TODO("Implement getFileInfo()")
    }

    override fun getFileContent(fileId: String): ByteArray {
        TODO("Implement getFileContent()")
    }

    override fun monitorTokenConsumption(model: String): Int {
        TODO("Implement monitorTokenConsumption()")
    }

    override fun getAccountBalance(): Double {
        TODO("Implement getAccountBalance()")
    }

    override fun submitBatchJob(batchData: ByteArray): String {
        TODO("Implement submitBatchJob()")
    }

    override fun getBatchJobStatus(jobId: String): Map<String, Any> {
        TODO("Implement getBatchJobStatus()")
    }

    override fun getMessageStream(message: String) {
        TODO("Implement getMessageStream()")
    }
}