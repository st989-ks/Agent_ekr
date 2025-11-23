package com.application.agent_ekr.lmm.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a single message in a conversation.
 *
 * @property role Sender's role (system/user/assistant/function).
 * @property content Message content.
 */
@Serializable
class ChatMessage(
    @SerialName("role") val role: Role,
    @SerialName("content") val content: String
)
