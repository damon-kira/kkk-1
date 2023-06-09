package com.bigdata.lib

class UploadResult {

    companion object {
        const val RESULT_SUCCESS = 100 // 上传成功
        const val RESULT_EXCEPTION = 101 // 异常
        const val RESULT_UPLOADING = 102 //上传中，频繁调用
    }

    var result: Int = -1
    var exception: Exception? = null

    fun isSuccess() = RESULT_SUCCESS == result
}