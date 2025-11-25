package com.application.agent_ekr.persistence

import com.application.agent_ekr.models.common.ChatRole
import com.application.agent_ekr.models.common.ToolCall

/**
 * Assembles streaming responses into complete messages for persistence.
 */
class MessageAssembler {
    private val buffer = StringBuilder()
    private val toolCalls = mutableListOf<ToolCall>()

    fun appendContent(content: String) {
        buffer.append(content)
    }

    fun addToolCall(toolCall: ToolCall) {
        toolCalls.add(toolCall)
    }

    // TODO: Implement complete message assembly with error handling
    fun assemble(
        conversationId: String,
        role: ChatRole,
        toolResults: List<ToolResult>? = null
    ): AssembledMessage {
        return AssembledMessage(
            conversationId = conversationId,
            role = role,
            content = buffer.toString(),
            toolCalls = if (toolCalls.isNotEmpty()) toolCalls else null,
            toolResults = toolResults
        ).also {
            reset()
        }
    }

    fun reset() {
        buffer.clear()
        toolCalls.clear()
    }
}