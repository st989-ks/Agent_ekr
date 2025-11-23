package com.application.agent_ekr.lmm.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class StreamDelta(
    @SerialName("content") val content: String? = null,
    @SerialName("role") val role: MessageRole? = null
)