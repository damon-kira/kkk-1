package com.common.lib.net.bean

import com.common.lib.net.ResponseCode
import com.project.util.AESNormalUtil
import com.util.lib.GsonUtil
import com.util.lib.log.logger_e
import org.json.JSONException
import java.lang.reflect.ParameterizedType

/**
 * Created by weisl on 2019/8/12.
 */
class BaseResponse<T> @JvmOverloads constructor(
    var code: Int,
    var data: String?,
    val message: String?,
    val e: Throwable? = null
) {
    var t: T? = null

    fun parseT(clazz: Class<T>): T? {
        if (t == null) {
            try {
                t = GsonUtil.fromJson(data!!, clazz) as? T
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

    fun getData(): T? = t

    fun isSuccess() = code == ResponseCode.SUCCESS

    /** 没有活动信息 */
    fun isActivitiesFailed() = code == ResponseCode.ERROR_ACTIVITIES_FAILED

    /** 没有banner信息 */
    fun isBannerFailed() = code == ResponseCode.ERROR_BANNER_FAILED

    fun isAppForcedUpdate(): Boolean {
        return code == ResponseCode.FORCED_UPDATE_CODE
    }

    fun isCouponInvalid() = code == ResponseCode.ERROR_COUPON_INVALID

    fun getErrorCode(): Int = code


    /**
     * 解析强制升级信息
     *
     * @return 返回提示信息
     */
    fun <T> parsingData(clazz: Class<T>): T? {
        var bean: T? = null
        try {
            val decryptStr = AESNormalUtil.mexicoDecrypt(data!!)
            if (!decryptStr.isNullOrEmpty()) {
                bean = GsonUtil.fromJson(decryptStr.orEmpty(), clazz) as? T
            }
        } catch (e: JSONException) {
            logger_e("debug_BaseResponse", "191 error = $e")
        }
        return bean
    }

}