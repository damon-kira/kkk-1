package com.common.lib.net

import com.aes.lib.SignatureManager
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.util.lib.GsonUtil
import com.util.lib.log.logger_d
import com.util.lib.log.logger_e
import com.util.lib.log.logger_i
import okhttp3.*
import okio.Buffer
import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.Charset
import java.util.*

/**
 *@author zhujun
 *@description:
 *@date : 2022/9/1 10:13 上午
 */
class BaseDataAddInterceptor : Interceptor {

    companion object {
        val TAG = "DataInterceptor"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val method = request.method().toUpperCase(Locale.ROOT).trim()
        //post 请求加密处理
        if (method == "POST") {
            request = buildBaseDataRequest(request)
        } else if (method == "GET") {
            request = buildBaseDataRequestGet(request)
        }
        return chain.proceed(request)
    }

    private fun buildBaseDataRequestGet(req: Request) : Request {
        var request = req
        val url = request.url().toString()
        logger_d(TAG, "43:buildBaseDataRequestGet: reqeust url = $url")
        if (url.contains("data=")) {
            var reqDataNew: String? = ""
            val startIndex = url.indexOf("data=")
            val realUrl = url.substring(0, startIndex)
            val datas = url.substring(startIndex).split("=")
            if (datas.size > 1) {
                val body = datas[1]
                val requestData = getURLDecoderString(body)
                val dataJobj = requestData?.let { GsonUtil.toJsonObject(it) } ?: JsonObject()
                logger_d(TAG, "52:buildBaseDataRequestGet: needEncryptData = $dataJobj")
                val jsonElement = Gson().toJsonTree(dataJobj)
                reqDataNew = if (jsonElement is JsonObject) {
                    val isNeedBaseHeaders = request.header("isNeedBase")
                    val isNeedBase = !(isNeedBaseHeaders!=null && isNeedBaseHeaders == "false")
                    val jsonObj = appendParams(jsonElement,isNeedBase)
                    jsonObj.toString()
                } else {
                    requestData
                }
                reqDataNew = "data=${URLEncoder.encode(reqDataNew)}"
            }
            val newUrl = "$realUrl${reqDataNew.orEmpty()}"
            request = request.newBuilder().url(newUrl).build()
        } else {
            val jobj = appendParams(JsonObject(), true)
            val reqData= "data=${URLEncoder.encode(jobj.toString())}"
            val newUrl = if (url.contains("?")) {
                "$url&$reqData"
            } else {
                "$url?$reqData"
            }
            request = request.newBuilder().url(newUrl).build()
        }
        return request
    }

    /**
     * 添加必传参数数据
     */
    private fun buildBaseDataRequest(req: Request): Request {
        var request = req
        var charset = Charset.forName("UTF-8")
        val requestBody = request.body()

        var requestData = ""
        var contentType: MediaType? = null
        requestBody?.let {
            contentType = requestBody.contentType()
            logger_i(TAG,"contentType $contentType ")
            contentType?.let {
                charset = it.charset(charset)
                if (it.type().toLowerCase() == "multipart") {
                    return request
                }
            }
            try {
                val buffer = Buffer()
                requestBody.writeTo(buffer)
                requestData = buffer.readString(charset)
            } catch (e: Exception) {
            }
        }
        val reqData: JsonObject
        var reqDataNew: String?
        try {
            logger_i(TAG,"requestData $requestData ")
            //请求参数转换为JsonObject
            val needEncryptData = getURLDecoderString( requestData )?.substring("data=".length)
            reqData = if (needEncryptData.isNullOrEmpty()) JsonObject() else GsonUtil.toJsonObject(needEncryptData) ?: JsonObject()
            logger_i(TAG,"needEncryptData $needEncryptData  reqData: $reqData")

            val jsonElement = Gson().toJsonTree(reqData)
            reqDataNew = if (jsonElement is JsonObject) {
                val isNeedBaseHeaders = request.header("isNeedBase")
                val isNeedBase = !(isNeedBaseHeaders!=null && isNeedBaseHeaders == "false")
                val jsonObj = appendParams(jsonElement,isNeedBase)
                jsonObj.toString()
            } else {
                requestData
            }

            reqDataNew = "data=${URLEncoder.encode(reqDataNew)}"
            //构建新的请求体
            val newRequestBody = reqDataNew.let { RequestBody.create(contentType, it) }
            //构建新的requestBuilder
            val newRequestBuilder = request.newBuilder()
            //根据请求方式构建相应的请求
            request = newRequestBuilder.post(newRequestBody).build()
        } catch (e: Exception) {
            logger_e(TAG, "${request.url()} buildBaseDataRequest error：${e.message}")
        }

        return request
    }


    /**
     * @param source 原始参数
     * @param isNeedBase 添加额外参数 在ApiService 设置请求头 @Headers("isNeedBase:false") 为不传base信息
     */
    private fun appendParams(source: JsonObject, isNeedBase: Boolean = true): JsonObject {
//        //添加必填签名信息
//        NetBaseParamsManager.getMustCashMustParams(source)
//        //添加base签名信息
//        if (isNeedBase) {
//            NetBaseParamsManager.getCashNormalBaseParams(source)
//        }
        //2.添加签名json
        source.addProperty("signature", SignatureManager.mexicoSign(source))
        return source
    }


    private fun getURLDecoderString(str: String?): String? {
        var result: String? = ""
        if (null == str) {
            return ""
        }
        try {
            result = URLDecoder.decode(str, "UTF-8")
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        return result
    }
}