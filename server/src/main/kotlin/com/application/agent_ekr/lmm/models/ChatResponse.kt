package com.application.agent_ekr.lmm.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class ChatResponse(
    @SerialName("choices") val choices: List<MessageChoice>,
    @SerialName("created") val created: Long,
    @SerialName("model") val model: String,
    @SerialName("object") val obj: String,
    @SerialName("usage") val usage: UsageTokens
)

@Serializable
class MessageChoice(
    @SerialName("message") val message: ChatMessage,
    @SerialName("index") val index: Int,
    @SerialName("finish_reason") val finishReason: String
)

@Serializable
class Usage(
    @SerialName("prompt_tokens") val promptTokens: Int,
    @SerialName("completion_tokens") val completionTokens: Int,
    @SerialName("total_tokens") val totalTokens: Int
)
