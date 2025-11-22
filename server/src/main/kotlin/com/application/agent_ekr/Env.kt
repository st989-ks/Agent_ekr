package com.application.agent_ekr

object Env {
    val GIGACHAT_TOKEN: String =
        System.getenv("GIGACHAT_TOKEN")
            ?: throw IllegalStateException("GIGACHAT_TOKEN не установлен")
}