package com.application.agent_ekr.lmm.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Тут описание эндпоинта в котором применяется и описание поей
 */
@Serializable
class ChatMessage(
    @SerialName("role") val role: Role,
    @SerialName("content") val content: String
)
