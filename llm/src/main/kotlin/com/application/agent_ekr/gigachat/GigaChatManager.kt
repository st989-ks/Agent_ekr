package com.application.agent_ekr.gigachat

import com.application.agent_ekr.LlmManager
import com.application.agent_ekr.gigachat.adapters.GigaChatAdapter
import com.application.agent_ekr.gigachat.models.GigaChatClientScope
import com.application.agent_ekr.gigachat.models.OauthRequest
import com.application.agent_ekr.models.common.ChatInput
import com.application.agent_ekr.models.common.ChatMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.slf4j.Logger
import java.util.UUID

class GigaChatManager(
    clientSecret: String,
    val logger: Logger,
    scope: GigaChatClientScope = GigaChatClientScope.PERSONAL,
) : LlmManager {

    private val api = GigaChatApi(
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

    /**
     * Backward compatible method - single string input
     */
    override suspend fun sendMessageStream(message: String): Flow<String> =
        sendMessageStream(ChatInput(messages = listOf(ChatMessage.User(message))))

    /**
     * Enhanced method with universal ChatInput support
     */
    override suspend fun sendMessageStream(input: ChatInput): Flow<String> {
        val gigaChatRequest = GigaChatAdapter.run {
            input.toGigaChatRequest()
        }

        logger.debug("Sending enhanced chat request with ${input.messages.size} messages")
        if (input.tools?.isNotEmpty() == true) {
            logger.debug("Including ${input.tools.size} tools in request")
        }

        return api.processChatCompletionsStream(gigaChatRequest)
            .map { chunk ->
                // Extract content from stream chunks
                val content = chunk.choices
                    .mapNotNull { choice -> choice.delta.content }
                    .joinToString("")

                // TODO: Handle function calls from stream if GigaChat supports streaming function calls
                // For now, we only return content
                content
            }
    }
}
