package com.application.agent_ekr.models.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import javax.swing.UIManager.put


/**
 * Universal tool definition for function calling across LLM providers.
 *
 * Usage:
 * ```kotlin
 * val weatherTool = ToolDefinition(
 *     name = "get_weather",
 *     description = "Get current weather for location",
 *     parameters = buildJsonObject {
 *         put("location", buildJsonObject {
 *             put("type", "string")
 *             put("description", "City name")
 *         })
 *     }
 * )
 * ```
 *
 * Compatibility:
 * - OpenAI: Direct mapping to tools array
 * - GigaChat: Maps to CustomFunction via adapter
 * - Anthropic: Requires tool schema conversion
 */
@Serializable
data class ToolDefinition(
    @SerialName("name") val name: String,
    @SerialName("description") val description: String = "ToolDefinition",
    @SerialName("parameters") val parameters: JsonObject
) {
    fun toJsonSchema(): JsonObject = buildJsonObject {
        put("type", "object")
        put("properties", parameters)
        put(
            "required", JsonArray(
                parameters.keys
                    .filter {
                        parameters[it]
                            ?.jsonObject
                            ?.get("required")
                            ?.jsonPrimitive
                            ?.boolean == true
                    }
                    .map { JsonPrimitive(it) }
            )
        )
    }
}
