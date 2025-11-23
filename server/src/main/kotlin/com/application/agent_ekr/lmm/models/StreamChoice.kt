package com.application.agent_ekr.lmm.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class StreamChoice(
    @SerialName("delta") val delta: StreamDelta,
    @SerialName("index") val index: Int,
    @SerialName("finish_reason") val finishReason: String? = null
)
