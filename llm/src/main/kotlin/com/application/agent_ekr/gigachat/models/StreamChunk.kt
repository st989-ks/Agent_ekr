package com.application.agent_ekr.gigachat.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class StreamChunk(
    @SerialName("choices") val choices: List<StreamChoice>,
    @SerialName("created") val created: Long? = null,
    @SerialName("model") val model: String? = null,
    @SerialName("object") val obj: String? = null,
    @SerialName("usage") val usage: UsageTokens? = null
)