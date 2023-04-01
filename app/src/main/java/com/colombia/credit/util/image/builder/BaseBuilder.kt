package com.colombia.credit.util.image.builder

import com.colombia.credit.util.image.FunctionManager
import com.colombia.credit.util.image.callback.ResultCallback
import com.colombia.credit.util.image.data.ResultData
import com.colombia.credit.util.image.worker.Worker


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
