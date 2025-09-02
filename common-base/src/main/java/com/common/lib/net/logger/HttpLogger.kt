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

    fun log(trackNo: String?, logs: Array<String>, failed: Boolean = false) {
        if (isDebug()) {
            val sb = StringBuilder()
            val tag = TAG
            logExecutor.execute {
                if (failed) {
                    HttpLogHelper.e(tag, sb.toString())
                } else HttpLogHelper.i(tag, sb.toString())
            }
        }
    }
}