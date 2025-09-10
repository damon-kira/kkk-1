package com.kira.learning.network

/**
 * 响应状态码常量定义
 */
object ResponseCode {
    // HTTP 状态码
    const val SUCCESS_CODE = 200
    const val UNAUTHORIZED = 401
    const val FORBIDDEN = 403
    const val NOT_FOUND = 404
    const val BAD_REQUEST = 400
    const val NETWORK_TIMEOUT = 408
    const val SERVER_ERROR = 500

    // 业务状态码
    const val BIZ_SUCCESS = 0
    const val BIZ_ERROR = -1
    const val TOKEN_EXPIRED = 1001
    const val USER_NOT_FOUND = 1002
    const val PERMISSION_DENIED = 1003

    // 添加状态码描述映射
    fun getDescription(code: Int): String = when (code) {
        SUCCESS_CODE -> "请求成功"
        UNAUTHORIZED -> "未授权，请重新登录"
        FORBIDDEN -> "无权限访问"
        NOT_FOUND -> "资源未找到"
        BAD_REQUEST -> "请求参数错误"
        NETWORK_TIMEOUT -> "网络超时"
        SERVER_ERROR -> "服务器内部错误"
        BIZ_SUCCESS -> "业务处理成功"
        BIZ_ERROR -> "业务处理失败"
        TOKEN_EXPIRED -> "Token已过期"
        USER_NOT_FOUND -> "用户不存在"
        PERMISSION_DENIED -> "权限不足"
        else -> "未知错误"
    }
}