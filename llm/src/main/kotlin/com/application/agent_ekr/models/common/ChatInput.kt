package com.application.agent_ekr.models.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Universal input representation that abstracts provider-specific message formats.
 */
@Serializable
data class ChatInput(
    @SerialName("messages") val messages: List<ChatMessage>,
    @SerialName("tools") val tools: List<ToolDefinition>? = null,
    @SerialName("tool_choice") val toolChoice: ToolChoice? = null,
    @SerialName("parameters") val parameters: GenerationParameters? = null,
    @SerialName("model") val model: String? = null,
    @SerialName("stream") val stream: Boolean = true
) {
    companion object {
        // TODO: Implement convenience method for single message input
        fun fromSingleMessage(message: String): ChatInput = ChatInput(
            messages = listOf(ChatMessage.user(message))
        )
    }
}

@Serializable
enum class ToolChoice {
    @SerialName("auto") AUTO,
    @SerialName("none") NONE,
    @SerialName("required") REQUIRED
}