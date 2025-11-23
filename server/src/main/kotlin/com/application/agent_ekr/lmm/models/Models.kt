package com.application.agent_ekr.lmm.models

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

/**
 * Represents a single model.
 *
 * @property id Model ID.
 * @property obj Object type ("model").
 * @property ownedBy Owner of the model.
 * @property type Model type.
 */
@Serializable
class Model(
    @SerialName("id") val id: String,
    @SerialName("object") val obj: String,
    @SerialName("owned_by") val ownedBy: String,
    @SerialName("type") val type: String
)
