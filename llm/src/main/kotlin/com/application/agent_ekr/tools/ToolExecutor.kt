package com.application.agent_ekr.tools

import com.application.agent_ekr.models.common.ChatMessage
import com.application.agent_ekr.models.common.ChatRole
import com.application.agent_ekr.models.common.ToolCall
import com.application.agent_ekr.persistence.MessageAssembler
import com.application.agent_ekr.persistence.ToolResult

/**
 * Complete tool execution flow for agent workflows
 */
class ToolExecutor(
    private val toolRegistry: ToolRegistry,
    private val messageAssembler: MessageAssembler
) {
    // TODO: Implement parallel tool execution and result aggregation
    suspend fun processToolCalls(
        toolCalls: List<ToolCall>,
        conversationId: String
    ): List<ChatMessage> {
        val toolResults = mutableListOf<ToolResult>()
        val toolMessages = mutableListOf<ChatMessage>()

        for (toolCall in toolCalls) {
            val result = toolRegistry.executeToolCall(toolCall)
            toolResults.add(result)

            toolMessages.add(
                ChatMessage.tool(
                    content = if (result.success) result.result else "Error: ${result.error}",
                    toolCallId = toolCall.id,
                    name = toolCall.function.name
                )
            )
        }

        // Store assembled message with tool results
        messageAssembler.assemble(
            conversationId = conversationId,
            role = ChatRole.ASSISTANT,
            toolResults = toolResults
        )

        return toolMessages
    }
}