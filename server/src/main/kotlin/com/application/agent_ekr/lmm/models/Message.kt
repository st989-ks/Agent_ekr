package com.application.agent_ekr.lmm.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Single message exchanged between the user and the model.
 *
 * @param role Role of the sender ('system', 'user', 'assistant', 'function').
 * @param content Textual content of the message.
 * @param functionsStateId Identifier linking related function calls.
 * @param attachments Identifiers of attached files.
 */
@Serializable
class Message(
    @SerialName("role") val role: Role,
    @SerialName("content") val content: String,
    @SerialName("functions_state_id") val functionsStateId: String?,
    @SerialName("attachments") val attachments: List<String>?
)