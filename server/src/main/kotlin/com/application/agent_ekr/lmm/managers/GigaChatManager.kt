package com.application.agent_ekr.lmm.managers

import com.application.agent_ekr.lmm.models.ChatCompletionsRequest
import com.application.agent_ekr.lmm.models.GigaChatClientScope
import com.application.agent_ekr.lmm.models.GigaChatModel
import com.application.agent_ekr.lmm.models.Message
import com.application.agent_ekr.lmm.models.MessageRole
import com.application.agent_ekr.lmm.models.OauthRequest
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import org.slf4j.Logger
import java.util.UUID

class GigaChatManager(
    tokenGigaChat: String,
    val logger: Logger,
    scope: GigaChatClientScope = GigaChatClientScope.PERSONAL,
) : LlmManager {

    val jsonApp = Json {
        isLenient = true
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    val api = GigaChatApi(
        HttpClient(CIO) {
            install(ContentNegotiation) {
                json(jsonApp)
            }
            install(Logging) {
                level = LogLevel.INFO
            }
        },
        oauthBase = OauthRequest(
            rqUid = UUID.randomUUID().toString(),
            credentials = tokenGigaChat,
            scope = scope
        ),
        logger
    )

    override suspend fun getModels(): String {
        return jsonApp.encodeToString(api.getModels())
    }
    override suspend fun sendMessageStream(message: String): Flow<String> = api
        .processChatCompletionsStream(
            ChatCompletionsRequest(
                model=GigaChatModel.GIGA_CHAT_2,
                messages = listOf(Message(
                    role = MessageRole.USER,
                    content = message,
                ))
            )
        )
        .map {
            it.choices
                .mapNotNull { choice -> choice.delta.content }
                .joinToString()
        }
}