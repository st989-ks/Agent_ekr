# Migration Guide: Enhanced LLM Architecture

## Overview
This guide helps you migrate from the original single-string interface to the enhanced universal input architecture while maintaining full backward compatibility.

## Backward Compatibility
All existing code continues to work without changes:
```kotlin
// OLD CODE - STILL WORKS
val response = llmManager.sendMessageStream("Hello, world!")
```

## New Features

### 1. Message History Support
```kotlin
// NEW: Support for message history and system prompts
val messages = listOf(
    ChatMessage.system("You are a helpful assistant"),
    ChatMessage.user("What's the weather today?"),
    ChatMessage.assistant("I can help with that! Let me check...")
)
val response = llmManager.sendMessageStream(messages)
```

### 2. Full ChatInput with Tools
```kotlin
// NEW: Complete request with tools and parameters
val chatInput = ChatInput(
    messages = listOf(
        ChatMessage.system("You are a weather assistant"),
        ChatMessage.user("What's the weather in Moscow?")
    ),
    tools = toolRegistry.getAvailableTools(),
    parameters = GenerationParameters(
        temperature = 0.7,
        maxTokens = 500
    ),
    model = "GIGA_CHAT_2"
)
val response = llmManager.sendMessageStream(chatInput)
```

### 3. Tool Integration
```kotlin
// NEW: Tool registration and execution
val toolRegistry = ToolRegistry()
toolRegistry.register(WeatherTool())

// Tools will be automatically used when available
val response = llmManager.sendMessageStream(
    ChatInput(
        messages = listOf(ChatMessage.user("Get weather for London")),
        tools = toolRegistry.getAvailableTools()
    )
)
```

## Package Structure Changes

### New Packages:
- `models/common/` - Universal types for cross-provider compatibility
- `gigachat/adapters/` - GigaChat-specific type conversions
- `tools/` - Tool execution framework
- `persistence/` - Message assembly and storage

### Key Classes:
- `ChatInput` - Universal input representation
- `ChatMessage` - Sealed class for different message types
- `ToolRegistry` - Central tool management
- `MessageAssembler` - Stream response aggregation

## Migration Steps

### Step 1: Update Dependencies
Ensure your build file includes the new modules.

### Step 2: Gradual Adoption
Start using new features incrementally while existing code continues to work.

### Step 3: Tool Integration
Register tools and enable function calling in conversations.

### Step 4: Advanced Features
Implement message persistence, conversation memory, and complex tool chains.

## Examples
See `EnhancedUsageExample.kt` for complete usage patterns.

## Support
For migration issues, refer to the test cases in `EnhancedArchitectureTest.kt`.
