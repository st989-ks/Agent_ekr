package com.application.agent_ekr.lmm.models

import java.util.UUID

/**
 * OAuth request configuration.
 *
 * @property rqUid Unique request ID.
 * @property credentials Authentication credentials.
 * @property scope Scope of authorization.
 */
class OauthRequest(
    val rqUid: String = UUID.randomUUID().toString(),
    val credentials: String = "client_credentials",
    val scope: GigaChatClientScope = GigaChatClientScope.PERSONAL
)
