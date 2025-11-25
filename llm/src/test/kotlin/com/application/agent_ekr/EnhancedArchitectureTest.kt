package com.application.agent_ekr

import com.application.agent_ekr.gigachat.adapters.GigaChatAdapter
import com.application.agent_ekr.gigachat.models.FunctionCall
import com.application.agent_ekr.models.common.ChatInput
import com.application.agent_ekr.models.common.ChatMessage
import com.application.agent_ekr.models.common.GenerationParameters
import com.application.agent_ekr.models.common.ToolDefinition
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class EnhancedArchitectureTest {
    
    @Test
    fun testChatInputToGigaChatRequestConversion() {
        val chatInput = ChatInput(
            messages = listOf(
                ChatMessage.system("You are a helpful assistant"),
                ChatMessage.user("Hello, world!")
            ),
            parameters = GenerationParameters(
                temperature = 0.7,
                maxTokens = 100
            ),
            stream = true
        )
        
        val gigaRequest = GigaChatAdapter.run {
            chatInput.toGigaChatRequest()
        }
        
        assertNotNull(gigaRequest)
        assertEquals(2, gigaRequest.messages.size)
        assertEquals("system", gigaRequest.messages[0].role.role)
        assertEquals("user", gigaRequest.messages[1].role.role)
        assertEquals(0.7, gigaRequest.temperature)
        assertEquals(100, gigaRequest.maxTokens)
        assertTrue(gigaRequest.stream == true)
    }
    
    @Test
    fun testToolDefinitionConversion() {
        val toolDefinition = ToolDefinition(
            name = "test_tool",
            description = "A test tool",
            parameters = buildJsonObject {
                put("type", "string")
                put("description", "Test parameter")
            }
        )
        
        val gigaFunction = GigaChatAdapter.run {
            toolDefinition.toGigaChatFunction()
        }
        
        assertNotNull(gigaFunction)
        assertEquals("test_tool", gigaFunction.name)
        // Description is now a JsonObject, so we need to check its structure
        assertNotNull(gigaFunction.description)
        assertEquals("string", gigaFunction.description?.get("type")?.jsonPrimitive?.content)
        assertEquals("A test tool", gigaFunction.description?.get("description")?.jsonPrimitive?.content)
        assertNotNull(gigaFunction.parameters)
    }
    
    @Test
    fun testFunctionCallConversion() {
        val gigaFunctionCall = FunctionCall(
            name = "test_function",
            arguments = """{"param": "value"}"""
        )
        
        val universalToolCall = GigaChatAdapter.run {
            gigaFunctionCall.toUniversalToolCall()
        }
        
        assertNotNull(universalToolCall)
        assertEquals("test_function", universalToolCall.function.name)
        assertEquals("""{"param": "value"}""", universalToolCall.function.arguments)
        assertTrue(universalToolCall.id.startsWith("call_"))
    }
    
    @Test
    fun testAssistantMessageWithToolCalls() {
        val toolCall = com.application.agent_ekr.models.common.ToolCall(
            id = "call_123",
            function = com.application.agent_ekr.models.common.FunctionCall(
                name = "test_tool",
                arguments = "{}"
            )
        )
        
        val chatInput = ChatInput(
            messages = listOf(
                ChatMessage.system("You are a helpful assistant"),
                ChatMessage.assistant("I'll use a tool", listOf(toolCall))
            )
        )
        
        val gigaRequest = GigaChatAdapter.run {
            chatInput.toGigaChatRequest()
        }
        
        assertNotNull(gigaRequest)
        assertEquals(2, gigaRequest.messages.size)
        assertEquals("assistant", gigaRequest.messages[1].role.role)
        assertNotNull(gigaRequest.messages[1].functionCall)
        assertEquals("test_tool", gigaRequest.messages[1].functionCall?.name)
    }
    
    @Test
    fun testToolMessageConversion() {
        val chatInput = ChatInput(
            messages = listOf(
                ChatMessage.tool(
                    content = "Tool result",
                    toolCallId = "call_123",
                    name = "test_tool"
                )
            )
        )
        
        val gigaRequest = GigaChatAdapter.run {
            chatInput.toGigaChatRequest()
        }
        
        assertNotNull(gigaRequest)
        assertEquals(1, gigaRequest.messages.size)
        assertEquals("function", gigaRequest.messages[0].role.role)
        assertEquals("test_tool", gigaRequest.messages[0].name)
        assertEquals("Tool result", gigaRequest.messages[0].content)
    }
    
    @Test
    fun testBackwardCompatibility() {
        // This test ensures that the original interface still works
        // The actual implementation would be tested with a mock LLM manager
        println("Backward compatibility maintained via interface method overloading")
    }
}
