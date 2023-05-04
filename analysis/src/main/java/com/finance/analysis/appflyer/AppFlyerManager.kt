package com.finance.analysis.appflyer

import android.app.Application
import android.content.Context
import android.net.Uri
import android.text.TextUtils.isEmpty
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.appsflyer.AppsFlyerConversionListener
import com.appsflyer.AppsFlyerLib
import com.appsflyer.CreateOneLinkHttpTask.ResponseListener
import com.appsflyer.deeplink.DeepLink
import com.appsflyer.deeplink.DeepLinkListener
import com.appsflyer.deeplink.DeepLinkResult
import com.appsflyer.share.ShareInviteHelper
import com.finance.analysis.AnalysisSpkey
import com.finance.analysis.FirebaseMsgManager
import com.util.lib.GsonUtil
import com.util.lib.log.isDebug
import com.util.lib.log.logger_d
import com.util.lib.log.logger_e
import com.util.lib.ThreadPoolUtil
import com.util.lib.cache.SharedPrefGlobal
import org.json.JSONObject
import java.net.URLEncoder


class AppFlyerManager {
    companion object {
        var DEBUG = isDebug()
        const val TAG = "AppFlyerManager"
        const val SHARE_INVITE_COE = "invite_code"
        const val SHARE_CHANNEL = "channel"
        const val SHARE_TEMPLATE = "template"
        const val SHARE_DP_TYPE = "dp_type"
        private var appFlyerUid: String = ""
        private var appFlyerUtmSource: String = ""

        fun initAppFlyer(application: Application) {
            try {
                val context = application.applicationContext
                val listener = object : AppsFlyerConversionListener {
                    override fun onConversionDataSuccess(map: MutableMap<String, Any>) {
                        getAppflyerInfo(context, map)
                    }

                    override fun onConversionDataFail(p0: String?) {

                    }

                    override fun onAppOpenAttribution(map: Map<String, String>) {

                    }

                    override fun onAttributionFailure(s: String) {

                    }
                }
                val deepLinkListener = object : DeepLinkListener {
                    override fun onDeepLinking(deepLinkResult: DeepLinkResult) {
                       val deepLink:  DeepLink  = when (deepLinkResult.status) {
                            DeepLinkResult.Status.FOUND -> {
                                logger_d(TAG,"Deep link found")
                                deepLinkResult.deepLink
                            }
                            DeepLinkResult.Status.NOT_FOUND -> {
                                logger_d(TAG, "Deep link not found");
                                null
                            }
                            else -> {
                                val dlError = deepLinkResult.error;
                                logger_d(TAG, "There was an error getting Deep Link data: $dlError");
                                null
                            }
                        } ?: return

                        logger_d(TAG, "The DeepLink data is: $deepLink")
                        val inviteCode = deepLink.getStringValue(SHARE_INVITE_COE).orEmpty()
                        val channel = deepLink.getStringValue(SHARE_CHANNEL).orEmpty()
                        val dpType = deepLink.getStringValue(SHARE_DP_TYPE).orEmpty()
                        val inviteInfo = InviteInfo(inviteCode, channel, dpType)
                        if (inviteCode.isNotEmpty()) {
                            saveInviteInfo(inviteInfo)
                            FirebaseMsgManager.getAppsFlyerInfoList().forEach {
                                it.onInviteInfo(GsonUtil.toJson(inviteInfo).orEmpty())
                            }
//                            uploadInviteCode(inviteInfo)
                        }

                    }
                }
                val appflyerDevKey = "VkvjgJncYgEamPA93z5VuC"
                AppsFlyerLib.getInstance().init(appflyerDevKey, listener, context)
                AppsFlyerLib.getInstance().setDebugLog(DEBUG)
                AppsFlyerLib.getInstance().setCollectIMEI(false)//禁止搜集imei
                AppsFlyerLib.getInstance().subscribeForDeepLink(deepLinkListener)
                AppsFlyerLib.getInstance().start(application, appflyerDevKey)
            } catch (e: ExceptionInInitializerError) {
                if (DEBUG) {
                    Log.e(TAG, "AppsFlyer SDK ExceptionInInitializerError err", e)
                }
            } catch (e: Exception) {
                if (DEBUG) {
                    Log.e(TAG, "AppsFlyer SDK err", e)
                }
            }

        }

        /**
         * 获取appflyer相关信息
         */
        private fun getAppflyerInfo(context: Context, map: MutableMap<String, Any>) {
            ThreadPoolUtil.executor("appflyer") {
                appFlyerUid = AppsFlyerLib.getInstance().getAppsFlyerUID(context).orEmpty()
                if (map.containsKey("media_source")) {
                    appFlyerUtmSource = map["media_source"]?.toString() ?: ""
                } else {
                    if (map.containsKey("af_status")) {
                        appFlyerUtmSource = map["af_status"]?.toString() ?: ""
                    }
                }
                logger_d("AppsFlyer", "$appFlyerUid---------$appFlyerUtmSource")
            }
        }

        fun getAppsFlyerUtmSource() = appFlyerUtmSource

        fun getAppsFlyerUid(context: Context): String {
            if (isEmpty(appFlyerUid)) {
                appFlyerUid = AppsFlyerLib.getInstance().getAppsFlyerUID(context).orEmpty()
            }
            return appFlyerUid
        }

        fun getShortAFLink(context: Context, uri: String, template: String, inviteCode: String, dpType: String, channel:String): LiveData<String> {
            val resultLiveData = MutableLiveData<String>()
            //1.设置模板
            AppsFlyerLib.getInstance().setAppInviteOneLink(template)
            //2.设置参数
            ShareInviteHelper.generateInviteUrl(context)
//                .addParameter("deep_link_value", inviteCode)
                .addParameter(SHARE_INVITE_COE, inviteCode)
                .addParameter(SHARE_CHANNEL, channel)
                .addParameter(SHARE_DP_TYPE, dpType)
                .addParameter("af_dp", uri)
                .addParameter("af_deeplink", "true")
                .generateLink(context, object : ResponseListener {
                    override fun onResponse(linkUrl: String?) {
                        logger_d(TAG, "Invite Link onResponse : $linkUrl")
                        resultLiveData.postValue(GsonUtil.toJson(ShortLinkResult(true, linkUrl.orEmpty(), channel,"")))
                    }

                    override fun onResponseError(msg: String?) {
                        logger_d(TAG, "Invite Link onResponseError : $msg")
                        resultLiveData.postValue(GsonUtil.toJson(ShortLinkResult(false, "", channel, msg.orEmpty())))
                    }
                })
            return resultLiveData
        }

        /**
         * 上传 邀请码
         */
        private fun uploadInviteCode(inviteInfo: InviteInfo) {
//            val disposable = Flowable.just(inviteInfo.inviteCode)
//                .map {
//                    val obj = JsonObject()
//                    obj.addProperty("invite_code", inviteInfo.inviteCode)
//                    obj.addProperty("channel", inviteInfo.channel)
//                    obj.addProperty(SHARE_DP_TYPE, inviteInfo.dpType)
//                    obj.toString()
////                    NetBaseParamsManager.buildDataParmas(obj, true)
//                }
//                .flatMap {
//                    ApiManager.getInstance().getApiDefaultService().uploadInviteCode(it)
//                }
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe({
//                    logger_d(TAG, "邀请码上传结果:success=${it.isSuccess()}")
//                }) {
//                    logger_d(TAG, "邀请码上传异常 ${it.message} ")
//                }
        }

        private fun saveInviteInfo(inviteInfo: InviteInfo){
            val jsonObj =  JSONObject()
            jsonObj.put(SHARE_INVITE_COE, inviteInfo.inviteCode)
            jsonObj.put(SHARE_CHANNEL, inviteInfo.channel)
            jsonObj.put(SHARE_DP_TYPE, inviteInfo.dpType)
            SharedPrefGlobal.setString(AnalysisSpkey.KEY_INVITE_CODE_INFO, jsonObj.toString())
        }

        fun getInviteInfo(): InviteInfo {
            val inviteJson = SharedPrefGlobal.getString(AnalysisSpkey.KEY_INVITE_CODE_INFO, "")
            var inviteCode = ""
            var channel = ""
            var shareType = ""
            if (inviteJson.isNotEmpty()) {
                try {
                    val jsonObj = JSONObject(inviteJson)
                    inviteCode = jsonObj.optString(SHARE_INVITE_COE)
                    channel = jsonObj.optString(SHARE_CHANNEL)
                    shareType = jsonObj.optString(SHARE_DP_TYPE)
                } catch (t: Throwable) {
                    logger_e(TAG, "获取邀请信息异常 ${t.message} ")
                }
            }
            return InviteInfo(inviteCode, channel, shareType)
        }
    }



    class ShortLinkResult(val success:Boolean,val linkData:String, val channel: String, val errorMsg:String)

    class InviteInfo(val inviteCode:String, val channel:String, val dpType: String)





}