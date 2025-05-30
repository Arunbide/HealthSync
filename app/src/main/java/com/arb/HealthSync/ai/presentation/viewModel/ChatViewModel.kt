package com.arb.HealthSync.ai.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arb.HealthSync.ai.RetrofitClient
import com.arb.HealthSync.ai.data.ChatMessage
import com.arb.HealthSync.ai.data.ChatRequest

import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ChatViewModel : ViewModel() {

    private val apiService = RetrofitClient.apiService

    private val chatHistory = mutableListOf<ChatMessage>()

    private val _chatResponse = MutableStateFlow<String?>(null)
    val chatResponse: StateFlow<String?> = _chatResponse

    fun sendMessage(userMessage: String, onResponse: (String) -> Unit) {
        viewModelScope.launch {
            chatHistory.add(ChatMessage("user", userMessage))


            if (chatHistory.size > 3) {
                chatHistory.removeAt(0)
            }

            val request = ChatRequest(
                messages = listOf(
                    ChatMessage("system", "You're HealthWise - a friendly AI health advisor and ai doctor. " +
                            "Give clear, practical health tips in simple language. " +
                            "Be supportive but professional - like a doctor who explains things simply. " +
                            "Keep responses short " +
                            "Use occasional simple emojis like 🩺 or 💊 when helpful. " +
                            "Focus on prevention, healthy habits and basic symptom guidance.")
                ) + chatHistory
            )

            try {
                val response = apiService.getChatResponse(request)
                if (response.isSuccessful) {
                    response.body()?.choices?.firstOrNull()?.message?.content?.let { reply ->
                        _chatResponse.value = reply
                        chatHistory.add(ChatMessage("assistant", reply))
                    }
                } else {
                    _chatResponse.value = "Error: ${response.errorBody()?.string()}"
                }
                onResponse(_chatResponse.value ?: "Error in response")
            } catch (e: Exception) {
                onResponse("Error: ${e.localizedMessage}")
            }
        }
    }

}
