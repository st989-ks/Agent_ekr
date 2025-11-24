package com.application.agent_ekr.gigachat.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject


/**
 * Describes a custom function.
 *
 * @property name Function name.
 * @property description Function description.
 * @property parameters Input parameters schema.
 * @property returnParameters Output parameters schema.
 * @property fewShotExamples Examples of function usage.
 */
@Serializable
class CustomFunction(
    @SerialName("name") val name: String,
    @SerialName("description") val description: String,
    @SerialName("parameters") val parameters: JsonObject,
    @SerialName("few_shot_examples") val fewShotExamples: List<FewShotExample>? = null,
    @SerialName("return_parameters") val returnParameters: JsonObject? = null
)
