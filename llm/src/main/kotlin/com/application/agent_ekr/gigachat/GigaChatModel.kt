package com.application.agent_ekr.gigachat

import com.application.agent_ekr.ChatModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Enumeration of supported GigaChat models.
 *
 * @property model Model name.
 */
@Serializable
enum class GigaChatModel(override val model: String): ChatModel {

    @SerialName("GigaChat-2")
    GIGA_CHAT_2("GigaChat-2"),

    @SerialName("GigaChat-2-Pro")
    GIGA_CHAT_2_PRO("GigaChat-2-Pro"),

    @SerialName("GigaChat-2-Max")
    GIGA_CHAT_2_MAX("GigaChat-2-Max"),
}