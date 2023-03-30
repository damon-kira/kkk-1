package com.util.lib

import android.content.Context
import android.util.Log
import com.aes.lib.BuildConfig
import com.aes.lib.MD5Utils
import java.io.*
import java.util.*

/**
 * Created by weishl on 2020/10/28
 *
 */
object FileUtils {
    private val TAG = "Utils"
    private val DEBUG: Boolean = BuildConfig.DEBUG
    private val TIMESTAMP_EXT = ".timestamp"


    @Throws(IOException::class)
    fun copyAssetsFile(
        context: Context,
        assetsFileName: String?,
        destFile: File?
    ) {
        copyFile(context.assets.open(assetsFileName!!), destFile)
    }

    @Throws(IOException::class)
    fun copyFile(`is`: InputStream?, destFile: File?) {
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(destFile)
            val arrayByte = ByteArray(1024)
            var i = 0
            while (`is`!!.read(arrayByte).also { i = it } != -1) {
                fos.write(arrayByte, 0, i)
            }
            fos.flush()
        } finally {
            if (fos != null) {
                try {
                    fos.close()
                } catch (e: Exception) {
                    if (DEBUG) {
                        Log.e(TAG, "copyFiles", e)
                    }
                }
            }
            if (`is` != null) {
                try {
                    `is`.close()
                } catch (e: Exception) {
                    if (DEBUG) {
                        Log.e(TAG, "copyFile", e)
                    }
                }
            }
        }
    }

    /**
     * 在给定路径的后面附加文件或者目录。
     */
    fun pathAppend(path: String, more: String?): String? {
        val buffer = StringBuffer(path)
        if (!path.endsWith("/")) {
            buffer.append('/')
        }
        buffer.append(more)
        return buffer.toString()
    }

    fun makeSurePathExists(path: String?): Boolean {
        val file = File(path)
        return makeSurePathExists(file)
    }

    fun makeSurePathExists(path: File?): Boolean {
        return if (path == null) false else if (path.isDirectory) true else if (!path.exists()) path.mkdirs() else false
    }

    /**
     * 封装这个方法是因为保证使用Application context来调用getSystemService，否则会内存泄漏
     */
    fun getSystemService(context: Context, name: String?): Any? {
        return context.applicationContext.getSystemService(name!!)
    }


    /**
     * 比对assets 和 data/data/包名/files 中的时间戳文件，读取新文件，若files
     * 中不文件，则先将asserts中文件拷贝到files中，避免v5重复下载，消耗无用流量 到 files
     */
    fun openLatestInputFile(
        c: Context,
        filename: String
    ): InputStream? {
        try {
            // 将相关文件复制到data/data/pkg/files中
            val file = File(c.filesDir, filename)
            if (!file.exists()) {
                copyAssetsFile(c, filename, file)
            }
            val timestampFileName = filename + TIMESTAMP_EXT
            val timestampFile = File(c.filesDir, timestampFileName)
            if (!timestampFile.exists()) {
                copyAssetsFile(c, timestampFileName, timestampFile)
            }
        } catch (e: Exception) {
            if (DEBUG) {
//                Log.e(TAG, "copy filter file fail", e);
            }
        }
        var `is`: InputStream? = null
        val timestampOfFile: Long = getFileTimestamp(c, filename)
        val timestampOfAsset: Long = getBundleTimestamp(c, filename)
        if (timestampOfFile >= timestampOfAsset) {
            // files 目录的时间戳更新，那么优先读取 files 目录的文件
            try {
                `is` = c.openFileInput(filename)
                if (DEBUG) {
                    Log.d(
                        TAG,
                        String.format("Opening %s in files directory", filename)
                    )
                }
            } catch (e: Exception) {
                if (DEBUG) {
                    Log.w(
                        TAG,
                        String.format("%s in files directory not found, skip.", filename),
                        e
                    )
                }
            }
        }
        if (`is` == null) {
            // is == null 表明没能从 files 目录读到文件，那么到 assets 目录去读读看
            try {
                `is` = c.assets.open(filename)
                if (DEBUG) {
                    Log.d(TAG, String.format("Opening %s in assets", filename))
                }
            } catch (e: Exception) {
                if (DEBUG) {
                    Log.e(TAG, "$filename in assets not found, open failed!", e)
                }
            }
        }
        return `is`
    }

    /**
     * 读取文件的时间戳
     */
    fun getFileTimestamp(c: Context, filename: String): Long {
        var fis: FileInputStream? = null
        try {
            fis = c.openFileInput(filename + TIMESTAMP_EXT)
        } catch (e: Exception) {
        }
        return if (fis != null) {
            getTimestampFromStream(fis)
        } else {
            0
        }
    }

    private fun getTimestampFromStream(fis: InputStream?): Long {
        var dis: DataInputStream? = null
        try {
            dis = DataInputStream(fis)
            val s = dis.readLine()
            return s.toLong()
        } catch (e: Exception) {
            if (DEBUG) {
                Log.e(TAG, "", e)
            }
        } finally {
            try {
                dis?.close()
                fis?.close()
            } catch (e: Exception) {
                if (DEBUG) {
                    Log.e(TAG, "", e)
                }
            }
        }
        return 0
    }

    // 对于打包的文件，都是放在 assets 目录的，时间戳自然也在 assets 目录
    fun getBundleTimestamp(c: Context, filename: String): Long {
        var fis: InputStream? = null
        try {
            fis = c.assets.open(filename + TIMESTAMP_EXT)
        } catch (e: Exception) {
        }
        return if (fis != null) {
            getTimestampFromStream(fis)
        } else {
            0
        }
    }

    /**
     * 获取length 随机数
     */
    fun getRandomString(length: Int): String { //length表示生成字符串的长度
        val base = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"
        val random = Random()
        val sb = StringBuilder()
        for (i in 0 until length) {
            val number = random.nextInt(base.length)
            sb.append(base[number])
        }
        return sb.toString()
    }


    fun DES_decrypt(passwordToken: String?, password: String?): String {
        return MD5Utils.des_decrypt_hex(passwordToken, password.orEmpty())
    }

    fun DES_encrypt(securityToken: String?, passwd: String?): String {
        return MD5Utils.des_encrypt_hex(securityToken.orEmpty(), passwd.orEmpty()).orEmpty()
    }

}