package com.application.agent_ekr

import kotlinx.coroutines.flow.Flow

interface LlmManager {

    suspend fun getModels(): List<String>

    suspend fun sendMessageStream(message:String): Flow<String>
}