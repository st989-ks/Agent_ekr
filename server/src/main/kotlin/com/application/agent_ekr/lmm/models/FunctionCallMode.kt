package com.application.agent_ekr.lmm.models

import kotlinx.serialization.SerialName

enum class FunctionCallMode(val mode: String) {

    @SerialName("auto")
    AUTO("auto"),

    @SerialName("none")
    NONE("none")
}