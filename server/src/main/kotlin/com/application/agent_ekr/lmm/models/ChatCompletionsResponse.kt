package com.application.agent_ekr.lmm.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Response received from the `/chat/completions` endpoint.
 *
 * @param choices Generated completions.
 * @param created Timestamp of creation.
 * @param model Used model identifier.
 * @param usage Information about token usage.
 */
@Serializable
class ChatCompletionsResponse(
    @SerialName("choices") val choices: List<Choice>,
    @SerialName("created") val created: Long,
    @SerialName("model") val model: String,
    @SerialName("usage") val usage: UsageTokens
)
