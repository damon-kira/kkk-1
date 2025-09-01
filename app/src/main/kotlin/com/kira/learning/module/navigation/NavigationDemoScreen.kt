package com.kira.learning.module.navigation

import android.app.Activity
import android.widget.TextView
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.kira.learning.manager.Launch
import com.kira.learning.xml.expand.setLogout
import com.common.lib.livedata.LiveDataBus
import com.kira.learning.xml.modules.home.HomeEvent
import com.kira.learning.xml.modules.home.MainEvent
import com.hjq.window.EasyWindow
import com.hjq.window.OnWindowViewClickListener
import com.hjq.window.draggable.MovingWindowDraggableRule
import com.kira.learning.R

// 顶级定义，供 DemoRow 与页面共享
private data class DemoItem(
    val title: String,
    val enabled: Boolean = true,
    val keyword: String = title.lowercase(),
    val action: () -> Unit
)

/**
 * Compose 版 Navigation 示例页，复刻 NavigationFragment.initViewSetting 功能
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationDemoRoute(
    modifier: Modifier = Modifier,
    openDrawer: () -> Unit = {}
) {
    val context = LocalContext.current

    // 浮窗 EasyWindow
    val easyWindow by remember {
        mutableStateOf(
            (context as? Activity)?.let { act ->
                EasyWindow.with(act)
                    .setContentView(R.layout.window_hint)
                    .setWindowDraggableRule(MovingWindowDraggableRule())
                    .setOnClickListenerByView(
                        R.id.win_tv_message,
                        object : OnWindowViewClickListener<TextView> {
                            override fun onClick(easyWindow: EasyWindow<*>, view: TextView) {
                                // 点击浮窗占位逻辑
                            }
                        }
                    )
            }
        )
    }
    var floatingShowing by remember { mutableStateOf(false) }

    // 搜索关键字
    var query by remember { mutableStateOf("") }

    // Crash 确认弹窗
    var showCrashConfirm by remember { mutableStateOf(false) }
    // Bus 注销确认弹窗
    var showLogoutConfirm by remember { mutableStateOf(false) }

    // 在组合中注册窗口释放
    FloatingWindowLifecycleDisposer(easyWindow)

    val allItems = remember {
        listOf(
            DemoItem("Question Bank", action = { Launch.skipAnswerActivity(context) }),
            DemoItem("Python Run(废弃)", action = { Launch.skipCodingActivity(context) }),
            DemoItem("AI Chat(废弃)", action = { Launch.skipAIIMActivity(context) }),
            DemoItem("Chat (废弃)", action = { Launch.skipChatActivity(context) }),
            DemoItem("Video Player", action = { Launch.skipPlayerManageActivity(context) }),
            DemoItem("Activities Quiz", action = { Launch.skipQuizActivity(context) }),
            DemoItem("Upload(废弃)", enabled = false, action = { }),
            DemoItem("Logout", action = { showLogoutConfirm = true }),
            DemoItem("Floating Window", action = {
                if (floatingShowing) {
                    easyWindow?.cancel(); floatingShowing = false
                } else {
                    easyWindow?.show(); floatingShowing = true
                }
            }),
            DemoItem("Crash Test", action = { showCrashConfirm = true }),
            DemoItem("Image Zoom(正在迁移)", action = { Launch.skipZoomImageActivity(context) }),
            DemoItem("Step Bar", action = { Launch.skipStepBarViewActivity(context) }),
            DemoItem(
                "WebView Embed",
                action = {
                    Launch.skipWebViewActivity(
                        context,
                        "https://onecompiler.com/embed?language=python"
                    )
                }),
            DemoItem("Code Playground", action = { Launch.skipCodePlaygroundActivity(context) }),
            DemoItem("Super Editor", action = { Launch.skipSuperEditorActivity(context) }),
            DemoItem("AI Chat", action = { Launch.skipAiChatActivity(context) }),
        )
    }

    val demoItems = remember(query, allItems) {
        if (query.isBlank()) allItems else allItems.filter {
            it.keyword.contains(
                query.trim().lowercase()
            )
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("导航示例") },
                navigationIcon = {
                    IconButton(onClick = openDrawer) {
                        Icon(
                            Icons.Filled.Menu,
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    if (easyWindow != null) {
                        TextButton(onClick = {
                            if (floatingShowing) {
                                easyWindow?.cancel(); floatingShowing = false
                            } else {
                                easyWindow?.show(); floatingShowing = true
                            }
                        }) { Text(if (floatingShowing) "隐藏浮窗" else "浮窗") }
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                leadingIcon = { Icon(Icons.Filled.Search, null) },
                singleLine = true,
                label = { Text("搜索 Demo") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            )
            if (demoItems.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        "无匹配项",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            } else {
                LazyColumn(Modifier.fillMaxSize()) {
                    items(demoItems) { item ->
                        DemoRow(item)
                        HorizontalDivider()
                    }
                    item { Spacer(Modifier.height(32.dp)) }
                }
            }
        }
    }

    if (showCrashConfirm) {
        AlertDialog(
            onDismissRequest = { showCrashConfirm = false },
            confirmButton = {
                TextButton(onClick = {
                    showCrashConfirm =
                        false; throw RuntimeException("${System.currentTimeMillis()} 测试崩溃")
                }) { Text("继续") }
            },
            dismissButton = { TextButton(onClick = { showCrashConfirm = false }) { Text("取消") } },
            title = { Text("确认崩溃测试") },
            text = { Text("此操作将立即触发应用崩溃，用于测试崩溃捕获。确认继续？") }
        )
    }

    if (showLogoutConfirm) {
        AlertDialog(
            onDismissRequest = { showLogoutConfirm = false },
            confirmButton = {
                TextButton(onClick = {
                    showLogoutConfirm = false
                    setLogout()
                    LiveDataBus.post(HomeEvent(HomeEvent.EVENT_LOGOUT))
                    LiveDataBus.post(MainEvent(MainEvent.EVENT_SHOW_HOME))
                }) { Text("退出") }
            },
            dismissButton = {
                TextButton(onClick = {
                    showLogoutConfirm = false
                }) { Text("取消") }
            },
            title = { Text("确认退出") },
            text = { Text("将注销并发送相关事件，确认继续？") }
        )
    }
}

@Composable
private fun DemoRow(item: DemoItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(item.title, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
        Button(
            onClick = item.action,
            enabled = item.enabled
        ) { Text(if (item.enabled) "进入" else "待实现") }
    }
}

// 释放浮窗资源
@Composable
private fun FloatingWindowLifecycleDisposer(easyWindow: EasyWindow<*>?) {
    DisposableEffect(easyWindow) {
        onDispose { easyWindow?.recycle() }
    }
}
