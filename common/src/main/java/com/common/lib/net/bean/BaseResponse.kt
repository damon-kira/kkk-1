package com.common.lib.net.bean

import com.common.lib.net.ResponseCode
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


//    /**
//     * 解析强制升级信息
//     *
//     * @return 返回提示信息
//     */
//    fun <T> parsingData(clazz: Class<T>): T? {
//        var bean: T? = null
//        try {
//            val decryptStr = AESNormalUtil.mexicoDecrypt(data!!)
//            if (!decryptStr.isNullOrEmpty()) {
//                bean = GsonUtil.fromJson(decryptStr.orEmpty(), clazz) as? T
//            }
//        } catch (e: JSONException) {
//            logger_e("debug_BaseResponse", "191 error = $e")
//        }
//        return bean
//    }

}