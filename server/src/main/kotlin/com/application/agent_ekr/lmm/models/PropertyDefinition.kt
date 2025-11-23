package com.application.agent_ekr.lmm.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Definition of a property in the parameters schema.
 *
 * @param type Type of the field (e.g., 'string', 'number').
 * @param description Human-readable explanation of the field.
 * @param format Formatting details (e.g., 'date-time').
 * @param enum Possible enumerated values.
 */
@Serializable
class PropertyDefinition(
    @SerialName("type") val type: String,
    @SerialName("description") val description: String,
    @SerialName("format") val format: String?,
    @SerialName("enum") val enum: List<String>?
)
