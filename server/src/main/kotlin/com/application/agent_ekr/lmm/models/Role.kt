package com.application.agent_ekr.lmm.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class Role(val role: String) {

    @SerialName("system")
    ROLE_SYSTEM("system"),

    @SerialName("user")
    ROLE_USER("user"),

    @SerialName("assistant")
    ROLE_ASSISTANT("assistant"),

    @SerialName("function")
    ROLE_FUNCTION("function")
}