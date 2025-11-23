package com.application.agent_ekr.lmm.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Enumeration representing roles in a conversation.
 *
 * @property role Role name.
 */
@Serializable
enum class MessageRole(val role: String) {

    @SerialName("system")
    SYSTEM("system"),

    @SerialName("user")
    USER("user"),

    @SerialName("assistant")
    ASSISTANT("assistant"),

    @SerialName("function")
    FUNCTION("function")
}
