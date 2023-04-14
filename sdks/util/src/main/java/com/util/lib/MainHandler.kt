package com.util.lib

import android.os.Handler
import android.os.Looper
import android.os.Message

class MainHandler {
    companion object {

        fun post(runnable: Runnable) {
            HanderHolder.handler.post(runnable)
        }
        fun postDelayed(runnable: Runnable,time:Long) {
            HanderHolder.handler.postDelayed(runnable,time)
        }

        fun post(runnable: () -> Unit) {
            Message.obtain()
            HanderHolder.handler.post(runnable)
        }

        fun postDelay(runnable: () -> Unit, delayMillis: Long) {
            HanderHolder.handler.postDelayed(runnable, delayMillis)
        }

        fun remove(runnable: Runnable) {
            HanderHolder.handler.removeCallbacks(runnable)
        }

        fun removeAll() {
            HanderHolder.handler.removeCallbacksAndMessages(null)
        }
    }

    private object HanderHolder{
        val handler = Handler(Looper.getMainLooper())
    }
}