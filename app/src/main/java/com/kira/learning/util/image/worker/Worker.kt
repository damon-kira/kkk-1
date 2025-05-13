package com.kira.learning.util.image.worker

import com.kira.learning.util.image.callback.ResultCallback
import com.kira.learning.util.image.data.ResultData

interface Worker<Result : ResultData> {
    fun start(flowData: ResultData?, callback: ResultCallback<Result>)
}