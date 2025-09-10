package com.kira.learning.modules.chat

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.kira.learning.di.ChatConversation
import com.kira.learning.models.ChatMessageUi
import com.kira.learning.models.ChatUiState
import kotlinx.coroutines.launch

@Composable
fun rememberChatController(viewModel: ChatViewModel = hiltViewModel()): ChatController {
    val state by viewModel.uiState.collectAsState()
    val adv by viewModel.advanced.collectAsState()
    val token by viewModel.token.collectAsState()
    val conversations by viewModel.conversations.collectAsState()
    return remember(state, adv, token, conversations) {
        ChatController(
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
        )
    }
}

data class ChatController(
    val state: ChatUiState,
    val adv: ChatAdvancedState,
    val token: String,
    val conversations: List<ChatConversation>,
    val currentConversationId: Long?,
    val onInput: (String) -> Unit,
    val onSend: () -> Unit,
    val onClear: () -> Unit,
    val onToggleMock: () -> Unit,
    val onToggleStreaming: () -> Unit,
    val onUpdateToken: (String) -> Unit,
    val onNewConversation: () -> Unit,
    val onSwitchConversation: (Long) -> Unit,
    val onRenameConversation: (Long, String) -> Unit,
    val onDeleteConversation: (Long) -> Unit,
)

// 原 ChatRoute 简化使用 ChatScaffold
@Composable
fun ChatRoute(
    modifier: Modifier = Modifier,
    viewModel: ChatViewModel = hiltViewModel(),
    openDrawer: () -> Unit = {}
) {
    val controller = rememberChatController(viewModel)
    val snackbar = remember { SnackbarHostState() }
    controller.state.error?.let { err -> LaunchedEffect(err) { snackbar.showSnackbar(err) } }
    ChatScaffold(
        controller = controller,
        snackbarHostState = snackbar,
        modifier = modifier,
        openDrawer = openDrawer
    )
}

/** 可嵌入任意容器的最小聊天面板：只含消息列表与输入框，不含顶栏/底状态/对话管理 */
@Composable
fun ChatPanel(
    controller: ChatController,
    modifier: Modifier = Modifier,
    onCopyMessage: (String) -> Unit = {},
    onShareMessage: (String) -> Unit = {},
    onCopyAll: (() -> Unit)? = null,
    onShareAll: (() -> Unit)? = null,
    showDividerAboveInput: Boolean = true,
) {
    Column(modifier.fillMaxSize()) {
        MessagesList(
            list = controller.state.messages,
            onCopy = onCopyMessage,
            onShare = onShareMessage,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        )
        if (showDividerAboveInput) HorizontalDivider()
        InputBar(
            value = controller.state.input,
            onValueChange = controller.onInput,
            onSend = controller.onSend,
            sending = controller.state.sending,
            canSend = controller.state.canSend,
        )
    }
}

/** 全功能 Scaffold（含会话列表、设置、顶栏、底部状态）。 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScaffold(
    controller: ChatController,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    modifier: Modifier = Modifier,
    openDrawer: () -> Unit = {},
    isDialog: Boolean = false,
) {
    val clipboard = LocalClipboardManager.current
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var showSettings by remember { mutableStateOf(false) }
    var showConversations by remember { mutableStateOf(false) }
    var showRename by remember { mutableStateOf<Long?>(null) }
    var renameText by remember { mutableStateOf("") }
    var tokenDraft by remember(controller.token) { mutableStateOf(controller.token) }

    val currentTitle =
        controller.conversations.firstOrNull { it.id == controller.currentConversationId }?.title
            ?: "AI 聊天"

    fun copyAll() {
        if (controller.state.messages.isEmpty()) return
        clipboard.setText(AnnotatedString(controller.state.messages.joinToString("\n") { (if (it.isUser) "我:" else "AI:") + it.content }))
        scope.launch { snackbarHostState.showSnackbar("已复制全部内容") }
    }

    fun shareAll() {
        if (controller.state.messages.isEmpty()) return
        val all = controller.state.messages.joinToString("\n\n") { (if (it.isUser) "我:" else "AI:") + it.content }
        val sendIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, all)
        }
        context.startActivity(Intent.createChooser(sendIntent, "分享会话"))
    }

    fun clearAll() {
        controller.onClear(); scope.launch { snackbarHostState.showSnackbar("已清空") }
    }

    fun copySingle(text: String) {
        clipboard.setText(AnnotatedString(text)); scope.launch { snackbarHostState.showSnackbar("已复制") }
    }

    fun shareSingle(text: String) {
        val sendIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
        }
        context.startActivity(Intent.createChooser(sendIntent, "分享消息"))
    }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    if (showConversations) {
        ModalBottomSheet(
            onDismissRequest = { showConversations = false },
            sheetState = sheetState
        ) {
            ConversationSheet(
                conversations = controller.conversations,
                currentConversationId = controller.currentConversationId,
                onNewConversation = controller.onNewConversation,
                onSwitchConversation = {
                    controller.onSwitchConversation(it); showConversations = false
                },
                onRename = { id, title -> renameText = title; showRename = id },
                onDelete = controller.onDeleteConversation,
            )
        }
    }

    if (showSettings) {
        SettingsDialog(
            show = showSettings,
            onDismiss = { showSettings = false },
            adv = controller.adv,
            tokenDraft = tokenDraft,
            onTokenDraftChange = { tokenDraft = it },
            onSave = { controller.onUpdateToken(tokenDraft); showSettings = false },
            onToggleMock = controller.onToggleMock,
            onToggleStreaming = controller.onToggleStreaming,
        )
    }

    showRename?.let { rid ->
        RenameDialog(
            id = rid,
            name = renameText,
            onNameChange = { renameText = it },
            onConfirm = {
                controller.onRenameConversation(
                    rid,
                    renameText.ifBlank { "未命名" }); showRename = null
            },
            onDismiss = { showRename = null }
        )
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(currentTitle) },
                navigationIcon = {
                    IconButton(onClick = openDrawer) {
                        Icon(
                            if (isDialog) Icons.Default.Close else Icons.Default.Menu,
                            null
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { showConversations = true }) {
                        Icon(
                            Icons.Default.List,
                            null
                        )
                    }
                    IconButton(onClick = { showSettings = true }) {
                        Icon(
                            Icons.Default.MoreVert,
                            null
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AssistChip(
                    onClick = {},
                    label = { Text(if (controller.adv.useMock) "Mock" else "Real") })
                AssistChip(
                    onClick = {},
                    label = { Text(if (controller.adv.streaming) "Stream" else "Once") })
                if (controller.token.isBlank()) AssistChip(
                    onClick = { showSettings = true },
                    label = { Text("Token未设") })
            }
        }
    ) { padding ->
        Column(Modifier
            .fillMaxSize()
            .padding(padding)) {
            MessagesList(
                list = controller.state.messages,
                onCopy = ::copySingle,
                onShare = ::shareSingle,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )
            HorizontalDivider()
            InputBar(
                value = controller.state.input,
                onValueChange = controller.onInput,
                onSend = controller.onSend,
                sending = controller.state.sending,
                canSend = controller.state.canSend,
            )
        }
    }
}

/** 可在任意地方以全屏 Dialog 的形式打开聊天 */
@Composable
fun ChatDialog(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    controller: ChatController = rememberChatController(),
    usePlatformDefaultWidth: Boolean = false,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    controller.state.error?.let { err -> LaunchedEffect(err) { snackbarHostState.showSnackbar(err) } }
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = usePlatformDefaultWidth)
    ) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            tonalElevation = 4.dp,
            modifier = Modifier.fillMaxSize()
        ) {
            ChatScaffold(
                controller = controller,
                snackbarHostState = snackbarHostState,
                modifier = modifier,
                openDrawer = { onDismiss() },
                isDialog = true,
            )
        }
    }
}

@Composable
private fun ConversationSheet(
    conversations: List<ChatConversation>,
    currentConversationId: Long?,
    onNewConversation: () -> Unit,
    onSwitchConversation: (Long) -> Unit,
    onRename: (Long, String) -> Unit,
    onDelete: (Long) -> Unit,
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                "会话列表",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = onNewConversation) { Icon(Icons.Default.Add, null) }
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
                        Modifier
                            .padding(12.dp)
                            .clickable { onSwitchConversation(conv.id) },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(Modifier.weight(1f)) {
                            Text(
                                conv.title,
                                style = MaterialTheme.typography.bodyMedium,
                                maxLines = 1
                            )
                            if (conv.lastPreview.isNotBlank()) Text(
                                conv.lastPreview,
                                style = MaterialTheme.typography.bodySmall,
                                maxLines = 1
                            )
                        }
                        IconButton(onClick = {
                            onRename(
                                conv.id,
                                conv.title
                            )
                        }) { Icon(Icons.Default.Edit, null) }
                        IconButton(onClick = { onDelete(conv.id) }) {
                            Icon(
                                Icons.Default.Close,
                                null
                            )
                        }
                    }
                }
            }
        }
        Spacer(Modifier.height(8.dp))
    }
}

@Composable
private fun SettingsDialog(
    show: Boolean,
    onDismiss: () -> Unit,
    adv: ChatAdvancedState,
    tokenDraft: String,
    onTokenDraftChange: (String) -> Unit,
    onSave: () -> Unit,
    onToggleMock: () -> Unit,
    onToggleStreaming: () -> Unit,
) {
    if (!show) return
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = { TextButton(onClick = onSave) { Text("保存") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("关闭") } },
        title = { Text("设置") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Mock模式", Modifier.weight(1f)); Switch(
                    checked = adv.useMock,
                    onCheckedChange = { onToggleMock() })
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("流式输出", Modifier.weight(1f)); Switch(
                    checked = adv.streaming,
                    onCheckedChange = { onToggleStreaming() })
                }
                OutlinedTextField(
                    value = tokenDraft,
                    onValueChange = onTokenDraftChange,
                    label = { Text("API Token") },
                    singleLine = true,
                    supportingText = { if (tokenDraft.isBlank()) Text("留空将使用注解中的默认密钥") })
            }
        }
    )
}

@Composable
private fun RenameDialog(
    id: Long,
    name: String,
    onNameChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = { TextButton(onClick = onConfirm) { Text("确定") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("取消") } },
        title = { Text("重命名会话") },
        text = { OutlinedTextField(value = name, onValueChange = onNameChange, singleLine = true) }
    )
}

// 原 MessageList / MessageBubble / InputBar 保留在文件底部
@Composable
private fun MessagesList(
    list: List<ChatMessageUi>,
    onCopy: (String) -> Unit,
    onShare: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    // reverseLayout = true 下：index=0 永远是最新消息（因为我们传入 list.asReversed()）
    val listState = rememberLazyListState()
    // 监听最新消息 id + 内容（流式 pending 内容更新时 content 会变化）
    val newestKey = list.lastOrNull()?.let { it.id to it.content }
    LaunchedEffect(newestKey) {
        // 保持最新显示；若后续需要尊重用户手动滚动，可加判断 listState.firstVisibleItemIndex == 0
        listState.scrollToItem(0)
    }
    LazyColumn(
        state = listState, modifier = modifier.padding(8.dp), reverseLayout = true
    ) {
        items(list.asReversed(), key = { it.id }) { msg ->
            MessageBubble(msg, onCopy, onShare)
            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
private fun MessageBubble(msg: ChatMessageUi, onCopy: (String) -> Unit, onShare: (String) -> Unit) {
    // 根据 isUser 动态决定对齐与背景色
    val bg =
        if (msg.isUser) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
    val align = if (msg.isUser) Alignment.End else Alignment.Start
    Column(horizontalAlignment = align, modifier = Modifier.fillMaxWidth()) {
        Surface(shape = RoundedCornerShape(12.dp), color = bg, tonalElevation = 1.dp) {
            Column(
                Modifier
                    .padding(12.dp)
                    .widthIn(min = 48.dp, max = 300.dp)
            ) {
                Text(msg.content, style = MaterialTheme.typography.bodyMedium)
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    if (!msg.pending) {
                        TextButton(onClick = { onCopy(msg.content) }) {
                            Text(
                                "复制", style = MaterialTheme.typography.labelSmall
                            )
                        }
                        TextButton(onClick = { onShare(msg.content) }) {
                            Text(
                                "分享", style = MaterialTheme.typography.labelSmall
                            )
                        }
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
    onValueChange: (String) -> Unit,
    onSend: () -> Unit,
    sending: Boolean,
    canSend: Boolean,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(8.dp), verticalAlignment = Alignment.Bottom
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = { if (!sending) onValueChange(it) }, // 发送中禁止修改，避免状态抖动
            modifier = Modifier.weight(1f),
            placeholder = { Text(if (sending) "请等待 AI 回复" else "输入消息...") },
            maxLines = 6,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
            keyboardActions = KeyboardActions(onSend = { if (canSend) onSend() }) // 软键盘回车即发送
        )
        Spacer(Modifier.width(8.dp))
        FilledIconButton(onClick = onSend, enabled = canSend) {
            Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null)
        }
    }
}
