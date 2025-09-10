package com.kira.learning.module.coding

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.FormatListBulleted
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kira.learning.base.components.StateHandler
import com.kira.learning.base.mvi.BaseUiState
import com.kira.learning.base.mvi.UiEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CodingEditorRoute(
    modifier: Modifier = Modifier,
    viewModel: CodingViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit = {}
) {
    val state by viewModel.viewState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    // 收集事件
    LaunchedEffect(viewModel) {
        viewModel.viewEvent.collect { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(event.message)
                }
                else -> {}
            }
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("代码编辑器") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回")
                    }
                },
                actions = {
                    // 语言选择
                    var expanded by remember { mutableStateOf(false) }
                    Box {
                        TextButton(onClick = { expanded = true }) {
                            Text(state.selectedLanguage.displayName)
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            ProgrammingLanguage.entries.forEach { language ->
                                DropdownMenuItem(
                                    text = { Text(language.displayName) },
                                    onClick = {
                                        viewModel.handleAction(CodingEvent.ChangeLanguage(language))
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }

                    // 行号切换
                    IconButton(
                        onClick = { viewModel.handleAction(CodingEvent.ToggleLineNumbers) }
                    ) {
                        Icon(
                            if (state.showLineNumbers) Icons.Default.FormatListNumbered
                            else Icons.AutoMirrored.Filled.FormatListBulleted,
                            contentDescription = "切换行号"
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // 代码编辑器区域
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                CodeEditor(
                    code = state.code,
                    language = state.selectedLanguage,
                    showLineNumbers = state.showLineNumbers,
                    onCodeChange = { viewModel.handleAction(CodingEvent.UpdateCode(it)) },
                    modifier = Modifier.fillMaxSize()
                )
            }

            // 控制栏
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { viewModel.handleAction(CodingEvent.ExecuteCode) },
                    enabled = !state.isExecuting,
                    modifier = Modifier.weight(1f)
                ) {
                    if (state.isExecuting) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    } else {
                        Icon(Icons.Default.PlayArrow, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Text(if (state.isExecuting) "运行中..." else "运行代码")
                }

                if (state.executionResult !is BaseUiState.Idle) {
                    OutlinedButton(
                        onClick = { viewModel.handleAction(CodingEvent.ClearResult) }
                    ) {
                        Icon(Icons.Default.Clear, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("清空")
                    }
                }
            }

            // 执行结果区域
            if (state.executionResult !is BaseUiState.Idle) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = "执行结果",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(16.dp)
                        )

                        StateHandler(
                            state = state.executionResult,
                            onRetry = { viewModel.handleAction(CodingEvent.ExecuteCode) }
                        ) { result, stateModifier ->
                            ExecutionResultContent(
                                result = result,
                                userInput = state.userInput,
                                onInputChange = { viewModel.handleAction(CodingEvent.UpdateUserInput(it)) },
                                modifier = stateModifier.fillMaxSize()
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CodeEditor(
    code: String,
    language: ProgrammingLanguage,
    showLineNumbers: Boolean,
    onCodeChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val horizontalScrollState = rememberScrollState()
    val verticalScrollState = rememberScrollState()

    // 计算行高和字体样式
    val textStyle = TextStyle(
        fontFamily = FontFamily.Monospace,
        fontSize = 14.sp,
        color = MaterialTheme.colorScheme.onSurface,
        lineHeight = 20.sp // 固定行高确保对齐
    )

    val lineNumberStyle = TextStyle(
        fontFamily = FontFamily.Monospace,
        fontSize = 14.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        lineHeight = 20.sp // 与代码区域相同的行高
    )

    // 生成语法高亮的代码
    val highlightedCode = SyntaxHighlighter.highlightCode(code, language)

    Row(modifier = modifier) {
        // 行号显示
        if (showLineNumbers) {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(start = 8.dp, end = 8.dp, top = 24.dp, bottom = 16.dp)
                    .verticalScroll(verticalScrollState)
            ) {
                val lineCount = code.count { it == '\n' } + 1
                repeat(lineCount) { index ->
                    Text(
                        text = "${index + 1}",
                        style = lineNumberStyle,
                        modifier = Modifier
                            .height(20.dp)
                            .padding(vertical = 0.dp)
                    )
                }
            }

            VerticalDivider(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp)
            )
        }

        // 代码编辑区域 - 使用双层显示（编辑层 + 高亮层）
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // 背景层 - 显示语法高亮
            if (code.isNotEmpty()) {
                Text(
                    text = highlightedCode,
                    style = textStyle,
                    modifier = Modifier
                        .fillMaxSize()
                        .horizontalScroll(horizontalScrollState)
                        .verticalScroll(verticalScrollState)
                        .padding(8.dp)
                )
            }

            // 前景层 - 透明的编辑器
            BasicTextField(
                value = code,
                onValueChange = onCodeChange,
                textStyle = textStyle.copy(color = Color.Transparent),
                modifier = Modifier
                    .fillMaxSize()
                    .horizontalScroll(horizontalScrollState)
                    .verticalScroll(verticalScrollState),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                if (code.isEmpty()) MaterialTheme.colorScheme.surface
                                else Color.Transparent,
                                MaterialTheme.shapes.small
                            )
                            .padding(8.dp)
                    ) {
                        if (code.isEmpty()) {
                            Text(
                                text = "在此输入${language.displayName}代码...",
                                style = textStyle.copy(
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                )
                            )
                        }
                        innerTextField()
                    }
                }
            )
        }
    }
}

@Composable
private fun ExecutionResultContent(
    result: CodeExecutionResponse,
    userInput: String,
    onInputChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // 输出结果
        Text(
            text = "输出:",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold
        )

        SelectionContainer {
            Text(
                text = result.output.ifEmpty { "无输出" },
                style = TextStyle(
                    fontFamily = FontFamily.Monospace,
                    fontSize = 12.sp
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant,
                        MaterialTheme.shapes.small
                    )
                    .padding(12.dp)
            )
        }

        // 错误信息
        if (!result.error.isNullOrEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "错误:",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.error
            )

            SelectionContainer {
                Text(
                    text = result.error,
                    style = TextStyle(
                        fontFamily = FontFamily.Monospace,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.error
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            MaterialTheme.colorScheme.errorContainer,
                            MaterialTheme.shapes.small
                        )
                        .padding(12.dp)
                )
            }
        }

        // 执行时间
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "执行时间: ${result.executionTime}ms",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // 用户输入区域
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "交互输入:",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold
        )

        OutlinedTextField(
            value = userInput,
            onValueChange = onInputChange,
            placeholder = { Text("如果程序需要输入，请在此输入...") },
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(fontFamily = FontFamily.Monospace)
        )

        Text(
            text = "提示: 输入后重新运行代码以查看结果",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}
