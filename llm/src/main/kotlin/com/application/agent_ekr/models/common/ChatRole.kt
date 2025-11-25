package com.application.agent_ekr.models.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * Universal role definitions compatible across LLM providers.
 *
 * Usage: Used in ChatMessage to specify message origin
 * Compatibility: Maps to provider-specific role strings
 */
@Serializable
enum class ChatRole(val value: String) {
    @SerialName("system") SYSTEM("system"),
    @SerialName("user") USER("user"),
    @SerialName("assistant") ASSISTANT("assistant"),
    @SerialName("tool") TOOL("tool"); // OpenAI-style tool role

    companion object {
        fun fromString(role: String): ChatRole = entries.find { it.value == role }
            ?: throw IllegalArgumentException("Unknown role: $role")
    }
}
