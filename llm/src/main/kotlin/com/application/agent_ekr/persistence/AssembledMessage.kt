package com.application.agent_ekr.persistence

import com.application.agent_ekr.models.common.ChatRole
import com.application.agent_ekr.models.common.ToolCall
import kotlinx.serialization.Contextual
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.modules.SerializersModule
import java.time.Instant
import java.util.UUID

/**
 * Complete message assembly for database persistence and conversation history.
 */
@Serializable
data class AssembledMessage(
    @SerialName("id") val id: String = UUID.randomUUID().toString(),
    @SerialName("conversation_id") val conversationId: String,
    @SerialName("role") val role: ChatRole,
    @SerialName("content") val content: String,
    @SerialName("tool_calls") val toolCalls: List<ToolCall>? = null,
    @SerialName("tool_results") val toolResults: List<ToolResult>? = null,

    @Serializable(with = InstantIso8601Serializer::class)
    @SerialName("timestamp") val timestamp: Instant = Instant.now(),
    @SerialName("metadata") val metadata: JsonObject = buildJsonObject {
        put("model", JsonNull)
        put("tokens", JsonNull)
    }
) {
    // TODO: Implement JSON serialization/deserialization helpers
    fun toJson(): String = json.encodeToString(serializer(), this)

    companion object {
        private val json = Json {
            serializersModule = SerializersModule {
                contextual(Instant::class, InstantIso8601Serializer)
            }
            ignoreUnknownKeys = true
        }

        fun fromJson(json: String): AssembledMessage =
            Json.decodeFromString(serializer(), json)
    }
}



/**
 * Tool execution result.
 * TODO: Expand with structured error handling instead of String.
 */
@Serializable
data class ToolResult(
    @SerialName("tool_call_id") val toolCallId: String,
    @SerialName("tool_name") val toolName: String,
    @SerialName("result") val result: String,
    @SerialName("success") val success: Boolean,
    @SerialName("error") val error: String? = null
)




// TODO: Replace with a more robust Instant serializer if needed
object InstantIso8601Serializer : KSerializer<Instant> {
    override val descriptor = PrimitiveSerialDescriptor("Instant", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Instant) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): Instant {
        return Instant.parse(decoder.decodeString())
    }
}