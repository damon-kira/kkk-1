package com.kira.learning.compose.module.chat

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kira.learning.app.ChatConversation
import kotlinx.coroutines.launch

@Composable
fun ChatRoute(modifier: Modifier = Modifier, viewModel: ChatViewModel = hiltViewModel(), openDrawer: () -> Unit = {}) {
    val state by viewModel.uiState.collectAsState()
    val adv by viewModel.advanced.collectAsState()
    val token by viewModel.token.collectAsState()
    val conversations by viewModel.conversations.collectAsState()
    val snackbar = remember { SnackbarHostState() }
    state.error?.let { err -> LaunchedEffect(err) { snackbar.showSnackbar(err) } }

    ChatScreen(
        state = state,
        adv = adv,
        token = token,
        conversations = conversations,
        currentConversationId = viewModel.currentConversationId(),
        onInput = viewModel::updateInput,
        onSend = viewModel::send,
        onClear = viewModel::reset,
        onToggleMock = viewModel::toggleMock,
        onToggleStreaming = viewModel::toggleStreaming,
        onUpdateToken = viewModel::updateToken,
        onNewConversation = viewModel::startNewConversation,
        onSwitchConversation = viewModel::switchConversation,
        onRenameConversation = viewModel::renameConversation,
        onDeleteConversation = viewModel::deleteConversation,
        snackbarHostState = snackbar,
        modifier = modifier,
        openDrawer = openDrawer
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChatScreen(
    state: ChatUiState,
    adv: ChatAdvancedState,
    token: String,
    conversations: List<ChatConversation>,
    currentConversationId: Long?,
    onInput: (String) -> Unit,
    onSend: () -> Unit,
    onClear: () -> Unit,
    onToggleMock: () -> Unit,
    onToggleStreaming: () -> Unit,
    onUpdateToken: (String) -> Unit,
    onNewConversation: () -> Unit,
    onSwitchConversation: (Long) -> Unit,
    onRenameConversation: (Long, String) -> Unit,
    onDeleteConversation: (Long) -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    openDrawer: () -> Unit = {},
) {
    val clipboard = LocalClipboardManager.current
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var showSettings by remember { mutableStateOf(false) }
    var showConversations by remember { mutableStateOf(false) }
    var showRename by remember { mutableStateOf<Long?>(null) }
    var renameText by remember { mutableStateOf("") }
    var tokenDraft by remember(token) { mutableStateOf(token) }

    val currentTitle = conversations.firstOrNull { it.id == currentConversationId }?.title ?: "AI 聊天"

    fun copyAll() { if (state.messages.isEmpty()) return; clipboard.setText(AnnotatedString(state.messages.joinToString("\n"){ (if(it.isUser)"我:" else "AI:")+it.content })); scope.launch{ snackbarHostState.showSnackbar("已复制全部内容") } }
    fun shareAll() { if (state.messages.isEmpty()) return; val all = state.messages.joinToString("\n\n"){(if(it.isUser)"我:" else "AI:")+it.content}; context.startActivity(Intent(Intent.ACTION_SEND).apply{ type="text/plain"; putExtra(Intent.EXTRA_TEXT,all) }.let{Intent.createChooser(it,"分享会话")}) }
    fun copySingle(text:String){ clipboard.setText(AnnotatedString(text)); scope.launch{ snackbarHostState.showSnackbar("已复制") } }
    fun shareSingle(text:String){ context.startActivity(Intent(Intent.ACTION_SEND).apply{ type="text/plain"; putExtra(Intent.EXTRA_TEXT,text)}.let{Intent.createChooser(it,"分享消息")}) }
    fun clearAll(){ onClear(); scope.launch{ snackbarHostState.showSnackbar("已清空") } }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    if (showConversations) {
        ModalBottomSheet(onDismissRequest = { showConversations = false }, sheetState = sheetState) {
            Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("会话列表", style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
                    IconButton(onClick = { onNewConversation() }) { Icon(Icons.Default.Add, null) }
                }
                if (conversations.isEmpty()) {
                    Text("暂无会话，点击右上 + 创建")
                } else {
                    conversations.forEach { conv ->
                        val selected = conv.id == currentConversationId
                        Surface(
                            tonalElevation = if (selected) 4.dp else 0.dp,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                Modifier.padding(12.dp).clickable { onSwitchConversation(conv.id); showConversations = false },
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(Modifier.weight(1f)) {
                                    Text(conv.title, style = MaterialTheme.typography.bodyMedium, maxLines = 1)
                                    if (conv.lastPreview.isNotBlank()) Text(conv.lastPreview, style = MaterialTheme.typography.bodySmall, maxLines = 1)
                                }
                                IconButton(onClick = { renameText = conv.title; showRename = conv.id }) { Icon(Icons.Default.Edit, null) }
                                IconButton(onClick = { onDeleteConversation(conv.id) }) { Icon(Icons.Default.Close, null) }
                            }
                        }
                    }
                }
                Spacer(Modifier.height(8.dp))
            }
        }
    }

    if (showSettings) {
        AlertDialog(
            onDismissRequest = { showSettings = false },
            confirmButton = {
                TextButton(onClick = { onUpdateToken(tokenDraft); showSettings = false }) { Text("保存") }
            },
            dismissButton = { TextButton(onClick = { showSettings = false }) { Text("关闭") } },
            title = { Text("设置") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Mock模式", Modifier.weight(1f))
                        Switch(checked = adv.useMock, onCheckedChange = { onToggleMock() })
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("流式输出", Modifier.weight(1f))
                        Switch(checked = adv.streaming, onCheckedChange = { onToggleStreaming() })
                    }
                    OutlinedTextField(
                        value = tokenDraft,
                        onValueChange = { tokenDraft = it },
                        label = { Text("API Token") },
                        singleLine = true,
                        supportingText = { if (tokenDraft.isBlank()) Text("留空将使用注解中的默认密钥") }
                    )
                }
            }
        )
    }

    showRename?.let { rid ->
        AlertDialog(
            onDismissRequest = { showRename = null },
            confirmButton = { TextButton(onClick = { onRenameConversation(rid, renameText.ifBlank { "未命名" }); showRename = null }) { Text("确定") } },
            dismissButton = { TextButton(onClick = { showRename = null }) { Text("取消") } },
            title = { Text("重命名会话") },
            text = { OutlinedTextField(value = renameText, onValueChange = { renameText = it }, singleLine = true) }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(currentTitle) },
                navigationIcon = {
                    IconButton(onClick = openDrawer) { Icon(Icons.Default.Menu, contentDescription = "menu") }
                },
                actions = {
                    IconButton(onClick = { showConversations = true }) { Icon(Icons.Default.List, null) }
                    IconButton(onClick = { showSettings = true }) { Icon(Icons.Default.MoreVert, null) }
                    IconButton(onClick = { copyAll() }, enabled = state.messages.isNotEmpty()) { Icon(Icons.Default.ContentCopy, null) }
                    IconButton(onClick = { shareAll() }, enabled = state.messages.isNotEmpty()) { Icon(Icons.Default.Share, null) }
                    IconButton(onClick = { clearAll() }, enabled = state.messages.isNotEmpty() && !state.sending) { Icon(Icons.Default.Delete, null) }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            Row(Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                AssistChip(onClick = { }, label = { Text(if (adv.useMock) "Mock" else "Real") })
                AssistChip(onClick = { }, label = { Text(if (adv.streaming) "Stream" else "Once") })
                if (token.isBlank()) AssistChip(onClick = { showSettings = true }, label = { Text("Token未设") })
            }
        }
    ) { padding ->
        Column(modifier.fillMaxSize().padding(padding)) {
            MessagesList(
                list = state.messages,
                onCopy = ::copySingle,
                onShare = ::shareSingle,
                modifier = Modifier.weight(1f).fillMaxWidth()
            )
            HorizontalDivider()
            InputBar(
                value = state.input,
                onValueChange = onInput,
                onSend = onSend,
                sending = state.sending,
                canSend = state.canSend,
            )
        }
    }
}

@Composable
private fun MessagesList(
    list: List<ChatMessageUi>,
    onCopy: (String) -> Unit,
    onShare: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier.padding(8.dp), reverseLayout = true) {
        items(list.asReversed(), key = { it.id }) { msg ->
            MessageBubble(msg, onCopy, onShare)
            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
private fun MessageBubble(msg: ChatMessageUi, onCopy: (String)->Unit, onShare: (String)->Unit) {
    val bg = if (msg.isUser) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
    val align = if (msg.isUser) Alignment.End else Alignment.Start
    Column(horizontalAlignment = align, modifier = Modifier.fillMaxWidth()) {
        Surface(shape = RoundedCornerShape(12.dp), color = bg, tonalElevation = 1.dp) {
            Column(Modifier.padding(12.dp).widthIn(min = 48.dp, max = 300.dp)) {
                Text(msg.content, style = MaterialTheme.typography.bodyMedium)
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    if (!msg.pending) {
                        TextButton(onClick = { onCopy(msg.content) }) { Text("复制", style = MaterialTheme.typography.labelSmall) }
                        TextButton(onClick = { onShare(msg.content) }) { Text("分享", style = MaterialTheme.typography.labelSmall) }
                    } else {
                        Text("生成中...", style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
        }
    }
}

@Composable
private fun InputBar(
    value: String,
    onValueChange: (String)->Unit,
    onSend: () -> Unit,
    sending: Boolean,
    canSend: Boolean,
) {
    Row(Modifier.fillMaxWidth().padding(8.dp), verticalAlignment = Alignment.Bottom) {
        OutlinedTextField(
            value = value,
            onValueChange = { if (!sending) onValueChange(it) },
            modifier = Modifier.weight(1f),
            placeholder = { Text(if (sending) "请等待 AI 回复" else "输入消息...") },
            maxLines = 6,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
            keyboardActions = KeyboardActions(onSend = { if (canSend) onSend() })
        )
        Spacer(Modifier.width(8.dp))
        FilledIconButton(onClick = onSend, enabled = canSend) {
            Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null)
        }
    }
}
