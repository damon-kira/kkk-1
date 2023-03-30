package com.util.lib

import android.Manifest
import android.content.ContentResolver
import android.content.ContentResolver.SCHEME_CONTENT
import android.content.ContentResolver.SCHEME_FILE
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.OpenableColumns
import androidx.annotation.RequiresPermission
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream


object StorageUriUtils {

    /**
     * 开启分区存储的版本
     */
    private const val SCOPE_STORAGE_VERSION = Build.VERSION_CODES.Q

    @Deprecated("Uri should be used to access files directly")
    @RequiresPermission(anyOf = [Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE])
    fun getFilePathByUri(context: Context?, uri: Uri?): String? {
        if (context == null || uri == null) return null

        if (SCHEME_FILE.equals(uri.scheme, true))
            return uri.path
        val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
        if (!isKitKat && SCHEME_CONTENT.equals(uri.scheme, true)) {
            return when {
                isGooglePhotosUri(uri) -> {
                    uri.lastPathSegment
                }
                else -> {
                    getDataColumn(context, uri)
                }
            }
        }

        return when {
            isKitKat && DocumentsContract.isDocumentUri(context, uri) -> {
                when {
                    isExternalStorageDocument(uri) -> {  // ExternalStorageProvider
                        getExternalStorageDocumentFilePath(context, uri)
                    }
                    isDownloadsDocument(uri) -> {  // DownloadsProvider
                        getDownloadsDocumentFilePath(context, uri)
                    }
                    isMediaDocument(uri) -> {  // MediaProvider
                        getMediaDocumentFilePath(context, uri)
                    }
                    isGoogleDriveUri(uri) -> {  //GoogleDriveProvider
                        getGoogleDriveFilePath(uri, context)
                    }
                    else -> {
                        null
                    }
                }
            }
            SCHEME_CONTENT.equals(uri.scheme, true) -> {  // MediaStore (and general)
                when {
                    isGooglePhotosUri(uri) -> {  // Return the remote address
                        uri.lastPathSegment
                    }
                    isGoogleDriveUri(uri) -> { // Google drive legacy provider
                        getGoogleDriveFilePath(uri, context)
                    }
                    isHuaWeiUri(uri) -> {// HuaWei
                        getHuaWeiFilePath(context, uri)
                    }
                    else -> {
                        getDataColumn(context, uri)
                    }
                }
            }
            else -> {
                null
            }
        }

    }


    private fun getDataColumn(
        context: Context,
        uri: Uri,
        selection: String? = null,
        selectionArgs: Array<String>? = null
    ): String? {
        val column = MediaStore.MediaColumns.DATA
        val projection = arrayOf(column)
        val cursor = context.contentResolver.query(uri, projection, selection, selectionArgs, null)
        try {
            return cursor?.let {
                if (it.moveToFirst()) {
                    it.getString(it.getColumnIndexOrThrow(column))
                } else {
                    null
                }
            }
        } catch (e: Throwable) {

        } finally {
            cursor?.close()
        }
        return null
    }


    fun isGoogleDriveUri(uri: Uri?): Boolean {
        return "com.google.android.apps.docs.storage.legacy" == uri?.authority || "com.google.android.apps.docs.storage" == uri?.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    /**
     * 判断是否是Google相册的图片，类似于content://com.google.android.apps.photos.content/...
     */
    fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri.authority
    }

    /**
     * 判断是否是Google相册的图片，类似于content://com.google.android.apps.photos.contentprovider/0/1/mediakey:/local%3A821abd2f-9f8c-4931-bbe9-a975d1f5fabc/ORIGINAL/NONE/1075342619
     */
    fun isGooglePlayPhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.contentprovider" == uri.authority
    }


    /**
     * content://com.huawei.hidisk.fileprovider/root/storage/emulated/0/Android/data/com.xxx.xxx/
     *
     * @param uri
     * @return
     */
    fun isHuaWeiUri(uri: Uri?): Boolean {
        return "com.huawei.hidisk.fileprovider".equals(uri?.authority, true)
    }


    fun getGoogleDriveFilePath(uri: Uri, context: Context): String? {
        context.contentResolver.query(uri, null, null, null, null)?.use { c: Cursor ->
            val nameIndex: Int = c.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            c.moveToFirst()
            val name: String = c.getString(nameIndex)
            val file = File(context.cacheDir, name)
            var inputStream: InputStream? = null
            var outputStream: FileOutputStream? = null
            try {
                inputStream = context.contentResolver.openInputStream(uri)
                outputStream = FileOutputStream(file)
                var read = 0
                val maxBufferSize = 1 * 1024 * 1024
                val bytesAvailable: Int = inputStream?.available() ?: 0
                val bufferSize = bytesAvailable.coerceAtMost(maxBufferSize)
                val buffers = ByteArray(bufferSize)
                while (inputStream?.read(buffers)?.also { read = it } != -1) {
                    outputStream.write(buffers, 0, read)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                inputStream?.close()
                outputStream?.close()
                c.close()
            }
            return file.path
        }
        return uri.toString()
    }


    private fun getDownloadsDocumentFilePath(context: Context, uri: Uri): String? {
        return when {
            Build.VERSION.SDK_INT < SCOPE_STORAGE_VERSION -> {
                val id = DocumentsContract.getDocumentId(uri).let {
                    if (it.startsWith("raw:")) {
                        it.substring(4)
                    } else {
                        it
                    }
                }.toLong()
                val contentUriPreArr = arrayOf(
                    "content://downloads/public_downloads",
                    "content://downloads/my_downloads",
                    "content://downloads/all_downloads"
                )
                var data: String? = null
                for (prefix in contentUriPreArr) {
                    data = getDataColumn(
                        context, ContentUris.withAppendedId(Uri.parse(prefix), id)
                    )
                    if (!data.isNullOrBlank())
                        break
                }
                data
            }
            else -> {
                null
            }
        }
    }

    private fun getMediaDocumentFilePath(context: Context, uri: Uri): String? {
        val docId = DocumentsContract.getDocumentId(uri)
        val split = docId.split(":")
        return when (split[0]) {
            "image" -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            "video" -> MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            "audio" -> MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            else -> null
        }?.let {
            return getDataColumn(
                context,
                it,
                "_id=?",
                arrayOf(split[1])
            )
        }
    }

    private fun getExternalStorageDocumentFilePath(context: Context, uri: Uri): String? {
        val docId = DocumentsContract.getDocumentId(uri)
        val split = docId.split(":")
        val type = split[0]
        val id = split[1]
        return when {
            "primary".equals(type, true) -> {
                return if (Build.VERSION.SDK_INT >= SCOPE_STORAGE_VERSION) {
                    context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
                        .toString() + File.separator + id
                } else {
                    Environment.getExternalStorageDirectory().toString() + File.separator + id
                }
            }
            "home".equals(type, true) -> {
                return if (Build.VERSION.SDK_INT >= SCOPE_STORAGE_VERSION) {
                    context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
                        .toString() + File.separator + "documents" + File.separator + id
                } else {
                    Environment.getExternalStorageDirectory()
                        .toString() + File.separator + "documents" + File.separator + id
                }
            }
            else -> {
                null
            }
        }
    }

    private fun getHuaWeiFilePath(context: Context, uri: Uri): String? {
        // content://com.huawei.hidisk.fileprovider/root/storage/emulated/0/Android/data/com.xxx.xxx/
        val uriPath = uri.path ?: uri.toString()
        return if (uriPath.startsWith("/root")) {
            return uriPath.replace("/root", "")
        } else {
            getDataColumn(context, uri)
        }
    }


    fun getFileSize(context: Context, uri: Uri): Long? {
        val uriScheme = uri.scheme
        return when {
            Build.VERSION.SDK_INT >= SCOPE_STORAGE_VERSION || SCHEME_CONTENT.equals(
                uriScheme,
                true
            ) -> {
                val cursor: Cursor? = context.contentResolver.query(uri, null, null, null, null)
                return cursor?.use { c ->
                    val sizeIndex: Int = c.getColumnIndex(OpenableColumns.SIZE)
                    if (c.moveToFirst() && !c.isNull(sizeIndex))
                        c.getLong(sizeIndex)
                    else
                        null
                }
            }
            SCHEME_FILE.equals(uriScheme, true) -> {
                uri.path?.let { File(it) }?.length()
            }
            else -> null
        }
    }
}