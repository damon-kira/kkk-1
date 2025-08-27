package com.kira.learning.compose.module.answer

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import kotlin.time.Duration.Companion.seconds

@Composable
fun AnswerRoute(
    modifier: Modifier = Modifier,
    viewModel: AnswerViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    AnswerScreen(
        state = state,
        onSingle = viewModel::updateSingle,
        onMultiToggle = viewModel::toggleMulti,
        onShort = viewModel::updateShort,
        onFill = viewModel::updateFillBlank,
        onOpen = viewModel::updateOpen,
        onSubmitRequest = viewModel::requestSubmit,
        onSubmitConfirm = viewModel::submitConfirmed,
        onSubmitDismiss = viewModel::dismissSubmitConfirm,
        onPrev = viewModel::prevQuestion,
        onNext = viewModel::nextQuestion,
        onJump = viewModel::jumpTo,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AnswerScreen(
    state: AnswerUiState,
    onSingle: (String, Int) -> Unit,
    onMultiToggle: (String, Int) -> Unit,
    onShort: (String, String) -> Unit,
    onFill: (String, Int, String) -> Unit,
    onOpen: (String, String) -> Unit,
    onSubmitRequest: () -> Unit,
    onSubmitConfirm: () -> Unit,
    onSubmitDismiss: () -> Unit,
    onPrev: () -> Unit,
    onNext: () -> Unit,
    onJump: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val total = state.questions.size
    val currentIndex = state.currentIndex.coerceIn(0, (total - 1).coerceAtLeast(0))
    val question = state.questions.getOrNull(currentIndex)

    if (state.showSubmitConfirm) ConfirmSubmitDialog(
        answered = state.answeredCount,
        total = total,
        onConfirm = onSubmitConfirm,
        onDismiss = onSubmitDismiss
    )

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("答题 (${currentIndex + 1}/$total)")
                    Spacer(Modifier.width(12.dp))
                    LinearProgressIndicator(
                        progress = if (total == 0) 0f else state.answeredCount / total.toFloat(),
                        modifier = Modifier.weight(1f)
                    )
                }
            }, actions = {
                val min = state.elapsedSeconds / 60
                val sec = state.elapsedSeconds % 60
                Text(String.format("%02d:%02d", min, sec), style = MaterialTheme.typography.labelLarge, modifier = Modifier.padding(end = 12.dp))
            })
        },
        bottomBar = {
            Surface(shadowElevation = 6.dp) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AssistChip(onClick = { onPrev() }, label = { Text("上一题") }, enabled = currentIndex > 0 && !state.loading)
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("已答 ${state.answeredCount}/$total", style = MaterialTheme.typography.bodySmall)
                    }
                    AssistChip(onClick = { onNext() }, label = { Text("下一题") }, enabled = currentIndex < total - 1 && !state.loading)
                    Button(onClick = onSubmitRequest, enabled = total > 0 && !state.submitted) { Text(if (state.submitted) "已提交" else "提交") }
                }
            }
        }
    ) { padding ->
        when {
            state.loading -> Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
            question == null -> Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) { Text("暂无题目") }
            else -> SingleQuestionPage(
                question = question,
                answer = state.answers[question.id],
                result = state.results[question.id],
                submitted = state.submitted,
                onSingle = onSingle,
                onMultiToggle = onMultiToggle,
                onShort = onShort,
                onFill = onFill,
                onOpen = onOpen,
                modifier = Modifier.fillMaxSize().padding(padding)
            )
        }
    }
}

@Composable
private fun ConfirmSubmitDialog(answered: Int, total: Int, onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = { TextButton(onClick = onConfirm) { Text("确认提交") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("取消") } },
        title = { Text("提交确认") },
        text = { Text("您已完成 $answered/$total 题，确认提交吗？") }
    )
}

// 单题展示
@Composable
private fun SingleQuestionPage(
    question: QuestionBase,
    answer: UserAnswer?,
    result: MarkResult?,
    submitted: Boolean,
    onSingle: (String, Int) -> Unit,
    onMultiToggle: (String, Int) -> Unit,
    onShort: (String, String) -> Unit,
    onFill: (String, Int, String) -> Unit,
    onOpen: (String, String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            QuestionCard(
                question = question,
                answer = answer,
                result = result,
                submitted = submitted,
                onSingle = onSingle,
                onMultiToggle = onMultiToggle,
                onShort = onShort,
                onFill = onFill,
                onOpen = onOpen
            )
        }
    }
}

// 富文本题干简单解析（支持 **加粗** 与 `代码`）
@Composable
private fun RichStem(text: String, modifier: Modifier = Modifier) {
    val parts = remember(text) { parseRich(text) }
    Row(modifier, verticalAlignment = Alignment.CenterVertically) {
        parts.forEach { seg ->
            when (seg) {
                is RichSeg.Normal -> Text(seg.content)
                is RichSeg.Bold -> Text(seg.content, fontWeight = FontWeight.Bold)
                is RichSeg.Code -> Surface(color = MaterialTheme.colorScheme.surfaceVariant, shape = MaterialTheme.shapes.small) {
                    Text(seg.content, modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp), style = MaterialTheme.typography.labelSmall)
                }
            }
            Spacer(Modifier.width(2.dp))
        }
    }
}

private sealed interface RichSeg { data class Normal(val content: String): RichSeg; data class Bold(val content: String): RichSeg; data class Code(val content: String): RichSeg }
private fun parseRich(src: String): List<RichSeg> {
    val result = mutableListOf<RichSeg>()
    var i = 0
    while (i < src.length) {
        when {
            src.startsWith("**", i) -> {
                val end = src.indexOf("**", i + 2).takeIf { it > i + 2 }
                if (end != null) {
                    result += RichSeg.Bold(src.substring(i + 2, end))
                    i = end + 2
                } else { result += RichSeg.Normal("**"); i += 2 }
            }
            src[i] == '`' -> {
                val end = src.indexOf('`', i + 1).takeIf { it > i + 1 }
                if (end != null) {
                    result += RichSeg.Code(src.substring(i + 1, end))
                    i = end + 1
                } else { result += RichSeg.Normal("`"); i++ }
            }
            else -> {
                val next = listOf(src.indexOf("**", i).takeIf { it >= 0 }, src.indexOf('`', i).takeIf { it >= 0 })
                    .filterNotNull().minOrNull() ?: src.length
                result += RichSeg.Normal(src.substring(i, next))
                i = next
            }
        }
    }
    return result
}

// 修改题干使用 RichStem
@Composable
private fun QuestionCard(
    question: QuestionBase,
    answer: UserAnswer?,
    result: MarkResult?,
    submitted: Boolean,
    onSingle: (String, Int) -> Unit,
    onMultiToggle: (String, Int) -> Unit,
    onShort: (String, String) -> Unit,
    onFill: (String, Int, String) -> Unit,
    onOpen: (String, String) -> Unit,
) {
    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            RichStem(text = "[${typeLabel(question.type)}] ${question.stem}")
            if (question.imageUrls.isNotEmpty()) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    question.imageUrls.forEach { url ->
                        AsyncImage(
                            model = url,
                            contentDescription = null,
                            modifier = Modifier.size(72.dp)
                        )
                    }
                }
            }
            when (question) {
                is SingleChoiceQuestion -> {
                    val selected = (answer as? SingleChoiceUserAnswer)?.selected
                    question.options.forEachIndexed { idx, opt ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            RadioButton(selected = selected == idx, onClick = { onSingle(question.id, idx) }, enabled = !submitted)
                            Text(opt)
                        }
                    }
                }
                is MultiChoiceQuestion -> {
                    val selected = (answer as? MultiChoiceUserAnswer)?.selected ?: emptySet()
                    question.options.forEachIndexed { idx, opt ->
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                            Checkbox(checked = selected.contains(idx), onCheckedChange = { onMultiToggle(question.id, idx) }, enabled = !submitted)
                            Text(opt)
                        }
                    }
                }
                is ShortAnswerQuestion -> {
                    val content = (answer as? ShortAnswerUserAnswer)?.content ?: ""
                    OutlinedTextField(
                        value = content,
                        onValueChange = { onShort(question.id, it) },
                        label = { Text("回答") },
                        enabled = !submitted,
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 4
                    )
                    if (submitted) ReferenceAnswer(question.reference)
                }
                is FillBlankQuestion -> {
                    val ua = (answer as? FillBlankUserAnswer)
                    val parts = remember(question.stem) { question.stem.split("__") }
                    FlowRowWrap(parts = parts, blanks = question.answers.size, ua = ua, enabled = !submitted) { index, text ->
                        onFill(question.id, index, text)
                    }
                    if (submitted) ReferenceAnswer(question.answers.joinToString())
                }
                is OpenExtQuestion -> {
                    val content = (answer as? OpenExtUserAnswer)?.content ?: ""
                    OutlinedTextField(
                        value = content,
                        onValueChange = { onOpen(question.id, it) },
                        label = { Text("你的想法") },
                        enabled = !submitted,
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 6
                    )
                    if (question.guide != null) AssistChip(onClick = {}, label = { Text(question.guide!!) })
                }
            }

            if (submitted && result != null) ResultStatus(result)
        }
    }
}

@Composable
private fun ResultStatus(result: MarkResult) {
    when (result) {
        is ObjectiveMarkResult -> {
            val ok = result.correct
            val icon = if (ok) Icons.Default.Check else Icons.Default.Clear
            val color = if (ok) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(icon, contentDescription = null, tint = color)
                Text(
                    text = if (ok) "正确 (${result.score}/${result.total})" else "错误 (${result.score}/${result.total})",
                    color = color,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        is SubjectiveMarkResult -> {
            if (result.reference != null) ReferenceAnswer(result.reference)
        }
    }
}

@Composable
private fun ReferenceAnswer(text: String?) {
    if (text.isNullOrBlank()) return
    Surface(color = MaterialTheme.colorScheme.surfaceVariant, tonalElevation = 2.dp, shape = MaterialTheme.shapes.small) {
        Text(
            text = "参考: $text",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Composable
private fun FlowRowWrap(
    parts: List<String>,
    blanks: Int,
    ua: FillBlankUserAnswer?,
    enabled: Boolean,
    onChange: (Int, String) -> Unit
) {
    // 简易行内填空: 文本 + 输入框串联
    var blankIndex = 0
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        parts.forEachIndexed { idx, seg ->
            if (seg.isNotEmpty()) Text(seg)
            if (idx < parts.lastIndex || blankIndex < blanks) {
                if (blankIndex < blanks) {
                    val value = ua?.blanks?.getOrNull(blankIndex) ?: ""
                    OutlinedTextField(
                        value = value,
                        onValueChange = { onChange(blankIndex, it) },
                        modifier = Modifier.widthIn(min = 60.dp).padding(horizontal = 4.dp),
                        singleLine = true,
                        enabled = enabled,
                        textStyle = MaterialTheme.typography.bodySmall,
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.None,
                            autoCorrectEnabled = false,
                            keyboardType = KeyboardType.Text,
                            imeAction = if (blankIndex == blanks -1) ImeAction.Done else ImeAction.Next
                        )
                    )
                    blankIndex++
                }
            }
        }
    }
}

private fun typeLabel(type: QuestionType) = when(type) {
    QuestionType.SINGLE_CHOICE -> "单选"
    QuestionType.MULTI_CHOICE -> "多选"
    QuestionType.SHORT_ANSWER -> "简答"
    QuestionType.FILL_BLANK -> "填空"
    QuestionType.OPEN_EXT -> "拓展"
}
