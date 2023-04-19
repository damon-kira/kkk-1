package com.common.lib.net.logger

import com.util.lib.log.isDebug
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit


class HttpLogger {
    companion object {
        private const val TAG = "debug_HttpLogger"
        const val END_HTTP = "<-- END HTTP"
        const val END_HTTP_FAILED = "<-- END HTTP FAILED"

        private val logExecutor: ThreadPoolExecutor by lazy {
            val workQueue = LinkedBlockingQueue<Runnable>(100)
            val executor = ThreadPoolExecutor(1, 1, 0, TimeUnit.MILLISECONDS, workQueue)
            executor.rejectedExecutionHandler = ThreadPoolExecutor.CallerRunsPolicy()
            executor
        }
    }
//    private val mMessage = StringBuffer()
    /*override fun log(message: String?) {
        if (AppEnv.DEBUG) {
            message?.let {
                var message = message
                // 请求或者响应开始
                if (message.startsWith("--> POST")) {
                    logger_e(ApiManager.TAG, "-------------start-----------------")
                    mMessage.setLength(0)
                }
                // 以{}或者[]形式的说明是响应结果的json数据，需要进行格式化
                if (message.startsWith("{") && message.endsWith("}") || message.startsWith("[") && message.endsWith("]")) {
                    message = HttpLogHelper.formatJson(HttpLogHelper.decodeUnicode(message))
                }
                mMessage.append(message + "\n")
                // 响应结束，打印整条日志
                if (message.startsWith(END_HTTP_FAILED)) {
                    logger_e(ApiManager.TAG, mMessage.toString())
                    logger_e(ApiManager.TAG, "-------------end-------------------")
                    return
                }
                if (message.startsWith(END_HTTP)) {
                    HttpLogHelper.i(ApiManager.TAG, mMessage.toString())
                    logger_e(ApiManager.TAG, "-------------end-------------------")
                }
            }
        }
    }*/

    fun log(trackNo: String?, logs: Array<String>, failed: Boolean = false) {
        if (isDebug()) {
            val sb = StringBuilder()
            logs.forEach {
                val msg = if (it.startsWith("{") && it.endsWith("}")
                    || it.startsWith("[") && it.endsWith("]")) {
                    HttpLogHelper.formatJson(HttpLogHelper.decodeUnicode(it))
                } else it
                sb.append(msg).append("\n")
            }
            val tag = TAG
            logExecutor.execute {
                if (failed) {
                    HttpLogHelper.e(tag, sb.toString())
                } else HttpLogHelper.i(tag, sb.toString())
            }
        }
    }
}