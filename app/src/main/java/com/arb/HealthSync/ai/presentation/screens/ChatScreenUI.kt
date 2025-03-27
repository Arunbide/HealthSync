package com.arb.HealthSync.ai.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arb.HealthSync.ai.presentation.componants.ChatBubble
import com.arb.HealthSync.ai.presentation.componants.TypingIndicator
import com.arb.HealthSync.ai.presentation.viewModel.ChatViewModel

@Composable
fun ChatContent(
    userInput: String,
    onUserInputChange: (String) -> Unit,
    chatMessages: List<Pair<String, Boolean>>,
    isTyping: Boolean,
    scrollState: LazyListState,
    sendUserMessage: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val colors = MaterialTheme.colorScheme

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Chat messages area
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(colors.surfaceVariant.copy(alpha = 0.1f))
        ) {
            LazyColumn(
                state = scrollState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp),
                contentPadding = PaddingValues(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                if (chatMessages.isEmpty() && !isTyping) {
                    item {
                        WelcomeMessage()
                    }
                }

                items(chatMessages) { (message, isUser) ->
                    ChatBubble(
                        message = message,
                        isUser = isUser,

                    )
                }

                if (isTyping) {
                    item {
                        TypingIndicator(color = colors.primary)
                    }
                }
            }
        }

        Surface(
            modifier = Modifier
                .fillMaxWidth(),
            color = colors.surface,
            tonalElevation = 8.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = userInput,
                    onValueChange = onUserInputChange,
                    modifier = Modifier
                        .weight(1f),
                    textStyle = TextStyle(
                        fontSize = 16.sp,
                        color = colors.onSurface
                    ),
                    placeholder = {
                        Text(
                            "Ask about your health...",
                            color = colors.onSurfaceVariant,
                            fontSize = 16.sp
                        )
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Send
                    ),
                    keyboardActions = KeyboardActions(
                        onSend = {
                            sendUserMessage()
                            focusManager.clearFocus()
                        }
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = colors.surfaceVariant.copy(alpha = 0.3f),
                        unfocusedContainerColor = colors.surfaceVariant.copy(alpha = 0.2f),
                        cursorColor = colors.primary,
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        focusedTextColor = colors.onSurface,
                        unfocusedTextColor = colors.onSurface
                    ),
                    shape = MaterialTheme.shapes.extraLarge,
                    maxLines = 5
                )

                FloatingActionButton(
                    onClick = {
                        sendUserMessage()
                        focusManager.clearFocus()
                    },
                    containerColor = if (userInput.isNotBlank()) colors.primary else colors.surfaceVariant,
                    contentColor = colors.onPrimary,
                    modifier = Modifier.size(48.dp),
                    elevation = FloatingActionButtonDefaults.elevation(
                        defaultElevation = 2.dp,
                        pressedElevation = 8.dp,
                    )
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send",
                        modifier = Modifier.size(24.dp))                }
            }
        }
    }
}

@Composable
fun WelcomeMessage() {
    val colors = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 126.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = colors.surfaceVariant,
            tonalElevation = 2.dp
        ) {
            Text(
                text = "Welcome to Health Advisor! I'm here to help with your health questions. What would you like to know today?",
                color = colors.onSurfaceVariant,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
fun sendMessage(
    message: String,
    chatViewModel: ChatViewModel,
    chatMessages: List<Pair<String, Boolean>>,
    isTyping: Boolean,
    onUpdate: (List<Pair<String, Boolean>>, Boolean) -> Unit
) {
    if (message.isNotBlank()) {
        val updatedMessages = chatMessages + (message to true)
        onUpdate(updatedMessages, true)

        chatViewModel.sendMessage(message) { response ->
            val finalMessages = updatedMessages + (response to false)
            onUpdate(finalMessages, false)
        }
    }
}