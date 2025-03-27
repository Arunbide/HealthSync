package com.arb.HealthSync.ai.data

data class ChatMessage(
    val role: String,
    val content: String
)

data class ChatRequest(
    val model: String = "deepseek/deepseek-chat:free",
    val messages: List<ChatMessage>,
    val stream: Boolean = false
)

data class ChatResponse(
    val id: String,
    val choices: List<Choice>
)

data class Choice(
    val message: ChatMessage
)
