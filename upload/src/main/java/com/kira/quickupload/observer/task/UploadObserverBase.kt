package com.kira.quickupload.observer.task

import com.kira.quickupload.UploadService
import com.kira.quickupload.data.UploadInfo
import com.kira.quickupload.data.UploadStatus
import com.kira.quickupload.network.ServerResponse
import com.kira.quickupload.quick.QuickUploadRequest

open class UploadObserverBase(open val uploadId: String) : UploadTaskObserver {
    private var callback: ((UploadStatus, UploadInfo, Throwable?, ServerResponse?) -> Unit)? = null
    fun refresh(callback: ((UploadStatus, UploadInfo, Throwable?, ServerResponse?) -> Unit)) {
        this.callback = callback
    }

    /**
     * 请求
     */
    var quickUploadRequest: QuickUploadRequest? = null

    /**
     * 上传返回信息
     */
    var uploadInfo: UploadInfo = UploadInfo("")
    var status: UploadStatus = UploadStatus.DEFAULT
    var exception: Throwable? = null
    var serverResponse: ServerResponse? = null

    open fun startUpload() {
        quickUploadRequest?.setUploadID(uploadId)
        quickUploadRequest?.startUpload()
    }

    open fun stopUpload() {
        UploadService.stopUpload(uploadId)
    }

    open fun notifyChange() {
        // 子类可以覆盖通知逻辑
//        Handler(Looper.getMainLooper()).post {
        callback?.invoke(status, uploadInfo, exception, serverResponse)
//        }
    }

    override fun onWait(info: UploadInfo) {
        uploadInfo = info
        status = UploadStatus.Wait
        notifyChange()
    }

    override fun onProgress(info: UploadInfo) {
        uploadInfo = info
        status = UploadStatus.InProgress
        notifyChange()
    }

    override fun onSuccess(info: UploadInfo, response: ServerResponse) {
        uploadInfo = info
        status = UploadStatus.Success
        serverResponse = response
        notifyChange()
    }

    override fun onError(info: UploadInfo, exception: Throwable) {
        uploadInfo = info
        status = UploadStatus.Error
        this.exception = exception
        notifyChange()
    }

    override fun onCompleted(info: UploadInfo) {
        uploadInfo = info
        if (uploadInfo.progressPercent == 100) {
            status = UploadStatus.Completed
        }
        notifyChange()
    }
}