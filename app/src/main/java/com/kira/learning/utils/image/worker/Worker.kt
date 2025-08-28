package com.kira.learning.utils.image.worker

import com.kira.learning.utils.image.callback.ResultCallback
import com.kira.learning.utils.image.data.ResultData

interface Worker<Result : ResultData> {
    fun start(flowData: ResultData?, callback: ResultCallback<Result>)
}