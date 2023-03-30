package com.util.lib

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream

/**
 * Created by weisl on 2018/6/6.
 */
class GzipUtils {
    companion object {
        fun zip(str: String): ByteArray {
            val out = ByteArrayOutputStream()
            val gzip = GZIPOutputStream(out)
            gzip.write(str.toByteArray())
            gzip.close()
            val gzipArray = out.toByteArray()
            out.close()
            return gzipArray
        }

        fun unzip(data: ByteArray): ByteArray? {
            var unziped: ByteArray? = null
            try {
                val gin = GZIPInputStream(ByteArrayInputStream(data))
                var len = 0
                val buf = ByteArray(2048)
                val out = ByteArrayOutputStream()
                len = gin.read(buf)
                while (len > 0) {
                    out.write(buf, 0, len)
                    len = gin.read(buf)
                }
                gin.close()
                out.close()
                unziped = out.toByteArray()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return unziped
        }
    }
}