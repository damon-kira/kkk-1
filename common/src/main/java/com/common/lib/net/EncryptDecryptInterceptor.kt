package com.common.lib.net

import android.util.Log
import com.aes.lib.SignatureManager
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.project.util.AESNormalUtil
import com.util.lib.GsonUtil
import com.util.lib.log.logger_d
import com.util.lib.log.logger_e
import com.util.lib.log.logger_i
import okhttp3.*
import okio.Buffer
import org.json.JSONObject
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
class EncryptDecryptInterceptor : Interceptor {

    companion object {
        val TAG = "EDInterceptor"
        val utf8 = Charsets.UTF_8
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val method = request.method().toUpperCase(Locale.ROOT).trim()
        //post 请求加密处理
        if (method == "POST") {
            request = buildEncryptRequest(request)
        } else if (method == "GET") {
            request = buildEncryptRequestGet(request)
        }

        //解密
        var response = chain.proceed(request)

        if (method == "POST") {
            val decryptResponse = buildDecryptResponse(request, response)
            response = decryptResponse
        }
        return response
    }

    private fun buildEncryptRequestGet(req: Request): Request {
        var request = req
        val url = request.url().toString()
        logger_d(BaseDataAddInterceptor.TAG, "43:buildEncryptRequestGet: reqeust url = $url")
        if (url.contains("data=")) {
            var reqDataNew: String? = ""
            val startIndex = url.indexOf("data=")
            val realUrl = url.substring(0, startIndex)
            val datas = url.substring(startIndex).split("=")
            if (datas.size > 1) {
                val reqBody = datas[1]
                reqDataNew =
                    "data=${URLEncoder.encode(handle(getURLDecoderString(reqBody).orEmpty()))}"
            }
            val newUrl = "$realUrl${reqDataNew.orEmpty()}"
            request = request.newBuilder().url(newUrl).build()
        }
        return request
    }

    /**
     * 加密请求数据
     */
    private fun buildEncryptRequest(req: Request): Request {
        var request = req
        var charset = Charset.forName("UTF-8")
        val requestBody = request.body()

        var requestData = ""
        var contentType: MediaType? = null
        requestBody?.let {
            contentType = requestBody.contentType()
            contentType?.let {
                charset = it.charset(charset)
            }
            try {
                val buffer = Buffer()
                requestBody.writeTo(buffer)
                requestData = buffer.readString(charset)
            } catch (e: Exception) {
            }
        }
        val reqData: JsonObject
        try {
            //请求参数转换为JsonObject
//            val needEncryptData = getURLDecoderString( requestData )?.substring("data=".length)
            reqData = if (requestData.isNullOrEmpty()) JsonObject() else GsonUtil.toJsonObject(requestData) ?: JsonObject()
            val signature = SignatureManager.mexicoSign(reqData)
            reqData.addProperty("signature", signature)
            requestData = reqData.toString()
            logger_e(TAG, "加密前 requestData=$requestData")
//            encryptData = "data=${URLEncoder.encode(handle(reqData.toString()))}"
            val encryptData = if (requestData.isNotEmpty()) {
                handle(requestData)
            } else ""
            logger_d(TAG, "加密后 request data = $encryptData")
            //构建新的请求体
            val newRequestBody = RequestBody.create(contentType, encryptData)
            //构建新的requestBuilder
            val newRequestBuilder = request.newBuilder()
            //根据请求方式构建相应的请求
            request = newRequestBuilder.post(newRequestBody).build()
        } catch (e: Exception) {
            logger_e(TAG, "${request.url()} buildEncryptRequest error：${e.message}")
        }

        return request
    }

    private fun buildDecryptResponse(request: Request, res: Response): Response {
        var response = res
        response.body()?.use {
            val contentType = it.contentType()
            val charset = contentType?.charset(utf8) ?: utf8
            val source = it.source().apply { request(Long.MAX_VALUE) }
            val body = source.buffer().clone().readString(charset)
            logger_i(TAG, "${request.url()}  解密前 body = $body")
            var newResponseBody: ResponseBody = it
            try {
//                val jsonObject = JSONObject(body)
//
//                val obj = JSONObject(body)
//                if (obj.has("data")) {
//                    val encryptStr = obj.optString("data")
//                    val decryptStr = if (jsonObject.optString("code").equals("0")) {
//                        AESNormalUtil.mexicoDecrypt(encryptStr)
//                    }else{
//                        " "
//                    }
//                    obj.put("data", decryptStr)
//                    newResponseBody = ResponseBody.create(contentType, obj.toString())
//                }
                val decrypt = AESNormalUtil.mexicoDecrypt(body, false).orEmpty()
                logger_d(TAG, "解密后 body = $decrypt")
                newResponseBody = ResponseBody.create(contentType, JSONObject(decrypt).toString())
                response = response.newBuilder().body(newResponseBody).build()

            } catch (e: Exception) {
                Log.e(TAG, "buildDecryptResponse error：${e.message} ")
            }
            return response
        }

        return response
    }


    private fun handle(data: String): String {
        logger_i(TAG, " 需加密的参数 $data")
        return AESNormalUtil.mexicoEncrypt(data, false).orEmpty()
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