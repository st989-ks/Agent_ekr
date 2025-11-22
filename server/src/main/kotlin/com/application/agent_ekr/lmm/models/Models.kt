package com.application.agent_ekr.lmm.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class ModelsResponse(
    @SerialName("data") val data: List<Model>,
    @SerialName("object") val obj: String
)

@Serializable
class Model(
    @SerialName("id") val id: String,
    @SerialName("object") val obj: String,
    @SerialName("owned_by") val ownedBy: String,
    @SerialName("type") val type: String
)
