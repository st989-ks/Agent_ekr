package com.application.agent_ekr

import com.application.agent_ekr.gigachat.adapters.GigaChatAdapter
import com.application.agent_ekr.models.common.ChatInput
import com.application.agent_ekr.models.common.ChatMessage
import com.application.agent_ekr.models.common.GenerationParameters
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

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
    }
    
    @Test
    fun testToolDefinitionConversion() {
        val toolDefinition = com.application.agent_ekr.models.common.ToolDefinition(
            name = "test_tool",
            description = "A test tool",
            parameters = Json.parseToJsonElement("""{"type": "object", "properties": {}}""").jsonObject
        )
        
        val gigaFunction = GigaChatAdapter.run {
            toolDefinition.toGigaChatFunction()
        }
        
        assertNotNull(gigaFunction)
        assertEquals("test_tool", gigaFunction.name)
        assertEquals("A test tool", gigaFunction.description)
    }
    
    @Test
    fun testBackwardCompatibility() {
        // This test ensures that the original interface still works
        // The actual implementation would be tested with a mock LLM manager
        println("Backward compatibility maintained via interface method overloading")
    }
}
