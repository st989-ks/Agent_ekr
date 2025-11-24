package com.application.agent_ekr

import kotlinx.serialization.SerialName

enum class FunctionCallMode(val mode: String) {

    @SerialName("auto")
    AUTO("auto"),

    @SerialName("none")
    NONE("none")
}