package com.application.agent_ekr.lmm.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Response containing chat completions.
 *
 * @property choices List of generated messages.
 * @property created Creation timestamp.
 * @property model Model used for generation.
 * @property obj Object type ("chat.completion").
 * @property usage Token usage statistics.
 */
@Serializable
class ChatResponse(
    @SerialName("choices") val choices: List<MessageChoice>,
    @SerialName("created") val created: Long,
    @SerialName("model") val model: String,
    @SerialName("object") val obj: String,
    @SerialName("usage") val usage: UsageTokens
)
