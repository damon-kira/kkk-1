package com.kira.quickupload.observer.task

import com.kira.quickupload.data.UploadInfo
import com.kira.quickupload.network.ServerResponse

interface UploadTaskObserver {
    fun onWait(info: UploadInfo)

    fun onProgress(
        info: UploadInfo
    )

    fun onSuccess(
        info: UploadInfo,
        response: ServerResponse
    )

    fun onError(
        info: UploadInfo,
        exception: Throwable
    )

    fun onCompleted(
        info: UploadInfo
    )
}
