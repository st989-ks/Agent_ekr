package com.application.agent_ekr.lmm.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class GigaChatClientScope() {

    @SerialName("GIGACHAT_API_PERS")
    GIGACHAT_API_PERS,

    @SerialName("GIGACHAT_API_B2B")
    GIGACHAT_API_B2B,

    @SerialName("GIGACHAT_API_CORP")
    GIGACHAT_API_CORP,

}

