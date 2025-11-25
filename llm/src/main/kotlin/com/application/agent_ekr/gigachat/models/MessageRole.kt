package com.application.agent_ekr.gigachat.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * GigaChat-specific message role definitions.
 * 
 * Note: This is API-specific and should not be used directly in universal interfaces.
 */
@Serializable
enum class MessageRole(val role: String) {
    @SerialName("system") SYSTEM("system"),
    @SerialName("user") USER("user"),
    @SerialName("assistant") ASSISTANT("assistant"),
    @SerialName("function") FUNCTION("function");
    
    companion object {
        fun fromString(role: String): MessageRole = entries.find { it.role == role }
            ?: throw IllegalArgumentException("Unknown role: $role")
    }
}
