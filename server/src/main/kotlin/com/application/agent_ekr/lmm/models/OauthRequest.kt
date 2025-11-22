package com.application.agent_ekr.lmm.models

import java.util.UUID

data class OauthRequest(
    val rqUid: String = UUID.randomUUID().toString(),
    val credentials: String = "client_credentials",
    val scope: GigaChatClientScope = GigaChatClientScope.GIGACHAT_API_PERS
)
