package com.kira.learning.network

/**
 * 服务器通用响应模型：保持最小字段集合，解析应在网络层完成。
 * 不再承担动态反序列化职责，杜绝运行时反射 + 二次解析带来的隐患。
 */
 data class BaseResponse<T>(
     val code: Int,
     val data: T?,
     val msg: String?,
     val e: Throwable? = null
 ) {
     fun isSuccess(expected: Int = ResponseCode.SUCCESS_CODE): Boolean = code == expected
     fun getErrorCode(): Int = code // 如后续不需要可进一步删除
 }

/**
 * 数据转换扩展：仅转换 data，不改变 code/msg/e。
 * 即便失败(抛异常)也不捕获，交由调用方（保持可预期性）。
 */
inline fun <T, R> BaseResponse<T>.map(transform: (T) -> R): BaseResponse<R> =
    if (data == null) {
        @Suppress("UNCHECKED_CAST")
        BaseResponse(code, null, msg, e)
    } else {
        BaseResponse(code, transform(data), msg, e)
    }
