package com.application.agent_ekr.gigachat.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Response listing available models.
 *
 * @property data List of models.
 * @property obj Object type ("list").
 */
@Serializable
class ModelsResponse(
    @SerialName("data") val data: List<Model>,
    @SerialName("object") val obj: String
)