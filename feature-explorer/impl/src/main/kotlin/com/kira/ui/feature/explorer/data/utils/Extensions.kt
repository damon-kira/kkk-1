

package com.kira.ui.feature.explorer.data.utils

import android.content.*
import androidx.core.content.FileProvider
import androidx.core.content.getSystemService
import com.kira.ui.core.extensions.showToast
import com.kira.ui.feature.explorer.R
import com.kira.ui.filesystem.base.model.FileModel
import timber.log.Timber
import java.io.File
import java.io.FileNotFoundException
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.log10
import kotlin.math.pow

fun Context.openFileWith(fileModel: FileModel) {
    try {
        val file = File(fileModel.path)
        if (!file.exists()) {
            throw FileNotFoundException(file.path)
        }

        val uri = FileProvider.getUriForFile(
            this,
            "$packageName.provider",
            file,
        )

        val mime = contentResolver?.getType(uri)
        val intent = Intent(Intent.ACTION_VIEW).apply {
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            setDataAndType(uri, mime)
        }
        startActivity(intent)
    } catch (e: Exception) {
        Timber.d(e, e.message)
        showToast(R.string.message_cannot_be_opened)
    }
}

fun Long.toReadableDate(pattern: String): String {
    val dateFormat = SimpleDateFormat(pattern, Locale.getDefault())
    return dateFormat.format(this)
}

fun Long.toReadableSize(): String {
    if (this <= 0) {
        return "0"
    }
    val units = arrayOf("B", "KB", "MB", "GB", "TB")
    val digitGroups = (log10(toDouble()) / log10(1024.0)).toInt()
    return DecimalFormat("#,##0.#")
        .format(this / 1024.0.pow(digitGroups.toDouble())) + " " + units[digitGroups]
}

fun String.clipText(context: Context?) = clip(context, ClipData.newPlainText("Text", this))

private fun clip(context: Context?, data: ClipData) {
    context?.getSystemService<ClipboardManager>()?.setPrimaryClip(data)
}