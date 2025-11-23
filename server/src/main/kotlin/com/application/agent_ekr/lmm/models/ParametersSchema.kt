package com.application.agent_ekr.lmm.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Defines the input parameters schema.
 *
 * @property type Schema type.
 * @property properties Properties map.
 * @property required Required fields.
 */
@Serializable
class ParametersSchema(
    @SerialName("type") val type: String,
    @SerialName("properties") val properties: Map<String, PropertyDefinition>,
    @SerialName("required") val required: List<String>?
)