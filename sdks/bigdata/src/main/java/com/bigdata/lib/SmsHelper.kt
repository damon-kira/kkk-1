package com.bigdata.lib

import android.Manifest
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.Telephony
import com.bigdata.lib.bean.SmsInfo
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
    fun getMessage(context: Context): ArrayList<SmsInfo> {
        val list = arrayListOf<SmsInfo>()

        var cursor: Cursor? = null
        try {
            val resolver = context.contentResolver
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
                var info: SmsInfo
                while (cursor.moveToNext()) {
                    info = SmsInfo()
                    //如果是已发出信息就是收件人手机号 如果是收到的信息就是发件人的手机号
                    info.tel = cursor.getString(0).orEmpty()
                    info.body = cursor.getString(1)//短信具体内容
                    info.time = cursor.getLong(2) / 1000 // 接受时间
                    info.read = cursor.getString(3)//是否阅读0未读，1已读
                    info.status = cursor.getString(4)//短信状态,-1 接收，0 complete,64 pending128 failed
                    info.type = cursor.getString(5)//短信类型1是接收到的，2是已发出
                    list.add(info)
                }
                if (isDebug()) {
                    logger_i(TAG, " getmessage = $list")
                }
            }
        } catch (e: Exception) {
            logger_e(TAG, " getmessage e = $e")
        } finally {
            close(cursor)
        }
        return list
    }
}