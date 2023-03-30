package com.util.lib.expand

import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import androidx.core.content.FileProvider
import com.cache.lib.SharedPrefUser
import com.util.lib.ThreadPoolUtil
import java.io.File

/**
 * Created by weisl on 2019/10/16.
 */

/**
 * 获取文件uri
 */
fun getUriFile(context: Context, file: File): Uri? {
    var fileUri: Uri?
    if (Build.VERSION.SDK_INT >= 24) {
        fileUri = getUriForFile24(context, file)
    } else {
        fileUri = Uri.fromFile(file)
    }
    return fileUri
}

/**
 * 获取 sdk 24以及以上文件uri
 */
fun getUriForFile24(context: Context, file: File): Uri? {
    try {
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            file
        )
    } catch (e: Exception) {

    }

    return null
}


/**
 * 获取应用缓存文件路径
 */
fun getCacheFile(context: Context): File {
    val file = File("${context.filesDir.absoluteFile}")
    if (!file.exists()) {
        file.mkdirs()
    }
    return file
}

/**
 * 获取存储卡缓存文件路径
 */
fun getExternalCacheFile(context: Context): File {
    val file = File("${context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.absoluteFile}")
    if (!file.exists()) {
        file.mkdirs()
    }
    return file
}

/**
 * 获取照相机拍照temp缓存
 */
private val KEY_CACHE_FILE_PATH = "cache_file_path"
fun getCameraCacheTemp(context: Context): File {
    var path = SharedPrefUser.getString(KEY_CACHE_FILE_PATH, null)
    if (path.isNullOrEmpty()) {
        path = getCameraCache(context).absolutePath
        SharedPrefUser.setString(KEY_CACHE_FILE_PATH, path)
    }
    val file = File("$path${File.separator}cameraTemp")
    if (!file.exists()) {
        file.mkdirs()
    }
    return file
}

/**
 * 获取应用内照相机拍照缓存
 */
fun getCameraCache(context: Context): File {
    val file = File("${getCacheFile(context).absolutePath}${File.separator}camera")
    if (!file.exists()) {
        file.mkdirs()
    }
    return file
}

/**
 *
 * @param fileName 文件名
 * @return 返回路径
 */
fun getPicCacheFilePath(context: Context, fileName: String): String = File(getCameraCache(context), fileName).absolutePath


/**
 * 删除照相机拍照temp缓存
 */
fun deleteCameraExternalCache(context: Context) {
    ThreadPoolUtil.executor("删除照相机拍照临时缓存") {
        deleteDir(getCameraCacheTemp(context))
    }
}

/**
 * 删除应用内拍照图片
 */
fun deleteCameraAppCache(context: Context) {
    ThreadPoolUtil.executor("删除证件信息图片缓存") {
        deleteDir(getCameraCache(context))
    }
}


fun getTempPhotoSavePath(context: Context, fileName: String): String{
    val file = File(getCameraCacheTemp(context), fileName)
    return file.absolutePath
}

/**
 * 删除目录下的所有文件
 */
fun deleteDir(dir: File) {
    if (dir.isDirectory) {
        val children = dir.listFiles()
        if (children != null && !children.isEmpty()) {
            children.forEach {
                if (it.isFile) {
                    it.delete()
                }
            }
        }
    }
}

fun deleteFiles(file: File?) {
    try {
        if (file!!.exists()) {
            file.delete()
        }
    } catch (e: Exception) {

    }


}

fun deleteFiles(filePath: String?) {
    try {
        val file = File(filePath)
        deleteFiles(file)
    } catch (e: Exception) {

    }
}


/**
 * 删除应用内拍照图片
 */
fun deleteCameraCache(context: Context) {
    ThreadPoolUtil.executor("删除证件信息图片缓存") {
        deleteDir(getCameraCache(context))
    }
}



