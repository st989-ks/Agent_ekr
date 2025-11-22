package com.application.agent_ekr.lmm.managers

class TokenManager() {

    private val authKey: String? = null
    private var expTime = 0L
    var refreshToken: String = ""


    fun getToken(): String {
        if (isTokenExpired()) {

            val tokenResponse =
                objectMapper.readValue(response.body, AccessTokenResponse::class.java)
            expTime = tokenResponse.expiresAt
            refreshToken = tokenResponse.accessToken
        }

        return refreshToken
    }

    fun isTokenExpired(): Boolean = System.currentTimeMillis() + 3000L >= expTime
}