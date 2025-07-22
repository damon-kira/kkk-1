package com.kira.ui.feature.editor.data.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import com.kira.ui.editorkit.model.TextChange
import com.kira.ui.editorkit.model.UndoStack
import java.io.File
import java.nio.charset.Charset
import java.nio.charset.UnsupportedCharsetException

internal fun charsetFor(charsetName: String): Charset = try {
    Charset.forName(charsetName)
} catch (e: UnsupportedCharsetException) {
    e.printStackTrace()
    Charsets.UTF_8
}

internal fun UndoStack.encode(): String {
    val builder = StringBuilder()
    val delimiter = "\u0005"
    for (i in size - 1 downTo 0) {
        val textChange = get(i)
        builder.append(textChange.oldText)
        builder.append(delimiter)
        builder.append(textChange.newText)
        builder.append(delimiter)
        builder.append(textChange.start)
        builder.append(delimiter)
    }
    if (builder.isNotEmpty()) {
        builder.deleteCharAt(builder.length - 1)
    }
    return builder.toString()
}

internal fun String.decode(): UndoStack {
    val result = UndoStack()
    if (isNotEmpty()) {
        val items = split("\u0005").toTypedArray()
        if (items[items.size - 1].endsWith("\n")) {
            val item = items[items.size - 1]
            items[items.size - 1] = item.substring(0, item.length - 1)
        }
        for (i in items.size - 3 downTo 0 step 3) {
            val change = TextChange(
                newText = items[i + 1],
                oldText = items[i],
                start = items[i + 2].toInt(),
            )
            result.push(change)
        }
    }
    return result
}

/**
 * 检查文件大小并决定是否允许打开
 * @param context Context
 * @param fileUri 文件Uri (content:// 或 file://)
 * @param maxSizeMb 最大允许大小(MB)，默认5MB
 * @return Pair<Boolean, String> 第一个参数表示是否允许打开，第二个参数是错误信息或文件大小描述
 */
fun checkFileSizeBeforeOpen(
    context: Context,
    fileUri: Uri,
    maxSizeMb: Int = 5
): Pair<Boolean, String> {
    return try {
        // 获取文件大小（自动处理不同Uri类型）
        val fileSizeBytes = getFileSizeFromUri(context, fileUri)
        val maxSizeBytes = maxSizeMb * 1024 * 1024

        when {
            fileSizeBytes == null -> Pair(false, "无法获取文件大小")
            fileSizeBytes > maxSizeBytes -> {
                val fileSizeMb = String.format("%.2f", fileSizeBytes / (1024.0 * 1024.0))
                Pair(false, "文件过大 ($fileSizeMb MB > $maxSizeMb MB)")
            }

            else -> Pair(true, "文件大小合规")
        }
    } catch (e: SecurityException) {
        Pair(false, "无权限访问该文件")
    } catch (e: Exception) {
        Pair(false, "检查文件大小时出错: ${e.message}")
    }
}

/**
 * 通过Uri获取文件大小（兼容各种Uri方案）
 */
private fun getFileSizeFromUri(context: Context, uri: Uri): Long? {
    return when (uri.scheme) {
        "content" -> getContentUriSize(context, uri)
        "file" -> getFileUriSize(uri)
        else -> null
    }
}

/**
 * 处理 content:// 类型的Uri
 */
private fun getContentUriSize(context: Context, uri: Uri): Long? {
    return try {
        context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                cursor.getColumnIndex(OpenableColumns.SIZE)
                    .takeIf { it >= 0 }
                    ?.let { cursor.getLong(it) }
                    ?: calculateSizeByStream(context, uri)
            } else null
        }
    } catch (e: SecurityException) {
        null // 无权限访问
    }
}

private fun calculateSizeByStream(context: Context, uri: Uri): Long? {
    return context.contentResolver.openInputStream(uri)?.use { stream ->
        var size = 0L
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        var bytesRead: Int
        while (stream.read(buffer).also { bytesRead = it } != -1) {
            size += bytesRead
        }
        size
    }
}

/**
 * 处理 file:// 类型的Uri
 */
private fun getFileUriSize(uri: Uri): Long? {
    return File(uri.path).takeIf { it.exists() }?.length()
}