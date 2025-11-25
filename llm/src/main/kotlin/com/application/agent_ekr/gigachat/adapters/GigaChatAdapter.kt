package com.application.agent_ekr.gigachat.adapters

import com.application.agent_ekr.gigachat.GigaChatModel
import com.application.agent_ekr.gigachat.models.ChatCompletionsRequest
import com.application.agent_ekr.gigachat.models.CustomFunction
import com.application.agent_ekr.gigachat.models.Message
import com.application.agent_ekr.gigachat.models.MessageRole
import com.application.agent_ekr.models.common.ChatInput
import com.application.agent_ekr.models.common.ChatMessage
import com.application.agent_ekr.models.common.ToolDefinition

/**
 * Adapter for converting universal Input to GigaChat-specific Message
 */
object GigaChatAdapter {
    // TODO: Implement complete mapping from ChatInput to ChatCompletionsRequest
    fun ChatInput.toGigaChatRequest(): ChatCompletionsRequest {
        val gigaMessages = this.messages.map { chatMessage ->
            when (chatMessage) {
                is ChatMessage.System -> Message(
                    role = MessageRole.SYSTEM,
                    content = chatMessage.content
                )
                is ChatMessage.User -> Message(
                    role = MessageRole.USER,
                    content = chatMessage.content
                )
                is ChatMessage.Assistant -> Message(
                    role = MessageRole.ASSISTANT,
                    content = chatMessage.content
                    // TODO: Handle tool calls when GigaChat supports them
                )
                is ChatMessage.Tool -> Message(
                    role = MessageRole.FUNCTION,
                    content = chatMessage.content,
                    name = chatMessage.name
                )
            }
        }

        // Convert tools to GigaChat functions if available
        val gigaFunctions = this.tools?.map { it.toGigaChatFunction() }

        return ChatCompletionsRequest(
            model = this.model?.let { GigaChatModel.valueOf(it) } ?: GigaChatModel.GIGA_CHAT_2,
            messages = gigaMessages,
            stream = this.stream,
            functions = gigaFunctions,
            temperature = this.parameters?.temperature,
            topP = this.parameters?.topP,
            maxTokens = this.parameters?.maxTokens,
            repetitionPenalty = this.parameters?.frequencyPenalty
            // TODO: Map remaining parameters as needed
        )
    }

    // TODO: Implement tool definition to GigaChat function mapping
    fun ToolDefinition.toGigaChatFunction(): CustomFunction {
        return CustomFunction(
            name = this.name,
            description = this.description,
            parameters = this.parameters
        )
    }
}