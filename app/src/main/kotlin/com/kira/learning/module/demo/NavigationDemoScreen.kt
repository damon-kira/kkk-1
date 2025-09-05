package com.kira.learning.module.demo

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
import com.kira.learning.xml.Launch

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

    var floatingShowing by remember { mutableStateOf(false) }

    // 搜索关键字
    var query by remember { mutableStateOf("") }

    // Crash 确认弹窗
    var showCrashConfirm by remember { mutableStateOf(false) }


    val allItems = remember {
        listOf(
            DemoItem("Python Run(废弃)", action = { Launch.skipCodingActivity(context) }),
            DemoItem("Video Player", action = { Launch.skipPlayerManageActivity(context) }),
            DemoItem("Crash Test", action = { showCrashConfirm = true }),
            DemoItem("Step Bar", action = { Launch.skipStepBarViewActivity(context) }),
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