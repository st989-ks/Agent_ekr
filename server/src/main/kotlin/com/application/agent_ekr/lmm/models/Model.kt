package com.application.agent_ekr.lmm.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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