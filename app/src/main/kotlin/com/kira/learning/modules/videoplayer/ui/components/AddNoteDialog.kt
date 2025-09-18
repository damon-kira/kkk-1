package com.kira.learning.modules.videoplayer.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.kira.learning.models.VideoNote
import com.kira.learning.models.NoteColor
import com.kira.learning.models.formatTime
import com.kira.learning.models.toColor

/**
 * 添加/编辑笔记对话框
 */
@Composable
fun AddNoteDialog(
    note: VideoNote?, onSave: (String, NoteColor) -> Unit, // 修改：添加颜色参数
    onDismiss: () -> Unit, modifier: Modifier = Modifier
) {
    var noteContent by remember { mutableStateOf(note?.content ?: "") }
    var selectedColor by remember { mutableStateOf(note?.color ?: NoteColor.BLUE) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 标题
                Text(
                    text = if (note?.id?.isEmpty() != false) "添加笔记" else "编辑笔记",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                // 时间显示
                note?.let {
                    Text(
                        text = "时间位置: ${formatTime(it.position)}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // 笔记内容输入
                OutlinedTextField(
                    value = noteContent,
                    onValueChange = { noteContent = it },
                    label = { Text("笔记内容") },
                    placeholder = { Text("在这里记录你的想法...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 120.dp),
                    maxLines = 5,
                    singleLine = false
                )

                Spacer(modifier = Modifier.height(24.dp))

                // 颜色选择
                Text(
                    text = "选择颜色",
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.align(Alignment.Start)
                )

                Spacer(modifier = Modifier.height(12.dp))

                ColorSelector(
                    selectedColor = selectedColor, onColorSelected = { selectedColor = it })

                Spacer(modifier = Modifier.height(32.dp))

                // 操作按钮
                Row(
                    modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("取消")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {
                            if (noteContent.isNotBlank()) {
                                onSave(noteContent.trim(), selectedColor) // 修改：传递颜色参数
                            }
                        }, enabled = noteContent.isNotBlank()
                    ) {
                        Text("保存")
                    }
                }
            }
        }
    }
}

@Composable
private fun ColorSelector(
    selectedColor: NoteColor, onColorSelected: (NoteColor) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        NoteColor.values().forEach { color ->
            ColorOption(
                color = color,
                isSelected = selectedColor == color,
                onClick = { onColorSelected(color) })
        }
    }
}

@Composable
private fun ColorOption(
    color: NoteColor, isSelected: Boolean, onClick: () -> Unit
) {
    val backgroundColor = color.toColor() // 修改：使用统一的颜色转换函数

    Box(
        modifier = Modifier
            .size(if (isSelected) 36.dp else 32.dp)
            .padding(2.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = backgroundColor
            ),
            onClick = onClick,
            border = if (isSelected) {
                androidx.compose.foundation.BorderStroke(
                    2.dp, MaterialTheme.colorScheme.primary
                )
            } else null
        ) {
            Box(modifier = Modifier.fillMaxSize())
        }
    }
}
