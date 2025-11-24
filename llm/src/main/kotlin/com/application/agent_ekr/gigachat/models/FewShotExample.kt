package com.application.agent_ekr.gigachat.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

/**
 * Example pair showing how a custom function should be invoked.
 *
 * @param request User's request triggering the function.
 * @param params Arguments passed to the function.
 */
@Serializable
class FewShotExample(
    @SerialName("request") val request: String,
    @SerialName("params") val params: JsonObject
)