package com.util.lib

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.text.TextUtils
import java.io.*

/**
 * 在URI中获取照片地址，copy到APP目录下
 */
fun getFilePathFromUri(
    context: Context,
    contentUri: Uri
): String? {
    val rootDataDir = context.getExternalFilesDir(null)
    val fileName: String = getFileNameByUri(context, contentUri)
    if (!TextUtils.isEmpty(fileName)) {
        val copyFile =
            File(rootDataDir.toString() + File.separator + fileName)
        copyFile(context, contentUri, copyFile)
        return copyFile.absolutePath
    }
    return null
}

private fun getFileNameByUri(
    context: Context,
    uri: Uri
): String {
    var fileName = ""
    val cursor = context.contentResolver.query(uri, null, null, null, null)
    if (cursor != null && cursor.count > 0) {
        cursor.moveToFirst()
        fileName =
            cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME))
        cursor.close()
    }
    return fileName
}

fun copyFile(
    context: Context,
    srcUri: Uri?,
    dstFile: File?
) {
    try {
        val inputStream = context.contentResolver.openInputStream(srcUri!!) ?: return
        val outputStream: OutputStream = FileOutputStream(dstFile)
        copyStream(inputStream, outputStream)
        inputStream.close()
        outputStream.close()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

@Throws(Exception::class, IOException::class)
fun copyStream(input: InputStream?, output: OutputStream?): Int {
    val BUFFER_SIZE = 1024 * 2
    val buffer = ByteArray(BUFFER_SIZE)
    val bis = BufferedInputStream(input, BUFFER_SIZE)
    val out = BufferedOutputStream(output, BUFFER_SIZE)
    var count = 0
    var len = 0
    try {
        while (bis.read(buffer, 0, BUFFER_SIZE).also { len = it } != -1) {
            out.write(buffer, 0, len)
            count += len
        }
        out.flush()
    } finally {
        try {
            out.close()
        } catch (e: IOException) {
        }
        try {
            bis.close()
        } catch (e: IOException) {
        }
    }
    return count
}