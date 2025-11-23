package com.application.agent_ekr.lmm.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * Represents a single message choice.
 *
 * @property message Generated message.
 * @property index Choice index.
 * @property finishReason Reason for stopping generation.
 */
@Serializable
class MessageChoice(
    @SerialName("message") val message: ChatMessage,
    @SerialName("index") val index: Int,
    @SerialName("finish_reason") val finishReason: String
)
