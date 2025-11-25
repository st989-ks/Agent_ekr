package com.application.agent_ekr.models.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


/**
 * Universal generation parameters compatible across LLM providers.
 *
 * Usage: Standardized way to pass model settings
 * Compatibility: Maps to provider-specific parameter names
 */
@Serializable
data class GenerationParameters(
    @SerialName("temperature") val temperature: Double? = null,
    @SerialName("max_tokens") val maxTokens: Int? = null,
    @SerialName("top_p") val topP: Double? = null,
    @SerialName("frequency_penalty") val frequencyPenalty: Double? = null,
    @SerialName("presence_penalty") val presencePenalty: Double? = null
)

