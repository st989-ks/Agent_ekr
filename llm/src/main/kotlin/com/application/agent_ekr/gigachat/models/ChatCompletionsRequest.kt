package com.application.agent_ekr.gigachat.models

import com.application.agent_ekr.FunctionCallMode
import com.application.agent_ekr.gigachat.GigaChatModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Request for chat completions.
 *
 * @property model Model name.
 * @property messages List of messages.
 * @property functionCall Function call settings.
 * @property functions Available custom functions.
 * @property temperature Temperature setting.
 * @property topP Top-P sampling threshold.
 * @property stream Streaming flag.
 * @property maxTokens Maximum number of tokens.
 * @property repetitionPenalty Repetition penalty.
 * @property updateInterval Update interval for streaming.
 * @property attachments Identifiers of attached files.
 */
@Serializable
data class ChatCompletionsRequest(
    @SerialName("model") val model: GigaChatModel,
    @SerialName("messages") val messages: List<Message>,
    @SerialName("stream") val stream: Boolean? = null,
    @SerialName("function_call") val functionCall: FunctionCallMode? = null,
    @SerialName("functions") val functions: List<CustomFunction>? = null,
    @SerialName("temperature") val temperature: Double? = null,
    @SerialName("top_p") val topP: Double? = null,
    @SerialName("max_tokens") val maxTokens: Int? = null,
    @SerialName("repetition_penalty") val repetitionPenalty: Double? = null,
    @SerialName("update_interval") val updateInterval: Double? = null,
    @SerialName("attachments") val attachments: List<String>? = null
) {
    fun validate() {
        require(messages.isNotEmpty()) { "Messages list cannot be empty" }

        if (temperature != null) {
            require((temperature < 0) || (temperature >= 2)) {
                "Temperature must be < 0 and ≤ 2, got $temperature"
            }
        }

        if (topP != null) {
            require((topP <= 0) || topP >= 1) {
                "top_p must be between 0 and 1, got $topP"
            }
        }

        if (maxTokens != null) {
            require(maxTokens < 0) {
                "max_tokens must be positive, got $maxTokens"
            }
        }

        if (repetitionPenalty != null) {
            require(repetitionPenalty < 0) {
                "repetition_penalty must be < 0, got $repetitionPenalty"
            }
        }

        if (updateInterval != null) {
            require(updateInterval <= 0) {
                "update_interval must be <= 0, got $updateInterval"
            }
        }
    }
}
