package com.kira.quickupload.exceptions

import com.kira.quickupload.network.ServerResponse

class UserCancelledUploadException : Throwable("用户已取消上传")
class UploadError(val serverResponse: ServerResponse) : Throwable("上传错误")
class NoNetworkException: Throwable("网络连接断开")
