package com.application.agent_ekr.models.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Universal message representation compatible with OpenAI, Anthropic, and GigaChat APIs.
 */
@Serializable
sealed class ChatMessage {
    abstract val content: String
    abstract val role: ChatRole

    @Serializable @SerialName("system")
    data class System(@SerialName("content") override val content: String) : ChatMessage() {
        override val role: ChatRole get() = ChatRole.SYSTEM
    }

    @Serializable @SerialName("user")
    data class User(@SerialName("content") override val content: String) : ChatMessage() {
        override val role: ChatRole get() = ChatRole.USER
    }

    @Serializable @SerialName("assistant")
    data class Assistant(
        @SerialName("content") override val content: String,
        @SerialName("tool_calls") val toolCalls: List<ToolCall>? = null
    ) : ChatMessage() {
        override val role: ChatRole get() = ChatRole.ASSISTANT
    }

    @Serializable @SerialName("tool")
    data class Tool(
        @SerialName("content") override val content: String,
        @SerialName("tool_call_id") val toolCallId: String,
        @SerialName("name") val name: String
    ) : ChatMessage() {
        override val role: ChatRole get() = ChatRole.TOOL
    }

    companion object {
        fun system(content: String) = System(content)
        fun user(content: String) = User(content)
        fun assistant(content: String, toolCalls: List<ToolCall>? = null) = Assistant(content, toolCalls)
        fun tool(content: String, toolCallId: String, name: String) = Tool(content, toolCallId, name)
    }
}