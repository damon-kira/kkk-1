package com.kira.learning.module.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kira.learning.di.ChatConversationDao
import com.kira.learning.di.ChatConversation
import com.kira.learning.model.dao.ChatMessageDao
import com.kira.learning.model.dao.ChatMessage
import com.kira.learning.network.ApiResult
import com.kira.learning.network.ErrorMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.kira.ui.core.storage.keyvalue.SettingsManager
import com.kira.learning.network.InMemoryAuthTokenProvider
import kotlinx.coroutines.flow.update

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val repo: ChatRepository,
    private val chatMessageDao: ChatMessageDao,
    private val conversationDao: ChatConversationDao,
    private val settings: SettingsManager,
    private val authProvider: InMemoryAuthTokenProvider,
) : ViewModel() {

    // 当前会话 id（null 表示尚未创建）
    private val _currentConversationId = MutableStateFlow<Long?>(null)

    // UI 主状态：单一数据源（messages 由 DB + pending 合成）
    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    // 会话列表：直接从 Dao Flow 转为 StateFlow，界面自动重组
    val conversations: StateFlow<List<ChatConversation>> = conversationDao.listFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // 高级开关（Mock / Stream）
    private val _adv = MutableStateFlow(ChatAdvancedState())
    val advanced: StateFlow<ChatAdvancedState> = _adv.asStateFlow()

    // Token 状态（设置里修改 -> 更新 OkHttp）
    private val _token = MutableStateFlow(settings.aiToken ?: "")
    val token: StateFlow<String> = _token.asStateFlow()

    init {
        // 懒创建：不再这里创建会话，直到用户第一次发送消息
        viewModelScope.launch {
            _currentConversationId
                .filterNotNull()
                .flatMapLatest { id -> chatMessageDao.getMessagesByConversation(id) }
                .collectLatest { list ->
                    val mapped = list.map {
                        ChatMessageUi(
                            id = it.id,
                            content = it.content,
                            isUser = it.isUser == "USER",
                            timestamp = it.timestamp
                        )
                    }
                    _uiState.update { s -> s.copy(messages = mergeWithPending(mapped, s)) }
                }
        }
    }

    // 懒创建辅助：若当前没有会话则新建并返回 id
    private suspend fun ensureConversationInitialized(firstUserMessage: String): Long {
        val existing = _currentConversationId.value
        if (existing != null) return existing
        val titleSeed = firstUserMessage.trim().ifBlank { "新会话" }
        val convId = conversationDao.insert(ChatConversation(title = titleSeed.take(20)))
        _currentConversationId.value = convId
        return convId
    }

    // 将 DB 消息与一个可能存在的 pending 流式消息合并（避免闪烁）
    private fun mergeWithPending(
        dbMessages: List<ChatMessageUi>,
        state: ChatUiState
    ): List<ChatMessageUi> {
        if (!state.sending) return dbMessages
        val pending = state.messages.lastOrNull { it.pending } ?: return dbMessages
        return dbMessages + pending
    }

    // —— 基础交互 ——
    fun updateInput(v: String) {
        _uiState.update { it.copy(input = v) }
    }

    fun startNewConversation() {
        viewModelScope.launch {
            // 删除上一个空会话
            _currentConversationId.value?.let { prevId ->
                if (chatMessageDao.countMessages(prevId) == 0) {
                    conversationDao.delete(prevId)
                }
            }
            // 仅重置 UI，不立即创建记录，等待首条消息再创建
            _currentConversationId.value = null
            _uiState.value = ChatUiState()
        }
    }

    fun renameConversation(id: Long, title: String) = viewModelScope.launch {
        conversationDao.find(id)?.let {
            conversationDao.update(
                it.copy(
                    title = title,
                    updatedAt = System.currentTimeMillis()
                )
            )
        }
    }

    fun deleteConversation(id: Long) = viewModelScope.launch {
        conversationDao.delete(id)
        if (_currentConversationId.value == id) startNewConversation()
    }

    fun switchConversation(id: Long) {
        val prev = _currentConversationId.value
        if (prev == id) return
        viewModelScope.launch {
            // 切换前清理上一个空会话（若是懒创建阶段 prev 可能为 null 或已存在消息）
            prev?.let {
                if (chatMessageDao.countMessages(it) == 0) {
                    conversationDao.delete(it)
                }
            }
            _currentConversationId.value = id
            _uiState.update { it.copy(messages = emptyList(), sending = false, error = null) }
        }
    }

    // 更新会话预览（列表里显示最后一句 & 更新时间）
    private fun touchConversationPreview(text: String) = viewModelScope.launch {
        val id = _currentConversationId.value ?: return@launch
        conversationDao.touch(id, System.currentTimeMillis(), text.take(50))
    }

    // —— 流式发送 ——
    private var streamingJob: Job? = null
    fun sendStream() {
        val content = _uiState.value.input.trim()
        if (content.isEmpty() || _uiState.value.sending) return
        // 先更新 UI 状态（立即清空输入框）
        val pendingMsg = ChatMessageUi(
            id = Long.MAX_VALUE,
            content = "",
            isUser = false,
            timestamp = System.currentTimeMillis(),
            pending = true
        )
        _uiState.update {
            it.copy(
                input = "",
                sending = true,
                error = null,
                messages = it.messages + pendingMsg
            )
        }
        streamingJob?.cancel()
        streamingJob = viewModelScope.launch {
            // 确保会话存在，然后写入用户消息
            val cid = ensureConversationInitialized(content)
            chatMessageDao.insertMessage(
                ChatMessage(
                    content = content,
                    isUser = "USER",
                    conversationId = cid
                )
            )
            touchConversationPreview(content)
            repo.streamUserMessage(content).collect { res ->
                when (res) {
                    is ApiResult.Success -> _uiState.update { s ->
                        s.copy(messages = s.messages.map { m -> if (m.pending) m.copy(content = res.data) else m })
                    }

                    is ApiResult.Error -> finalizePendingWith(
                        ErrorMapper.map(
                            res.code,
                            res.message
                        )
                    )

                    ApiResult.NetworkUnavailable -> finalizePendingWith("网络不可用")
                }
            }
            val finalText = _uiState.value.messages.lastOrNull { it.pending }?.content ?: ""
            chatMessageDao.insertMessage(
                ChatMessage(
                    content = finalText.ifBlank { "(空)" },
                    isUser = "AI",
                    conversationId = cid
                )
            )
            touchConversationPreview(finalText)
            _uiState.update { s ->
                s.copy(sending = false, messages = s.messages.filterNot { it.pending })
            }
        }
    }

    private fun finalizePendingWith(text: String) {
        _uiState.update { s ->
            s.copy(
                messages = s.messages.map { if (it.pending) it.copy(content = text) else it },
                sending = false
            )
        }
    }

    // 兼容一次性发送
    fun send() {
        if (advanced.value.streaming) sendStream() else sendOnce()
    }

    private fun sendOnce() { // 非流式：一次得到完整回答
        val content = _uiState.value.input.trim()
        if (content.isEmpty() || _uiState.value.sending) return
        _uiState.update { it.copy(input = "", sending = true, error = null) }
        viewModelScope.launch {
            val cid = ensureConversationInitialized(content)
            chatMessageDao.insertMessage(
                ChatMessage(
                    content = content,
                    isUser = "USER",
                    conversationId = cid
                )
            )
            touchConversationPreview(content)
            when (val r = repo.sendUserMessage(content)) {
                is ApiResult.Success -> {
                    chatMessageDao.insertMessage(
                        ChatMessage(
                            content = r.data,
                            isUser = "AI",
                            conversationId = cid
                        )
                    )
                    touchConversationPreview(r.data)
                }

                is ApiResult.Error -> {
                    val mapped = ErrorMapper.map(r.code, r.message)
                    chatMessageDao.insertMessage(
                        ChatMessage(
                            content = "出错: $mapped",
                            isUser = "AI",
                            conversationId = cid
                        )
                    )
                    _uiState.update { s -> s.copy(error = mapped) }
                }

                ApiResult.NetworkUnavailable -> {
                    chatMessageDao.insertMessage(
                        ChatMessage(
                            content = "网络不可用",
                            isUser = "AI",
                            conversationId = cid
                        )
                    )
                }
            }
            _uiState.update { s -> s.copy(sending = false) }
        }
    }

    fun reset() {
        val cid = _currentConversationId.value ?: return
        viewModelScope.launch { chatMessageDao.clearConversation(cid); touchConversationPreview("") }
        _uiState.value = ChatUiState()
    }

    // 高级开关 & token
    fun toggleMock() {
        _adv.update { it.copy(useMock = !it.useMock) }; repo.setUseMock(_adv.value.useMock)
    }

    fun toggleStreaming() {
        _adv.update { it.copy(streaming = !it.streaming) }
    }

    fun updateToken(token: String) {
        settings.aiToken = token; authProvider.updateToken(token); _token.value = token
    }

    fun setToken(token: String) = updateToken(token)
    fun currentConversationId(): Long? = _currentConversationId.value

    override fun onCleared() {
        val id = _currentConversationId.value
        if (id != null) {
            // 使用独立 IO scope 做一次性清理
            CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
                if (chatMessageDao.countMessages(id) == 0) {
                    conversationDao.delete(id)
                }
            }
        }
        super.onCleared()
    }
}

// 高级 UI 状态：区分逻辑标志与主消息状态，避免 ChatUiState 过于臃肿
data class ChatAdvancedState(
    val useMock: Boolean = false,
    val streaming: Boolean = true,
)
