package com.application.agent_ekr.lmm.models

data class ChatResponse(
    val choices: List<MessageChoice>,
    val created: Long,
    val model: String,
    val `object`: String,
    val usage: Usage
)

data class MessageChoice(
    val message: ChatMessage,
    val index: Int,
    val finish_reason: String
)

data class Usage(
    val prompt_tokens: Int,
    val completion_tokens: Int,
    val total_tokens: Int
)
