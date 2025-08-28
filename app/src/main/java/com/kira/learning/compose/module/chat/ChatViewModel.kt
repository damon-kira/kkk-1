package com.kira.learning.compose.module.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kira.learning.app.ChatConversationDao
import com.kira.learning.app.ChatConversation
import com.kira.learning.bean.dao.ChatMessageDao
import com.kira.learning.bean.dao.ChatMessage
import com.kira.learning.compose.network.ApiResult
import com.kira.learning.compose.network.ErrorMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
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
import com.kira.learning.compose.network.InMemoryAuthTokenProvider
import kotlinx.coroutines.flow.update

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val repo: ChatRepository,
    private val chatMessageDao: ChatMessageDao,
    private val conversationDao: ChatConversationDao,
    private val settings: SettingsManager,
    private val authProvider: InMemoryAuthTokenProvider,
): ViewModel() {

    private val _currentConversationId = MutableStateFlow<Long?>(null)
    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    val conversations: StateFlow<List<ChatConversation>> = conversationDao.listFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _adv = MutableStateFlow(ChatAdvancedState())
    val advanced: StateFlow<ChatAdvancedState> = _adv.asStateFlow()

    private val _token = MutableStateFlow(settings.apiToken ?: "")
    val token: StateFlow<String> = _token.asStateFlow()

    init {
        // 初始新会话
        startNewConversation()
        viewModelScope.launch {
            _currentConversationId.filterNotNull().flatMapLatest { id -> chatMessageDao.getMessagesByConversation(id) }.collectLatest { list ->
                val mapped = list.map { ChatMessageUi(id = it.id, content = it.content, isUser = it.isUser == "USER", timestamp = it.timestamp) }
                _uiState.update { s -> s.copy(messages = mergeWithPending(mapped, s)) }
            }
        }
    }

    private fun mergeWithPending(dbMessages: List<ChatMessageUi>, state: ChatUiState): List<ChatMessageUi> {
        if (!state.sending) return dbMessages
        val pending = state.messages.lastOrNull { it.pending } ?: return dbMessages
        // 仅在发送中且存在 pending 时附加
        return dbMessages + pending
    }

    fun updateInput(v: String) { _uiState.update { it.copy(input = v) } }

    fun startNewConversation() {
        viewModelScope.launch {
            val convId = conversationDao.insert(ChatConversation(title = "新会话"))
            _currentConversationId.value = convId
            _uiState.value = ChatUiState()
        }
    }

    fun renameConversation(id: Long, title: String) = viewModelScope.launch {
        conversationDao.find(id)?.let { conversationDao.update(it.copy(title = title, updatedAt = System.currentTimeMillis())) }
    }

    fun deleteConversation(id: Long) = viewModelScope.launch {
        conversationDao.delete(id)
        if (_currentConversationId.value == id) startNewConversation()
    }

    fun switchConversation(id: Long) {
        if (_currentConversationId.value == id) return
        _currentConversationId.value = id
        _uiState.update { it.copy(messages = emptyList(), sending = false, error = null) }
    }

    private fun touchConversationPreview(text: String) = viewModelScope.launch {
        val id = _currentConversationId.value ?: return@launch
        conversationDao.touch(id, System.currentTimeMillis(), text.take(50))
    }

    private var streamingJob: Job? = null
    fun sendStream() {
        val content = _uiState.value.input.trim()
        val cid = _currentConversationId.value ?: return
        if (content.isEmpty() || _uiState.value.sending) return
        viewModelScope.launch { chatMessageDao.insertMessage(ChatMessage(content = content, isUser = "USER", conversationId = cid)); touchConversationPreview(content) }
        val pendingMsg = ChatMessageUi(id = Long.MAX_VALUE, content = "", isUser = false, timestamp = System.currentTimeMillis(), pending = true)
        _uiState.update { it.copy(input = "", sending = true, error = null, messages = it.messages + pendingMsg) }
        streamingJob?.cancel()
        streamingJob = viewModelScope.launch {
            repo.streamUserMessage(content).collect { res ->
                when(res) {
                    is ApiResult.Success -> _uiState.update { s -> s.copy(messages = s.messages.map { m -> if (m.pending) m.copy(content = res.data) else m }) }
                    is ApiResult.Error -> finalizePendingWith(ErrorMapper.map(res.code, res.message))
                    ApiResult.NetworkUnavailable -> finalizePendingWith("网络不可用")
                }
            }
            val finalText = _uiState.value.messages.lastOrNull { it.pending }?.content ?: ""
            viewModelScope.launch { chatMessageDao.insertMessage(ChatMessage(content = finalText.ifBlank { "(空)" }, isUser = "AI", conversationId = cid)); touchConversationPreview(finalText) }
            _uiState.update { s -> s.copy(sending = false, messages = s.messages.filterNot { it.pending }) }
        }
    }

    private fun finalizePendingWith(text: String) {
        _uiState.update { s -> s.copy(messages = s.messages.map { if (it.pending) it.copy(content = text) else it }, sending = false) }
    }

    // 兼容旧方法 send() -> 使用流式
    fun send() { if (advanced.value.streaming) sendStream() else sendOnce() }

    private fun sendOnce() { // fallback non-stream single shot
        val content = _uiState.value.input.trim(); val cid = _currentConversationId.value ?: return
        if (content.isEmpty() || _uiState.value.sending) return
        viewModelScope.launch { chatMessageDao.insertMessage(ChatMessage(content = content, isUser = "USER", conversationId = cid)); touchConversationPreview(content) }
        _uiState.update { it.copy(input = "", sending = true, error = null) }
        viewModelScope.launch {
            when(val r = repo.sendUserMessage(content)) {
                is ApiResult.Success -> {
                    chatMessageDao.insertMessage(ChatMessage(content = r.data, isUser = "AI", conversationId = cid)); touchConversationPreview(r.data)
                }
                is ApiResult.Error -> {
                    val mapped = ErrorMapper.map(r.code, r.message)
                    chatMessageDao.insertMessage(ChatMessage(content = "出错: $mapped", isUser = "AI", conversationId = cid)); _uiState.update { s -> s.copy(error = mapped) }
                }
                ApiResult.NetworkUnavailable -> chatMessageDao.insertMessage(ChatMessage(content = "网络不可用", isUser = "AI", conversationId = cid))
            }
            _uiState.update { s -> s.copy(sending = false) }
        }
    }

    fun reset() {
        val cid = _currentConversationId.value ?: return
        viewModelScope.launch { chatMessageDao.clearConversation(cid); touchConversationPreview("") }
        _uiState.value = ChatUiState()
    }

    fun toggleMock() { _adv.update { it.copy(useMock = !it.useMock) }; repo.setUseMock(_adv.value.useMock) }
    fun toggleStreaming() { _adv.update { it.copy(streaming = !it.streaming) } }
    fun updateToken(token: String) { settings.apiToken = token; authProvider.updateToken(token); _token.value = token }
    fun setToken(token: String) = updateToken(token)
    fun currentConversationId(): Long? = _currentConversationId.value
}

data class ChatAdvancedState(
    val useMock: Boolean = false,
    val streaming: Boolean = true,
)
