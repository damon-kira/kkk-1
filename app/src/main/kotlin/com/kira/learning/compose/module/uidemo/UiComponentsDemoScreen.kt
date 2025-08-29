package com.kira.learning.compose.module.uidemo

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import com.common.kira.ui.divider.HorizontalDivider as KHorizontalDivider
import com.common.kira.ui.preference.Preference
import com.common.kira.ui.preference.SwitchPreference
import com.common.kira.ui.preference.ListPreference
import com.common.kira.ui.preference.ListSelection
import com.common.kira.ui.SquircleTheme
import com.common.kira.ui.radio.Radio
import com.common.kira.ui.button.TextButton
import com.common.kira.ui.switcher.Switcher
import com.common.kira.ui.radio.RadioStyleDefaults

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UiComponentsDemoRoute(
    modifier: Modifier = Modifier,
    openDrawer: () -> Unit = {}
) {
    var darkTheme by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("UI组件演示") },
                navigationIcon = { IconButton(onClick = openDrawer) { Icon(Icons.Default.Menu, null) } },
                actions = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(if (darkTheme) "暗" else "亮", style = MaterialTheme.typography.labelSmall)
                        Switch(checked = darkTheme, onCheckedChange = { darkTheme = it })
                    }
                }
            )
        }
    ) { padding ->
        SquircleTheme(darkTheme = darkTheme) { // 使用 common-ui 主题并可切换
            ComponentList(
                modifier = modifier
                    .padding(padding)
                    .fillMaxSize(),
                darkThemeState = darkTheme,
                onToggleTheme = { darkTheme = !darkTheme }
            )
        }
    }
}

private data class DemoEntry(
    val group: String,
    val title: String,
    val content: @Composable () -> Unit
)

private data class TypographySample(val label: String, val render: @Composable () -> Unit)

@Composable
private fun ComponentList(
    modifier: Modifier = Modifier,
    darkThemeState: Boolean,
    onToggleTheme: () -> Unit
) {
    var switch1 by remember { mutableStateOf(true) }
    var switch2 by remember { mutableStateOf(false) }

    var listSelected by remember { mutableStateOf("light") }

    var radioSelected by remember { mutableStateOf("A") }
    var showAlert by remember { mutableStateOf(false) }

    var listSelectionValue by remember { mutableStateOf("opt1") }

    // 新增：搜索关键字
    var query by remember { mutableStateOf("") }
    // 新增：分组展开状态（初始全部展开）
    val groupExpandState = remember { mutableStateMapOf<String, Boolean>() }

    // Typography 列表
    val typographyShowcase = remember {
        listOf(
            TypographySample("text12Regular") { Text("text12Regular 文本示例", style = SquircleTheme.typography.text12Regular) },
            TypographySample("text14Regular") { Text("text14Regular 文本示例", style = SquircleTheme.typography.text14Regular) },
            TypographySample("text14Medium") { Text("text14Medium 文本示例", style = SquircleTheme.typography.text14Medium) },
            TypographySample("text14Bold") { Text("text14Bold 文本示例", style = SquircleTheme.typography.text14Bold) },
            TypographySample("text16Regular") { Text("text16Regular 文本示例", style = SquircleTheme.typography.text16Regular) },
            TypographySample("text16Medium") { Text("text16Medium 文本示例", style = SquircleTheme.typography.text16Medium) },
            TypographySample("text16Bold") { Text("text16Bold 文本示例", style = SquircleTheme.typography.text16Bold) },
            TypographySample("text18Regular") { Text("text18Regular 文本示例", style = SquircleTheme.typography.text18Regular) },
            TypographySample("text18Medium") { Text("text18Medium 文本示例", style = SquircleTheme.typography.text18Medium) },
            TypographySample("text18Bold") { Text("text18Bold 文本示例", style = SquircleTheme.typography.text18Bold) },
            TypographySample("text20Regular") { Text("text20Regular 文本示例", style = SquircleTheme.typography.text20Regular) },
            TypographySample("text20Medium") { Text("text20Medium 文本示例", style = SquircleTheme.typography.text20Medium) },
            TypographySample("header20Bold") { Text("header20Bold 标题示例", style = SquircleTheme.typography.header20Bold) },
        )
    }

    // 颜色展示
    @Composable
    fun ColorRow(name: String, color: androidx.compose.ui.graphics.Color) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                Modifier
                    .size(48.dp, 28.dp)
                    .background(color, RoundedCornerShape(4.dp))
            )
            Spacer(Modifier.width(12.dp))
            Text(name, style = SquircleTheme.typography.text14Regular)
        }
    }

    val colorEntries: List<Pair<String, androidx.compose.ui.graphics.Color>> = listOf(
        "colorPrimary" to SquircleTheme.colors.colorPrimary,
        "colorOutline" to SquircleTheme.colors.colorOutline,
        "bgPrimary" to SquircleTheme.colors.colorBackgroundPrimary,
        "bgSecondary" to SquircleTheme.colors.colorBackgroundSecondary,
        "bgTertiary" to SquircleTheme.colors.colorBackgroundTertiary,
        "textPrimary" to SquircleTheme.colors.colorTextAndIconPrimary,
        "textPrimaryInverse" to SquircleTheme.colors.colorTextAndIconPrimaryInverse,
        "textSecondary" to SquircleTheme.colors.colorTextAndIconSecondary,
        "textDisabled" to SquircleTheme.colors.colorTextAndIconDisabled,
        "textAdditional" to SquircleTheme.colors.colorTextAndIconAdditional,
        "textSuccess" to SquircleTheme.colors.colorTextAndIconSuccess,
        "textError" to SquircleTheme.colors.colorTextAndIconError,
    )

    val listEntries = arrayOf("浅色", "深色", "跟随系统")
    val listValues = arrayOf("light", "dark", "system")

    val entries = remember(darkThemeState, switch1, switch2, listSelected, radioSelected, listSelectionValue) {
        listOf(
            DemoEntry("Theme", "主题切换 & 状态") {
                Preference(title = "当前主题", subtitle = if (darkThemeState) "暗色" else "亮色")
                Switcher(title = "切换主题 (内部) ", checked = darkThemeState, onClick = onToggleTheme)
            },
            DemoEntry("Colors", "色板") {
                Column { colorEntries.forEach { ColorRow(it.first, it.second) } }
            },
            DemoEntry("Typography", "字体样式") {
                Column(Modifier.padding(12.dp)) { typographyShowcase.forEach { it.render() } }
            },
            DemoEntry("Divider", "HorizontalDivider") { KHorizontalDivider() },
            DemoEntry("Preference", "基础单行") { Preference(title = "基础项", subtitle = "副标题") },
            DemoEntry("Preference", "禁用状态") { Preference(title = "禁用项", subtitle = "无法点击", enabled = false) },
            DemoEntry("Preference", "带底部内容") {
                Preference(title = "多行扩展", subtitle = "显示底部块") {
                    Spacer(Modifier.height(6.dp))
                    Text("这里是 bottomContent 扩展区域", style = SquircleTheme.typography.text14Regular)
                }
            },
            DemoEntry("SwitchPreference", "开关(已开)") {
                SwitchPreference(
                    title = "推送通知",
                    subtitle = "是否接收推送",
                    checked = switch1,
                    onCheckedChange = { switch1 = it }
                )
            },
            DemoEntry("SwitchPreference", "开关(已关)") {
                SwitchPreference(
                    title = "声音",
                    subtitle = "是否开启声音",
                    checked = switch2,
                    onCheckedChange = { switch2 = it }
                )
            },
            DemoEntry("ListPreference", "选择主题") {
                ListPreference(
                    title = "主题模式",
                    subtitle = "当前: ${listSelected}",
                    entries = listEntries,
                    entryValues = listValues,
                    selectedValue = listSelected,
                    onValueSelected = { listSelected = it },
                    entryNameAsSubtitle = true,
                )
            },
            DemoEntry("ListSelection", "原始单选列表") {
                Column(Modifier.padding(horizontal = 12.dp)) {
                    listOf("opt1" to "选项一", "opt2" to "选项二", "opt3" to "选项三").forEach { (v, label) ->
                        ListSelection(
                            title = label,
                            selected = listSelectionValue == v,
                            onClick = { listSelectionValue = v }
                        )
                    }
                }
            },
            DemoEntry("Switcher", "单独 Switcher") {
                Switcher(
                    title = "单独展示 Switcher",
                    checked = switch1,
                    onClick = { switch1 = !switch1 }
                )
            },
            DemoEntry("Switcher", "禁用示例") {
                Row(Modifier.padding(start = 16.dp)) {
                    Switcher(title = "已开/禁用", checked = true, enabled = false)
                    Spacer(Modifier.width(12.dp))
                    Switcher(title = "已关/禁用", checked = false, enabled = false)
                }
            },
            DemoEntry("Radio", "单选按钮组") {
                Row(Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    listOf("A","B","C").forEach { opt ->
                        Radio(
                            title = opt,
                            checked = radioSelected == opt,
                            onClick = { radioSelected = opt },
                            modifier = Modifier.padding(end = 8.dp),
                            radioStyle = RadioStyleDefaults.Primary
                        )
                    }
                }
            },
            DemoEntry("Radio", "禁用状态") {
                Row(Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    Radio(title = "选中", checked = true, enabled = false)
                    Spacer(Modifier.width(8.dp))
                    Radio(title = "未选中", checked = false, enabled = false)
                }
            },
            DemoEntry("Button", "TextButton") {
                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    TextButton(text = "主要操作", onClick = { /* TODO */ })
                    Spacer(Modifier.width(12.dp))
                    TextButton(text = "禁用", enabled = false, onClick = {})
                }
            },
            DemoEntry("Dialog", "AlertDialog 触发") {
                Preference(title = "打开自定义弹窗", subtitle = "演示 AlertDialog", onClick = { showAlert = true })
            }
        )
    }

    // 过滤逻辑
    val filteredEntries = remember(query, entries) {
        if (query.isBlank()) entries else entries.filter { it.group.contains(query, true) || it.title.contains(query, true) }
    }

    // 初始化组展开状态（首次或当新增组出现）
    LaunchedEffect(filteredEntries) {
        filteredEntries.map { it.group }.distinct().forEach { g ->
            if (g !in groupExpandState) groupExpandState[g] = true
        }
    }

    val grouped = filteredEntries.groupBy { it.group }
    val forceExpandAll = query.isNotBlank()

    if (showAlert) {
        com.common.kira.ui.dialog.AlertDialog(
            title = "演示对话框",
            content = {
                Text(
                    text = "这是来自 common-ui 的 AlertDialog 组件。可用于展示说明、二次确认等。",
                    style = SquircleTheme.typography.text16Regular,
                    color = SquircleTheme.colors.colorTextAndIconSecondary
                )
            },
            confirmButton = "确定",
            dismissButton = "取消",
            onConfirmClicked = { showAlert = false },
            onDismissClicked = { showAlert = false },
            onDismiss = { showAlert = false }
        )
    }

    Column(modifier) {
        // 搜索框
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            singleLine = true,
            label = { Text("搜索组件 / 分组") },
            trailingIcon = { if (query.isNotEmpty()) TextButton(text = "清除", onClick = { query = "" }) }
        )
        // 列表
        LazyColumn(Modifier.fillMaxSize()) {
            grouped.forEach { (group, itemsInGroup) ->
                val expanded = forceExpandAll || groupExpandState[group] == true
                item(key = group + "__header") {
                    GroupHeader(
                        text = group,
                        expanded = expanded,
                        onToggle = {
                            if (!forceExpandAll) {
                                groupExpandState[group] = !(groupExpandState[group] ?: true)
                            }
                        },
                        count = itemsInGroup.size,
                        forceExpand = forceExpandAll
                    )
                }
                if (expanded) {
                    items(itemsInGroup, key = { it.group + it.title }) { entry ->
                        Column(Modifier.fillMaxWidth()) {
                            entry.content()
                            KHorizontalDivider(modifier = Modifier.padding(start = 16.dp))
                        }
                    }
                }
            }
            item { Spacer(Modifier.height(42.dp)) }
        }
    }
}

@Composable
private fun GroupHeader(
    text: String,
    expanded: Boolean,
    onToggle: () -> Unit,
    count: Int,
    forceExpand: Boolean
) {
    Surface(tonalElevation = 1.dp) {
        Row(
            Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (!forceExpand) {
                IconButton(onClick = onToggle, modifier = Modifier.size(24.dp)) {
                    Icon(if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore, null)
                }
                Spacer(Modifier.width(4.dp))
            }
            Text(
                text = "$text ($count)",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                modifier = Modifier.weight(1f)
            )
            if (forceExpand) {
                Text("筛选结果", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}
