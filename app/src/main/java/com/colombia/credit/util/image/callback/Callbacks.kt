package com.colombia.credit.util.image.callback

import android.content.Intent


typealias ContainerCallback = (requestCode: Int, resultCode: Int, data: Intent?) -> Unit

interface ResultCallback<R> {
    fun onSuccess(result: R)
    fun onCancel()
    fun onFailed(e: Throwable)
}


abstract class AdapterCallback<R> : ResultCallback<R> {

    override fun onSuccess(result: R) {}

    override fun onCancel() {
    }

    override fun onFailed(e: Throwable) {
        e.printStackTrace()

    }
}
