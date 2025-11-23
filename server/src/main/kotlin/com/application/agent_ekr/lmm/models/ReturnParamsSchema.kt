package com.application.agent_ekr.lmm.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Defines the return parameters schema.
 *
 * @property type Schema type.
 * @property properties Properties map.
 */
@Serializable
class ReturnParamsSchema(
    @SerialName("type") val type: String,
    @SerialName("properties") val properties: Map<String, PropertyDefinition>
)
