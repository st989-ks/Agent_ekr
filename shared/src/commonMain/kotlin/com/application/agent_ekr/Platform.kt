package com.application.agent_ekr

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform