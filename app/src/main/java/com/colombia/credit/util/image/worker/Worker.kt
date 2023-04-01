package com.colombia.credit.util.image.worker

import com.colombia.credit.util.image.callback.ResultCallback
import com.colombia.credit.util.image.data.ResultData

interface Worker<Result : ResultData> {
    fun start(flowData: ResultData?, callback: ResultCallback<Result>)
}