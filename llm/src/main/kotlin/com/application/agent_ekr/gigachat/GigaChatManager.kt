package com.application.agent_ekr.gigachat

import com.application.agent_ekr.LlmManager
import com.application.agent_ekr.MessageRole
import com.application.agent_ekr.gigachat.models.ChatCompletionsRequest
import com.application.agent_ekr.gigachat.models.GigaChatClientScope
import com.application.agent_ekr.gigachat.models.Message
import com.application.agent_ekr.gigachat.models.OauthRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import org.slf4j.Logger
import java.util.UUID

class GigaChatManager(
    clientSecret: String,
    val logger: Logger,
    scope: GigaChatClientScope = GigaChatClientScope.PERSONAL,
) : LlmManager {

    val api = GigaChatApi(
        oauthBase = OauthRequest(
            rqUid = UUID.randomUUID().toString(),
            credentials = clientSecret,
            scope = scope
        ),
        logger = logger
    )

    override suspend fun getModels(): List<String> {
        return api.getModels().data.map { it.id }
    }
    override suspend fun sendMessageStream(message: String): Flow<String> = api
        .processChatCompletionsStream(
            ChatCompletionsRequest(
                model = GigaChatModel.GIGA_CHAT_2,
                messages = listOf(
                    Message(
                        role = MessageRole.USER,
                        content = message,
                    )
                )
            )
        )
        .map {
            it.choices
                .mapNotNull { choice -> choice.delta.content }
                .joinToString()
        }
}