package com.colombia.credit.module.ocr.utils

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.DocumentsContract
import android.provider.MediaStore
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

object UriUtils {
    /**
     * 获取真实路径
     *
     *
     * 支持以下
     *
     *
     * file://
     * content://media/external/file/109009
     * FileProvider适配
     * content://com.tencent.mobileqq.fileprovider/external_files/storage/emulated/0/Tencent/QQfile_recv/
     * content://com.tencent.mm.external.fileprovider/external/tencent/MicroMsg/Download/
     * content://com.android.providers.downloads.documents"
     * content://com.android.externalstorage.documents
     * content://com.android.providers.media.documents
     * content://com.google.android.apps.photos.content
     */
    fun getFileFromUri(context: Context, uri: Uri?): String? {
        if (uri == null) {
            return null
        }
        when (uri.getScheme()) {
            ContentResolver.SCHEME_CONTENT -> {
                if (isGooglePhotosUri(uri)) {
                    return uri.getLastPathSegment()
                } else if (isMediaDocument(uri)) {
                    // MediaProvider
                    val docId = DocumentsContract.getDocumentId(uri)
                    val split: Array<String?> =
                        docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                    val type = split[0]
                    var contentUri: Uri? = null
                    if ("image" == type) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    } else if ("video" == type) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                    } else if ("audio" == type) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                    }
                    val selection = "_id=?"
                    val selectionArgs = arrayOf<String?>(split[1])

                    return getFilePathFromContentUri(context, contentUri, selection, selectionArgs)
                } else if (isDownloadsDocument(uri)) {
                    // DownloadsProvider
                    val id = DocumentsContract.getDocumentId(uri)
                    val contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        id.toLong()
                    )
                    return getFilePathFromContentUri(context, contentUri, null, null)
                }

                return getFilePathFromContentUri(context, uri, null, null)
            }

            ContentResolver.SCHEME_FILE ->                 //file://
                return File(uri.getPath()).getAbsolutePath()

            else ->
                return File(uri.getPath()).getAbsolutePath()
        }
    }

    /**
     * 从uri获取path 或 拷贝
     */
    private fun getFilePathFromContentUri(
        context: Context,
        uri: Uri?,
        selection: String?,
        selectionArgs: Array<String?>?
    ): String? {
        if (null == uri) return null
        var data: String? = null

        val filePathColumn =
            arrayOf<String?>(MediaStore.MediaColumns.DATA, MediaStore.MediaColumns.DISPLAY_NAME)
        val cursor =
            context.getContentResolver().query(uri, filePathColumn, selection, selectionArgs, null)
        if (null != cursor) {
            if (cursor.moveToFirst()) {
                val index = cursor.getColumnIndex(MediaStore.MediaColumns.DATA)
                if (index > -1) {
                    data = cursor.getString(index)
                    if (data == null || !fileIsExists(data)) {
                        //可能拿不到真实路径 或 文件不存在  走拷贝流程
                        val nameIndex = cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME)
                        val fileName = cursor.getString(nameIndex)
                        data = getPathFromInputStreamUri(context, uri, fileName)
                    }
                } else {
                    //拷贝一份
                    val nameIndex = cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME)
                    val fileName = cursor.getString(nameIndex)
                    data = getPathFromInputStreamUri(context, uri, fileName)
                }
            }
            cursor.close()
        }
        return data
    }

    /**
     * 用流拷贝文件一份到自己APP私有目录下
     *
     * @param context
     * @param uri
     * @param fileName
     */
    private fun getPathFromInputStreamUri(context: Context, uri: Uri, fileName: String): String? {
        var inputStream: InputStream? = null
        var filePath: String? = null

        if (uri.getAuthority() != null) {
            try {
                inputStream = context.getContentResolver().openInputStream(uri)
                val file = createTemporalFileFrom(context, inputStream, fileName)
                filePath = file?.getPath()
            } catch (e: Exception) {
            } finally {
                try {
                    if (inputStream != null) {
                        inputStream.close()
                    }
                } catch (e: Exception) {
                }
            }
        }

        return filePath
    }

    @Throws(IOException::class)
    private fun createTemporalFileFrom(
        context: Context,
        inputStream: InputStream?,
        fileName: String
    ): File? {
        var targetFile: File? = null

        if (inputStream != null) {
            var read: Int
            val buffer = ByteArray(8 * 1024)
            //自己定义拷贝文件路径
            targetFile = File(context.getExternalCacheDir(), fileName)
            if (targetFile.exists()) {
                targetFile.delete()
            }
            val outputStream: OutputStream = FileOutputStream(targetFile)

            while ((inputStream.read(buffer).also { read = it }) != -1) {
                outputStream.write(buffer, 0, read)
            }
            outputStream.flush()

            try {
                outputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        return targetFile
    }

    private fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.getAuthority()
    }

    private fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.getAuthority()
    }

    fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri.getAuthority()
    }

    //判断文件是否存在
    private fun fileIsExists(filePath: String): Boolean {
        try {
            val f = File(filePath)
            if (!f.exists()) {
                return false
            }
        } catch (e: Exception) {
            return false
        }
        return true
    }
}
