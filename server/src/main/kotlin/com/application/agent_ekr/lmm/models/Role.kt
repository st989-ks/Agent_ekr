package com.application.agent_ekr.lmm.models

enum class Role(val role: String) {
    ROLE_SYSTEM("system"),
    ROLE_USER("user"),
    ROLE_ASSISTANT("assistant"),
    ROLE_FUNCTION("function")
}