package com.encrypt.module

import com.google.gson.JsonObject
import org.json.JSONObject

object Signature {

    /**
     * 签名
     */
    fun sign(jobj: JsonObject): String {
        //构造签名串
        val build = StringBuilder()

        jobj.entrySet().sortedBy {
            //排序
            it.key
        }.forEach {
            build.append(it.key).append("=").append(it.value.asString).append("&")
            jobj.addProperty(it.key, it.value.asString)
        }
        build.append(AesConstant.AES_SECRET)
        return MD5Helper.getHexMD5(build.toString())//最后MD5加密
    }

    fun sign(jobj: JSONObject): String {
        //构造签名串
        val build = StringBuilder()

        val list = mutableListOf<String>()
        jobj.keys().forEach {
            list.add(it)
        }
        list.sortedBy {
            it
        }.forEach { key ->
            val value = jobj.opt(key)?.toString().orEmpty()
            build.append(key).append("=").append(value).append("&")
            jobj.put(key, value)
        }
        build.append(AesConstant.AES_SECRET)
        return MD5Helper.getHexMD5(build.toString())//最后MD5加密
    }
}