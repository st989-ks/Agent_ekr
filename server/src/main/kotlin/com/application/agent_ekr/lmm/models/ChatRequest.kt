package com.application.agent_ekr.lmm.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Request body for generating chat completions.
 *
 * @property model Name of the model to use.
 * @property messages Conversation history.
 * @property stream Whether to stream the result.
 */
@Serializable
class ChatRequest(
    @SerialName("model") val model: String,
    @SerialName("messages") val messages: List<ChatMessage>,
    @SerialName("stream") val stream: Boolean = false
)
