package com.common.lib.net.logger

import android.annotation.SuppressLint
import android.util.Log
import com.common.lib.net.ServiceClient

/**
 * Created by mingfeng on 2020-03-27.
 */
@SuppressLint("CheckLogUsageConditional")
object HttpLogHelper {

    private const val TAG = "debug_HttpLogHelper"

    /**
     * 格式化json字符串
     *
     * @param strJson 需要格式化的json串
     * @return 格式化后的json串
     */
    fun formatJson(strJson: String): String {
        // 计数tab的个数
        var tabNum = 0
        val jsonFormat = StringBuffer()
        val length = strJson.length

        var last: Char = 0.toChar()
        for (i in 0 until length) {
            val c = strJson[i]
            if (c == '{') {
                tabNum++
                jsonFormat.append(c + "\n")
                jsonFormat.append(getSpaceOrTab(tabNum))
            } else if (c == '}') {
                tabNum--
                jsonFormat.append("\n")
                jsonFormat.append(getSpaceOrTab(tabNum))
                jsonFormat.append(c)
            } else if (c == ',') {
                jsonFormat.append(c + "\n")
                jsonFormat.append(getSpaceOrTab(tabNum))
            } else if (c == ':') {
                jsonFormat.append("$c ")
            } else if (c == '[') {
                tabNum++
                val next = strJson[i + 1]
                if (next == ']') {
                    jsonFormat.append(c)
                } else {
                    jsonFormat.append(c + "\n")
                    jsonFormat.append(getSpaceOrTab(tabNum))
                }
            } else if (c == ']') {
                tabNum--
                if (last == '[') {
                    jsonFormat.append(c)
                } else {
                    jsonFormat.append("\n" + getSpaceOrTab(tabNum) + c)
                }
            } else {
                jsonFormat.append(c)
            }
            last = c
        }
        return jsonFormat.toString()
    }

    private fun getSpaceOrTab(tabNum: Int): String {
        val sbTab = StringBuffer()
        for (i in 0 until tabNum) {
            sbTab.append('\t')
        }
        return sbTab.toString()
    }


    /**
     * http 请求数据返回 json 中中文字符为 unicode 编码转汉字转码
     *
     * @param theString
     * @return 转化后的结果.
     */
    fun decodeUnicode(theString: String): String {
        var aChar: Char
        val len = theString.length
        val outBuffer = StringBuffer(len)
        var x = 0
        while (x < len) {
            aChar = theString[x++]
            if (aChar == '\\') {
                aChar = theString[x++]
                if (aChar == 'u') {
                    var value = 0
                    for (i in 0..3) {
                        aChar = theString[x++]
                        value = when (aChar) {
                            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> (value shl 4) + aChar.toInt() - '0'.toInt()
                            'a', 'b', 'c', 'd', 'e', 'f' -> (value shl 4) + 10 + aChar.toInt() - 'a'.toInt()
                            'A', 'B', 'C', 'D', 'E', 'F' -> (value shl 4) + 10 + aChar.toInt() - 'A'.toInt()
                            else -> {
                                Log.e(ServiceClient.TAG, "Malformed   \\uxxxx   encoding.")
                            }
                        }
                    }
                    outBuffer.append(value.toChar())
                } else {
                    when (aChar) {
                        't' -> aChar = '\t'
                        'r' -> aChar = '\r'
                        'n' -> aChar = '\n'
                        'f' -> aChar = '\u000C'
                    }
                    outBuffer.append(aChar)
                }
            } else
                outBuffer.append(aChar)
        }
        return outBuffer.toString()
    }

    /**
     * 截断输出日志
     * @param msg
     */
    @JvmStatic
    fun e(tag: String?, msg: String?) {
        logByPriority(Log.ERROR, tag, msg)
    }

    /**
     * 截断输出日志
     * @param msg
     */
    @JvmStatic
    fun i(tag: String?, msg: String?) {
        logByPriority(Log.INFO, tag, msg)
    }

    /**
     * 截断输出日志
     * @param msg
     */
    @JvmStatic
    fun w(tag: String?, msg: String?) {
        logByPriority(Log.WARN, tag, msg)
    }

    /**
     * 截断输出日志
     * @param msg
     */
    @JvmStatic
    fun d(tag: String?, msg: String?) {
        logByPriority(Log.DEBUG, tag, msg)
    }


    /**
     * 截断输出日志
     * @param msg
     */
    @JvmStatic
    fun logByPriority(priority: Int, tag: String?, msg: String?) {
        var msg = msg
        if (tag == null || tag.isEmpty()
            || msg == null || msg.isEmpty()
        )
            return

        val segmentSize = 3 * 1024
        val length = msg.length.toLong()
        if (length <= segmentSize) {// 长度小于等于限制直接打印
            logPrint(priority, tag, msg)
        } else {
            while (msg!!.length > segmentSize) {// 循环分段打印日志
                val logContent = msg.substring(0, segmentSize)
                msg = msg.replace(logContent, "")
                logPrint(priority, tag, logContent)
            }
            logPrint(priority, tag, msg)// 打印剩余日志
        }
    }

    private fun logPrint(priority:Int, tag: String, msg: String) {
        Log.println( priority, tag, msg)
    }
}