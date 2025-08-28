package com.kira.learning.compose.module.sample

import androidx.compose.runtime.Immutable

@Immutable
data class SampleImage(
    val id: String,
    val title: String,
    val thumbnailUrl: String,
)

// UI 层状态
sealed interface SampleUiState {
    object Idle: SampleUiState
    object Loading: SampleUiState
    data class Success(val data: List<SampleImage>, val refreshing: Boolean = false): SampleUiState
    data class Error(val message: String): SampleUiState
}

// 用户意图 / 事件
sealed interface SampleEvent {
    object Load: SampleEvent
    object Refresh: SampleEvent
    data class Retry(val reason: String = ""): SampleEvent
}
