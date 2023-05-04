package com.finance.analysis

import android.content.Context
import androidx.lifecycle.LiveData

/**
 * Created by weishl on 2022/11/24
 *
 */
object FirebaseMsgManager {

    private val mServiceList = arrayListOf<IFirebaseMsgService>()

    private val mIAppsFlyerInfo= arrayListOf<IAppsFlyerInfo>()

    fun addFirebaseMsgService(service: IFirebaseMsgService) {
        mServiceList.add(service)
    }

    fun remove(service: IFirebaseMsgService) {
        mServiceList.remove(service)
    }

    fun getServices() = mServiceList

    fun addIAppFlyerInfo(appsFlyerInfo: IAppsFlyerInfo){
        mIAppsFlyerInfo.add(appsFlyerInfo)
    }

    fun remove(appsFlyerInfo: IAppsFlyerInfo) {
        mIAppsFlyerInfo.remove(appsFlyerInfo)
    }

    fun getAppsFlyerInfoList() = mIAppsFlyerInfo
}

interface IFirebaseMsgService {

    fun onMessageReceived(context: Context, remoteMessage: RemoteMessage)
}

interface IAppsFlyerInfo {

    fun getAppsFlyerId(context: Context): String

    /**
     * json格式:{"channel":"channel","inviteCode":"inviteCode","dpType":"dpType"}
     */
    fun onInviteInfo(json: String)
    /**
     *
     * @return 返回结果是json格式 {"channel":"channel","errorMsg":"errormessage","linkData":"linkData","success":false}
     */
    fun getShortAFLink(context: Context, template: String, uri: String, inviteCode: String, dpType: String, channel: String): LiveData<String>
}