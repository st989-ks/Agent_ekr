package com.application.agent_ekr.lmm.models

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
 */
@Serializable
class ChatCompletionsRequest(
    @SerialName("model") val model: String,
    @SerialName("messages") val messages: List<Message>,
    @SerialName("function_call") val functionCall: String?,
    @SerialName("functions") val functions: List<CustomFunction>?,
    @SerialName("temperature") val temperature: Double?,
    @SerialName("top_p") val topP: Double?,
    @SerialName("stream") val stream: Boolean?,
    @SerialName("max_tokens") val maxTokens: Int?,
    @SerialName("repetition_penalty") val repetitionPenalty: Double?,
    @SerialName("update_interval") val updateInterval: Double?
)
