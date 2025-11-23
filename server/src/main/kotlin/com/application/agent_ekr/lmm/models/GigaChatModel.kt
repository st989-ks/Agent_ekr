package com.application.agent_ekr.lmm.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Enumeration of supported GigaChat models.
 *
 * @property model Model name.
 */
@Serializable
enum class GigaChatModel(val model: String) {

    @SerialName("GigaChat-2")
    GIGA_CHAT_2("GigaChat-2"),

    @SerialName("GigaChat-2-Pro")
    GIGA_CHAT_2_PRO("GigaChat-2-Pro"),

    @SerialName("GigaChat-2-Max")
    GIGA_CHAT_2_MAX("GigaChat-2-Max"),
}
