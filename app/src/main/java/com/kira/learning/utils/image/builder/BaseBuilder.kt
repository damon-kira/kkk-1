package com.kira.learning.utils.image.builder

import com.kira.learning.utils.image.FunctionManager
import com.kira.learning.utils.image.callback.ResultCallback
import com.kira.learning.utils.image.data.ResultData
import com.kira.learning.utils.image.worker.Worker


abstract class BaseBuilder<Result : ResultData>(protected val functionManager: FunctionManager) {


    abstract fun createWorker(): Worker<Result>
    fun then(): FunctionManager {
        this.functionManager.workerFlows.add(createWorker())
        return this.functionManager
    }

    fun start(callback: ResultCallback<Result>) {
        synchronized(functionManager) {
            this.functionManager.workerFlows.add(createWorker())
            val iterator = functionManager.workerFlows.iterator()
            if (!iterator.hasNext()) {
                return
            }
            realApply(null, iterator, callback)
        }
    }


    private fun realApply(flowData: ResultData?, iterator: MutableIterator<Worker<*>>, callback: ResultCallback<Result>) {
        val worker=  iterator.next()  as Worker<Result>
        worker.start(flowData, object : ResultCallback<Result> {
            override fun onSuccess(result: Result) {
                if (iterator.hasNext()) {
                    iterator.remove()
                    realApply(result, iterator, callback)
                } else {
                    callback.onSuccess(result)
                }
            }

            override fun onFailed(e: Throwable) {
                callback.onFailed(e)
            }

            override fun onCancel() {
                callback.onCancel()
            }
        })
    }
}
