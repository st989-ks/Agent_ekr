package com.application.agent_ekr.lmm.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
class FunctionCall(
    @SerialName("name")
    val name: String,
    @SerialName("arguments")
    val arguments: JsonObject? = null
)