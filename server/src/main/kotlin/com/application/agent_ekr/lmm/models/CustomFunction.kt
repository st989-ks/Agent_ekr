package com.application.agent_ekr.lmm.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


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
    @SerialName("parameters") val parameters: ParametersSchema,
    @SerialName("return_parameters") val returnParameters: ReturnParamsSchema?,
    @SerialName("few_shot_examples") val fewShotExamples: List<FewShotExample>?
)
