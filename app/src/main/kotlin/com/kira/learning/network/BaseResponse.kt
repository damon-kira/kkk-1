package com.kira.learning.network

import com.util.lib.GsonUtil
import java.lang.reflect.ParameterizedType

class BaseResponse<T> @JvmOverloads constructor(
    var code: Int,
    var data: T?,
    val msg: String?,
    val e: Throwable? = null
) {
    var t: T? = null

    fun parseT(clazz: Class<T>): T? {
        if (t == null) {
            try {
                t = GsonUtil.fromJson(data!!.toString(), clazz) as? T
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return t
    }

    private fun <T> getType(): Class<out Any?> {
        val genericType = this.javaClass.genericSuperclass
        if (genericType is ParameterizedType) {
            val type = genericType.actualTypeArguments[0]
            if (type is Class<*>) {
                return Any::class.java
            }
        }
        return Any::class.java
    }

    @JvmName("getData1")
    fun getData(): T? = data

    fun isSuccess() = code == ResponseCode.SUCCESS_CODE

    fun getErrorCode(): Int = code

}