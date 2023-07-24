package com.common.lib.net

import com.util.lib.SysUtils
import okhttp3.Request


class NetBaseParamsManager {
    companion object {

        private var mExternalParamsSupplier: ExternalParamsSupplier? = null

        internal fun setExternalParamsSupplier(supplier: ExternalParamsSupplier) {
            mExternalParamsSupplier = supplier
        }

        /**
         * 获取外部参数对象
         */
        private fun getExternalParamsSupplier(): ExternalParamsSupplier {
            val supplier = mExternalParamsSupplier
            return supplier!!
        }

        //添加header信息
        fun addHeader(builder: Request.Builder, type: String?) {
            val supplier = getExternalParamsSupplier()
            builder.addHeader("Content-type", type ?: "application/json;charset=utf-8")
            // app版本
            builder.addHeader("vMRdV0dUmj", supplier.getAppVersionCode().toString())// app 版本
            // 设备id
            builder.addHeader("NbBH4GIwmz", SysUtils.getDeviceId(supplier.getContext()))
            // 客户端类型
            builder.addHeader("dnpiIILLEI", "android")
            // google广告id
            builder.addHeader("pg77Foy4PL", supplier.getAdvertisingId())
            builder.addHeader("wCxyJuAwkK", supplier.getToken())
        }
    }
}