package com.application.agent_ekr.gigachat.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Enumeration of GigaChat client scopes.
 *
 * @property scope Scope name.
 */
@Serializable
enum class GigaChatClientScope(val scope: String) {

    @SerialName("GIGACHAT_API_PERS")
    PERSONAL("GIGACHAT_API_PERS"),

    @SerialName("GIGACHAT_API_B2B")
    BUSINESS_TO_BUSINESS("GIGACHAT_API_B2B"),

    @SerialName("GIGACHAT_API_CORP")
    CORPORATE("GIGACHAT_API_CORP"),

}