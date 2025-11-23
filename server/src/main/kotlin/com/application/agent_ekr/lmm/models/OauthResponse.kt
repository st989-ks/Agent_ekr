package com.application.agent_ekr.lmm.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * OAuth response containing authentication details.
 *
 * @property accessToken Access token.
 * @property expiresAt Expiration timestamp.
 */
@Serializable
class OauthResponse(
    @SerialName("access_token") val accessToken: String,
    @SerialName("expires_at") val expiresAt: Long
)
