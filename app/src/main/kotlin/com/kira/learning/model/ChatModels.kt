package com.kira.learning.model

import androidx.compose.runtime.Immutable

@Immutable
data class ChatMessageUi(
    val id: Long,
    val content: String,
    val isUser: Boolean,
    val timestamp: Long,
    val pending: Boolean = false,
)

@Immutable
data class ChatUiState(
    val messages: List<ChatMessageUi> = emptyList(),
    val input: String = "",
    val sending: Boolean = false,
    val error: String? = null,
) {
    val canSend: Boolean get() = input.isNotBlank() && !sending
}

