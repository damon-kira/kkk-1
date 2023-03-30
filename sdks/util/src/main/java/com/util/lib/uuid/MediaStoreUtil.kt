package com.util.lib.uuid

import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.BaseColumns
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import androidx.annotation.RequiresApi
import com.util.lib.log.logger_d
import com.util.lib.log.logger_e

/**
* android Q+版本访问文件中指纹专用
**/
object MediaStoreUtil {

    private val TAG = "debug_MediaStoreUtil"

    //媒体实体类
    data class MediaBean(
        val uri: Uri,
        val id: Long,
        val name: String,
        val mimeType: String,
        val size: Int
    ) {
        override fun toString(): String {
            return "MediaBean id =$id\ntitle = $name\nmime_type: =$mimeType\nsize: =\t$size\ncontentUri: =\t$uri\n"
        }
    }

    /**
     *  Insert [ 图片媒体集 ]
     */
    @RequiresApi(Build.VERSION_CODES.Q)
    fun insertImageToCollection(context: Context, disPlayName: String, uuid: String) {
        logger_d(
            TAG,
            "insertImageToCollection() called with: context = $context, disPlayName = $disPlayName"
        )
        val contentResolver = context.contentResolver
        //在主要外部存储设备上查找所有图片文件 (API <= 28 使用 VOLUME_EXTERNAL 代替)
//        val imageCollection = MediaStore.Images.Media.getContentUri(
//            MediaStore.VOLUME_EXTERNAL_PRIMARY
//        )
        val imageCollection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val contentValues = ContentValues().apply {
            //配置图片的显示名称
            put(MediaStore.Images.Media.DISPLAY_NAME, disPlayName)
            //配置图片的状态为：等待中...
            put(MediaStore.Images.Media.IS_PENDING, 1)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }

        //开始插入图片
        try {
            val imageUri = contentResolver.insert(imageCollection, contentValues)
            imageUri?.let {
                contentResolver.openOutputStream(imageUri)?.use { ops ->
                    ops.write(uuid.toByteArray())
                    ops.flush()
                    contentValues.clear()
                    contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
                    contentResolver.update(imageUri, contentValues, null, null)
                    ops.close()
                }
                logger_e(TAG, "insertImageToCollection: success ========")
            }
        } catch (e: Exception) {
            logger_e(TAG, "error = $e")
        }
    }


    /**
     * Query [ 图片媒体集 ] 包括： DCIM/ 和 Pictures/ 目录
     */
    @RequiresApi(Build.VERSION_CODES.Q)
    fun queryImageCollection(context: Context, disPlayName: String): MutableList<MediaBean> {
        val imageBeanList = mutableListOf<MediaBean>()
        //定义内容解析器
        val contentResolver = context.contentResolver
        //指定查询的列名
        val photoColumns = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.TITLE,
            MediaStore.Images.Media.MIME_TYPE,
            MediaStore.Images.Media.SIZE
        )
        val selection =
            MediaStore.Images.Media.DISPLAY_NAME + " LIKE  '%" + disPlayName + "%'"
        try {

            val cursor = contentResolver.query(
                //            setIncludePending(MediaStore.Images.Media.EXTERNAL_CONTENT_URI), //指定查询哪张表的URI
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, //指定查询哪张表的URI
                photoColumns, // 指定查询的列明
                selection, //指定where的约束条件
                null, //为where中的占位符提供具体的值
                null // 指定查询结果的排序方式
            )


            cursor?.use {
                while (cursor.moveToNext()) {
                    try {
                        val title =
                            cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.TITLE))
                        val mimeType =
                            cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.MIME_TYPE))
                        val size =
                            cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE))
                        val contentUri = ContentUris.withAppendedId(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            cursor.getLong(cursor.getColumnIndex(BaseColumns._ID))
                        )
                        if (size == null){
                            continue
                        }

                        val imageBean = MediaBean(
                            uri = contentUri,
                            name = title,
                            mimeType = mimeType,
                            size = size.toInt(),
                            id = cursor.getLong(cursor.getColumnIndex(BaseColumns._ID))
                        )

                        logger_d(TAG, "queryImageCollection = ${imageBean}")

                        imageBeanList += imageBean
                    } catch (e: Exception) {
                        logger_e(TAG, "error = $e")
                    }

                }
                cursor.close()
            }
        } catch (e: Exception) {
            logger_e(TAG, "catch error = $e")
        }
        return imageBeanList
    }


    /**
     * Query [ 视频媒体集 ] 包括： DCIM/, Movies/, 和 Pictures/ 目录
     */
    @RequiresApi(Build.VERSION_CODES.Q)
    fun queryVideoCollection(context: Context, disPlayName: String): MutableList<MediaBean> {
        Log.d(TAG, "########### 视频媒体集 ############")
        val videoBeanList = mutableListOf<MediaBean>()
        try {
            val contentResolver = context.contentResolver
            val videoColumns = arrayOf(
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.MIME_TYPE,
                MediaStore.Video.Media.SIZE
            )
            val selection =
                MediaStore.Video.Media.DISPLAY_NAME + " LIKE  '%" + disPlayName + "%'"
            val cursor = contentResolver.query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                videoColumns,
                selection,
                null,
                null
            )

            cursor?.use {
                while (cursor.moveToNext()) {
                    val title =
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE))
                    val mimeType =
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE))
                    val size =
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE))
                    val contentUri = ContentUris.withAppendedId(
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                        cursor.getLong(cursor.getColumnIndex(BaseColumns._ID))
                    )
                    if (size == null) {
                        continue
                    }

                    val videoBean = MediaBean(
                        uri = contentUri,
                        name = title,
                        mimeType = mimeType,
                        size = size.toInt(),
                        id = cursor.getLong(cursor.getColumnIndex(BaseColumns._ID))
                    )

                    Log.d(TAG, "videoBean = $videoBean")
                    videoBeanList += videoBean

                }
                cursor.close()
            }
        } catch (e: Exception) {
        }
        return videoBeanList
    }

    /**
     * Query [ 音频媒体集 ] 包括： Alarms/, Audiobooks/, Music/, Notifications/, Podcasts/, 和 Ringtones/ 目录
     * 以及 Music/ 和 Movies/ 目录中的音频文件
     */
    @RequiresApi(Build.VERSION_CODES.Q)
    fun queryAudioCollection(context: Context, disPlayName: String): MutableList<MediaBean> {
        Log.d(TAG, "########### 音频媒体集 ############")
        val audioBeanList = mutableListOf<MediaBean>()
        try {
            val contentResolver = context.contentResolver
            val videoColumns = arrayOf(
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.MIME_TYPE,
                MediaStore.Audio.Media.SIZE
            )
            val selection =
                MediaStore.Video.Media.DISPLAY_NAME + " LIKE  '%" + disPlayName + "%'"
            val cursor = contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                videoColumns,
                selection,
                null,
                null
            )

            cursor?.use {
                while (cursor.moveToNext()) {
                    val title =
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE))
                    val mimeType =
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE))
                    val size =
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE))
                    val contentUri = ContentUris.withAppendedId(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        cursor.getLong(cursor.getColumnIndex(BaseColumns._ID))
                    )
                    if (size == null) {
                        continue
                    }
                    val audioBean = MediaBean(
                        uri = contentUri,
                        name = title,
                        mimeType = mimeType,
                        size = size.toInt(),
                        id = cursor.getLong(cursor.getColumnIndex(BaseColumns._ID))
                    )

                    audioBeanList += audioBean

                }
                cursor.close()
            }
        } catch (e: Exception) {
        }
        return audioBeanList
    }

    /**
     *  Insert [ 视频媒体集 ]
     */
    @RequiresApi(Build.VERSION_CODES.Q)
    fun insertVideoToCollection(context: Context, disPlayName: String, uuid: String) {
        Log.d(
            TAG,
            "insertVideoToCollection() called with: context = $context, disPlayName = $disPlayName"
        )
        val contentResolver = context.contentResolver
        val contentValues = ContentValues().apply {
            //配置视频的显示名称
            put(MediaStore.Video.Media.DISPLAY_NAME, disPlayName)
            //配置视频的状态为：等待中...
            put(MediaStore.Video.Media.IS_PENDING, 1)
        }
        //在主要外部存储设备上查找所有视频文件 (API <= 28 使用 VOLUME_EXTERNAL 代替)
        var videoCollection = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            videoCollection = MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        }

        //开始插入视频
        try {
            val videoUri = contentResolver.insert(videoCollection, contentValues)
            videoUri?.let {
                contentResolver.openOutputStream(videoUri)?.use { ops ->
                    ops.write(uuid.toByteArray())
                    ops.flush()
                    contentValues.clear()
                    contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
                    contentResolver.update(videoUri, contentValues, null, null)
                    ops.close()
                }
                logger_d(TAG, "insertVideoToCollection success ====")
            }
        } catch (e: Exception) {
            Log.e(TAG, "insertVideoToCollection: error  = ${e.toString()}")
        }
    }

    /**
     *  Insert [ 音频媒体集 ]
     */
    @RequiresApi(Build.VERSION_CODES.Q)
    fun insertAudioToCollection(context: Context, disPlayName: String, uuid: String) {
        Log.d(
            TAG,
            "insertAudioToCollection() called with: context = $context, disPlayName = $disPlayName"
        )
        val contentResolver = context.contentResolver
        //在主要外部存储设备上查找所有音频文件 (API <= 28 使用 VOLUME_EXTERNAL 代替)
        val audioCollection = if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } else MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val contentValues = ContentValues().apply {
            //配置音频的显示名称
            put(MediaStore.Audio.Media.DISPLAY_NAME, disPlayName)
            //配置音频的状态为：等待中...
            put(MediaStore.Audio.Media.IS_PENDING, 1)
        }

        //开始插入音频

        try {
            val audioUri = contentResolver.insert(audioCollection, contentValues)
            audioUri?.let {
                contentResolver.openOutputStream(audioUri)?.use { ops ->
                    ops.write(uuid.toByteArray())
                    ops.flush()
                    contentValues.clear()
                    contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
                    contentResolver.update(audioUri, contentValues, null, null)
                    ops.close()
                }
                logger_d(TAG, "insertAudioToCollection success ====")
            }
        } catch (e: Exception) {
            Log.e(TAG, "insertAudioToCollection: error  = ${e.toString()}")
        }
    }


    fun readUUIDFromUri(context: Context, uri: Uri): String {
        try {
            val resolver: ContentResolver = context.applicationContext.contentResolver
            resolver.openInputStream(uri).use { inputStream ->
                if (inputStream != null) {
                    val bytes = ByteArray(inputStream.available())
                    inputStream.read(bytes, 0, bytes.size)
                    inputStream.close()
                    if (bytes.size == 34) {//只认可36位的 UUID，也就是8-4-4-4-12的
                        return String(bytes)
                    }
                }
            }
        } catch (e: Exception) {
        }
        return ""
    }


    fun getUUID(context: Context, disPlayName: String): String {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val imageUUID = getImageUUID(context, disPlayName)
            if (!TextUtils.isEmpty(imageUUID)) {
                return imageUUID
            }
            val videoUUID = getVideoUUID(context, disPlayName)
            if (!TextUtils.isEmpty(videoUUID)) {
                return videoUUID
            }
            val audioUUID = getAudioUUID(context, disPlayName)
            if (!TextUtils.isEmpty(audioUUID)) {
                return audioUUID
            }
        }
        return ""
    }


    fun saveUUID(context: Context, uuid: String, disPlayName: String) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val imageUUID = getImageUUID(context, disPlayName)
                if (TextUtils.isEmpty(imageUUID)) {
                    insertImageToCollection(context, disPlayName = disPlayName, uuid = uuid)
                }
                val videoUUID = getVideoUUID(context, disPlayName)
                if (TextUtils.isEmpty(videoUUID)) {
                    insertVideoToCollection(context, disPlayName = disPlayName, uuid = uuid)
                }
                val audioUUID = getAudioUUID(context, disPlayName)
                if (TextUtils.isEmpty(audioUUID)) {
                    insertAudioToCollection(context, disPlayName = disPlayName, uuid = uuid)
                }
            }
        } catch (e: Exception) {
            logger_e(TAG, "saveUUID error ===$e")
        }

    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun getImageUUID(context: Context, disPlayName: String): String {
        val list = queryImageCollection(context, disPlayName)
        return getUUIDFromList(context, list)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun getVideoUUID(context: Context, disPlayName: String): String {
        val list = queryVideoCollection(context, disPlayName)
        return getUUIDFromList(context, list)
    }


    @RequiresApi(Build.VERSION_CODES.Q)
    private fun getAudioUUID(context: Context, disPlayName: String): String {
        val list = queryAudioCollection(context, disPlayName)
        return getUUIDFromList(context, list)
    }

    private fun getUUIDFromList(context: Context, list: MutableList<MediaBean>): String {
        if (list.isEmpty()) {
            return ""
        }
        for (mediaBean in list) {
            val uuid = readUUIDFromUri(context, uri = mediaBean.uri)
            if (uuid.isNotEmpty()) {
                return uuid
            }
        }
        return ""
    }

}