package com.util.lib

import android.util.Log
import com.util.lib.log.isDebug
import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicInteger

/**
 * Created by sunsg on 2017/11/17.
 */

class ThreadPoolUtil {
    companion object {
        const val TAG = "ThreadPoolUtil"
        private const val CORE_POOL_SIZE = 3
        private const val MAXIMUM_POOL_SIZE = 128
        private const val KEEP_ALIVE = 1

        private val sThreadFactory = object : ThreadFactory {
            private val mCount = AtomicInteger(1)

            override fun newThread(r: Runnable): Thread {
                return Thread(r, "ThreadPoolUtil #" + mCount.getAndIncrement())
            }
        }

        private val sPoolWorkQueue = LinkedBlockingQueue<Runnable>(10)

        /**
         * An [Executor] that can be used to execute tasks in parallel.
         */
        val THREAD_POOL_EXECUTOR: Executor = ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE,
                KEEP_ALIVE.toLong(), TimeUnit.SECONDS, sPoolWorkQueue, sThreadFactory, ThreadPoolExecutor.DiscardOldestPolicy())

        fun executor(threadName: String,body:()->Unit) {
            THREAD_POOL_EXECUTOR.execute {
                Thread.currentThread().name = threadName
                if(isDebug()){
                    Log.i(TAG,"thread name = ${Thread.currentThread().name}")
                }
                body()
            }
        }

        fun executor(threadName: String,runnable: Runnable) {
            THREAD_POOL_EXECUTOR.execute {
                Thread.currentThread().name = threadName
                if(isDebug()){
                    Log.i(TAG,"thread name = ${Thread.currentThread().name}")
                }
                runnable.run()
            }
        }
    }
}
