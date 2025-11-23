package com.application.agent_ekr.lmm.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Single message exchanged between the user and the model.
 *
 * @param role Role of the sender ('system', 'user', 'assistant', 'function').
 * @param content Textual content of the message.
 * @param functionsStateId Identifier linking related function calls.
 * @param functionCall ... .
 * @param name ... .
 */
@Serializable
class Message(
    @SerialName("role") val role: MessageRole,
    @SerialName("content") val content: String,
    @SerialName("functions_state_id") val functionsStateId: String? = null,
    @SerialName("function_call") val functionCall: FunctionCall? = null,
    @SerialName("name") val name: String? = null
)