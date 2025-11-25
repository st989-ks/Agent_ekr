package com.application.agent_ekr.models.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * Universal tool call representation from LLM responses.
 *
 * Usage: Captures function calls from assistant responses
 * Compatibility: Maps to provider-specific function call formats
 */
@Serializable
data class ToolCall(
    @SerialName("id") val id: String,
    @SerialName("type") val type: String = "function",
    @SerialName("function") val function: FunctionCall
)

@Serializable
data class FunctionCall(
    @SerialName("name") val name: String,
    @SerialName("arguments") val arguments: String
)
