

package com.kira.ui.feature.editor.data.utils

import com.kira.ui.editorkit.model.TextChange
import com.kira.ui.editorkit.model.UndoStack
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