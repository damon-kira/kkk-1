package com.common.lib.net

import android.util.Log
import com.project.util.AESNormalUtil
import com.util.lib.log.logger_d
import com.util.lib.log.logger_i
import okhttp3.*


class DecryptInterceptor : Interceptor {

    companion object {
        val TAG = "EDInterceptor"
        val utf8 = Charsets.UTF_8
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        //解密
        var response = chain.proceed(request)
        val decryptResponse = buildDecryptResponse(request, response)
        response = decryptResponse
        return response
    }


    private fun buildDecryptResponse(request: Request, res: Response): Response {
        var response = res
        response.body()?.use {
            val contentType = it.contentType()
            val charset = contentType?.charset(utf8) ?: utf8
            val source = it.source().apply { request(Long.MAX_VALUE) }
            val body = source.buffer().clone().readString(charset)
            logger_i(TAG, "${request.url()}  body = $body")
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
                val decrypt = AESNormalUtil.mexicoDecrypt(body, false)
                logger_d(TAG,"解密后 decrypt = $decrypt")
                newResponseBody = ResponseBody.create(contentType, decrypt)
                response = response.newBuilder().body(newResponseBody).build()

            } catch (e: Exception) {
                Log.e(TAG, "buildDecryptResponse error：${e.message} ")
            }
            return response
        }

        return response
    }


}