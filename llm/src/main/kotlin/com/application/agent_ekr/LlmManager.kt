package com.application.agent_ekr

import com.application.agent_ekr.models.common.ChatInput
import com.application.agent_ekr.models.common.ChatMessage
import kotlinx.coroutines.flow.Flow

interface LlmManager {

    suspend fun getModels(): List<String>

    // Backward compatible method
    suspend fun sendMessageStream(message: String): Flow<String>

    // Enhanced methods for universal input
    suspend fun sendMessageStream(input: ChatInput): Flow<String>

    suspend fun sendMessageStream(messages: List<ChatMessage>): Flow<String> {
        return sendMessageStream(ChatInput(messages = messages))
    }
}