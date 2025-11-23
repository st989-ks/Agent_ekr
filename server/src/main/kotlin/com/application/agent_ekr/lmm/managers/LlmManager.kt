package com.application.agent_ekr.lmm.managers

import kotlinx.coroutines.flow.Flow

interface LlmManager {

    suspend fun getModels(): String

    suspend fun sendMessageStream(message:String): Flow<String>
}