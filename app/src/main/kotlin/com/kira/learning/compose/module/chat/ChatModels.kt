package com.kira.learning.compose.module.chat

import androidx.compose.runtime.Immutable

// ChatMessageUi: UI 层展示的单条消息（已从数据库/网络转换为界面友好格式）
//  - id: 唯一标识（数据库主键或临时值）
//  - content: 文本内容
//  - isUser: 是否为用户消息（true 用户 / false AI）
//  - timestamp: 时间戳（可用于排序 / 显示相对时间）
//  - pending: 是否为“正在流式生成 / 发送中” 的占位消息
@Immutable
data class ChatMessageUi(
    val id: Long,
    val content: String,
    val isUser: Boolean,
    val timestamp: Long,
    val pending: Boolean = false,
)

// ChatUiState: 聊天界面整体状态（单一数据源 UDF）
//  - messages: 当前会话消息列表（含可能的 pending 消息）
//  - input: 文本输入框当前内容
//  - sending: 是否正在与后端交互（流式或单次）
//  - error: 最近一次错误提示（Snackbar 展示后可清空）
//  衍生属性 canSend：输入非空 & 不在发送中 才允许发送
@Immutable
data class ChatUiState(
    val messages: List<ChatMessageUi> = emptyList(),
    val input: String = "",
    val sending: Boolean = false,
    val error: String? = null,
) {
    val canSend: Boolean get() = input.isNotBlank() && !sending
}
