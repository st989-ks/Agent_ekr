package com.application.agent_ekr.gigachat.adapters

import com.application.agent_ekr.FunctionCallMode
import com.application.agent_ekr.gigachat.GigaChatModel
import com.application.agent_ekr.gigachat.models.ChatCompletionsRequest
import com.application.agent_ekr.gigachat.models.CustomFunction
import com.application.agent_ekr.gigachat.models.FunctionCall
import com.application.agent_ekr.gigachat.models.Message
import com.application.agent_ekr.gigachat.models.MessageRole
import com.application.agent_ekr.models.common.ChatInput
import com.application.agent_ekr.models.common.ChatMessage
import com.application.agent_ekr.models.common.ToolCall
import com.application.agent_ekr.models.common.ToolChoice
import com.application.agent_ekr.models.common.ToolDefinition
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject

/**
 * Adapter for converting universal Input to GigaChat-specific Message
 */
object GigaChatAdapter {

    /**
     * Converts universal ChatInput to GigaChat-specific ChatCompletionsRequest
     */
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

                is ChatMessage.Assistant -> {

                    val functionCall = chatMessage.toolCalls?.firstOrNull()?.let { toolCall ->
                        try {
                            val argumentsJson =
                                Json.parseToJsonElement(toolCall.function.arguments) as? JsonObject
                            FunctionCall(
                                name = toolCall.function.name,
                                arguments = argumentsJson
                            )
                        } catch (e: Exception) {
                            // If parsing fails, create an empty JsonObject
                            FunctionCall(
                                name = toolCall.function.name,
                                arguments = buildJsonObject { }
                            )
                        }
                    }
                    Message(
                        role = MessageRole.ASSISTANT,
                        content = chatMessage.content,
                        functionCall = functionCall
                    )
                }

                is ChatMessage.Tool -> Message(
                    role = MessageRole.FUNCTION,
                    content = chatMessage.content,
                    name = chatMessage.name
                )
            }
        }

        // Convert tools to GigaChat functions if available
        val gigaFunctions = this.tools?.map { it.toGigaChatFunction() }

        // Map tool choice to function call mode
        val functionCallMode = when (this.toolChoice) {
            ToolChoice.AUTO -> FunctionCallMode.AUTO
            ToolChoice.NONE -> FunctionCallMode.NONE
            ToolChoice.REQUIRED -> FunctionCallMode.AUTO // GigaChat doesn't have "required" mode
            null -> null
        }

        // Safely map model string to GigaChatModel
        val gigaChatModel = try {
            this.model?.let { modelName ->
                GigaChatModel.entries.find { it.model == modelName || it.name == modelName }
            } ?: GigaChatModel.GIGA_CHAT_2
        } catch (e: Exception) {
            GigaChatModel.GIGA_CHAT_2
        }

        return ChatCompletionsRequest(
            model = gigaChatModel,
            messages = gigaMessages,
            stream = this.stream,
            functionCall = functionCallMode,
            functions = gigaFunctions,
            temperature = this.parameters?.temperature,
            topP = this.parameters?.topP,
            maxTokens = this.parameters?.maxTokens,
            repetitionPenalty = this.parameters?.frequencyPenalty
            // Note: GigaChat uses repetition_penalty instead of frequency_penalty/presence_penalty
        )
    }

    /**
     * Converts universal ToolDefinition to GigaChat-specific CustomFunction
     */
    fun ToolDefinition.toGigaChatFunction(): CustomFunction {
        return CustomFunction(
            name = this.name,
            description = this.description,
            parameters = this.parameters,
            fewShotExamples = null, // TODO: Implement few-shot examples if needed
            returnParameters = null // TODO: Implement return parameters if needed
        )
    }

    /**
     * Converts GigaChat FunctionCall to universal ToolCall
     */
    fun FunctionCall.toUniversalToolCall(id: String = generateId()): ToolCall {
        val argumentsString = this.arguments?.toString() ?: "{}"
        return ToolCall(
            id = id,
            function = com.application.agent_ekr.models.common.FunctionCall(
                name = this.name,
                arguments = argumentsString
            )
        )
    }

    /**
     * Converts universal ToolCall to GigaChat FunctionCall
     */
    fun ToolCall.toGigaChatFunctionCall(): FunctionCall {
        val argumentsJson = try {
            Json.parseToJsonElement(this.function.arguments) as? JsonObject
        } catch (e: Exception) {
            buildJsonObject { }
        }
        return FunctionCall(
            name = this.function.name,
            arguments = argumentsJson
        )
    }

    private fun generateId(): String = "call_${System.currentTimeMillis()}"
}
