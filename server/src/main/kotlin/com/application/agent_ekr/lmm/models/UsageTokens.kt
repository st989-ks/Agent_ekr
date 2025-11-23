package com.application.agent_ekr.lmm.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * Details about token usage in the response.
 *
 * @param promptTokens Number of tokens in the prompt.
 * @param completionTokens Number of tokens in the completion.
 * @param totalTokens Total number of tokens processed.
 */
@Serializable
class UsageTokens(
    @SerialName("prompt_tokens") val promptTokens: Int,
    @SerialName("completion_tokens") val completionTokens: Int,
    @SerialName("total_tokens") val totalTokens: Int
)