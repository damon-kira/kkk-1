package com.kira.learning.network

/**
 * 统一的错误映射器
 * 将各种错误码和错误信息转换为用户友好的提示
 */
object ErrorMapper {

    /**
     * HTTP错误码映射
     */
    fun mapHttpError(code: Int, originalMessage: String?): String = when (code) {
        400 -> "请求参数错误"
        401 -> "登录已过期，请重新登录"
        403 -> "无权限访问该资源"
        404 -> "请求的资源不存在"
        408 -> "请求超时，请重试"
        409 -> "请求冲突，请稍后再试"
        422 -> "请求数据格式错误"
        429 -> "请求过于频繁，请稍后再试"
        500 -> "服务器内部错误"
        502 -> "网关错误，请稍后再试"
        503 -> "服务暂时不可用"
        504 -> "网关超时，请重试"
        else -> originalMessage ?: "网络请求失败($code)"
    }

    /**
     * 业务错误码映射
     */
    fun mapBusinessError(code: Int?, originalMessage: String?): String = when (code) {
        // 认证相关错误
        10001 -> "用户名或密码错误"
        10002 -> "账户已被锁定"
        10003 -> "账户已被禁用"
        10004 -> "Token已过期，请重新登录"
        10005 -> "Token无效，请重新登录"

        // 权限相关错误
        20001 -> "无权限执行此操作"
        20002 -> "角色权限不足"
        20003 -> "访问被拒绝"

        // 业务逻辑错误
        30001 -> "数据验证失败"
        30002 -> "操作失败，请重试"
        30003 -> "资源不存在或已被删除"
        30004 -> "操作超时，请重试"

        // 文件上传相关错误
        40001 -> "文件大小超出限制"
        40002 -> "文件格式不支持"
        40003 -> "文件上传失败"

        // 支付相关错误
        50001 -> "支付失败"
        50002 -> "余额不足"
        50003 -> "支付超时"

        else -> originalMessage ?: "操作失败，请重试"
    }

    /**
     * 异常类型映射
     */
    fun mapException(throwable: Throwable): String = when (throwable) {
        is java.net.UnknownHostException -> "网络连接失败，请检查网络设置"
        is java.net.ConnectException -> "无法连接到服务器"
        is java.net.SocketTimeoutException -> "连接超时，请重试"
        is javax.net.ssl.SSLException -> "安全连接失败"
        is java.io.FileNotFoundException -> "文件不存在"
        is kotlinx.coroutines.CancellationException -> "操作已取消"
        is kotlinx.serialization.SerializationException -> "数据解析失败"
        is com.google.gson.JsonSyntaxException -> "数据格式错误"
        else -> throwable.message ?: "未知错误"
    }

    /**
     * 网络状态映射
     */
    fun mapNetworkError(errorType: ErrorType): String = when (errorType) {
        ErrorType.NETWORK -> "网络连接异常，请检查网络设置"
        ErrorType.SERVER -> "服务器繁忙，请稍后再试"
        ErrorType.AUTHENTICATION -> "身份验证失败，请重新登录"
        ErrorType.AUTHORIZATION -> "权限不足，无法执行此操作"
        ErrorType.VALIDATION -> "请求数据验证失败"
        ErrorType.TIMEOUT -> "请求超时，请重试"
        ErrorType.UNKNOWN -> "操作失败，请重试"
    }

    /**
     * 根据错误类型获取建议的用户操作
     */
    fun getSuggestedAction(errorType: ErrorType): String = when (errorType) {
        ErrorType.NETWORK -> "请检查网络连接后重试"
        ErrorType.SERVER -> "请稍后再试或联系客服"
        ErrorType.AUTHENTICATION -> "请重新登录"
        ErrorType.AUTHORIZATION -> "请联系管理员获取权限"
        ErrorType.VALIDATION -> "请检查输入信息"
        ErrorType.TIMEOUT -> "请重试"
        ErrorType.UNKNOWN -> "请重试或联系客服"
    }

    /**
     * 检查错误是否可以重试
     */
    fun isRetryable(errorType: ErrorType): Boolean = when (errorType) {
        ErrorType.NETWORK, ErrorType.TIMEOUT, ErrorType.SERVER -> true
        ErrorType.AUTHENTICATION, ErrorType.AUTHORIZATION, ErrorType.VALIDATION -> false
        ErrorType.UNKNOWN -> true
    }
}
