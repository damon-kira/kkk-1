package com.finance.analysis.push

import android.content.Context

/**
 * author: weishl
 * data: 2021/3/4
 **/
object PushManagerFactory: IPushManager {

    private val firebaesManager by lazy {
        FirebaseManager()
    }

//    private var huaweiManager: IPushManager? = null

    private var mCurrManager: IPushManager? = null

    @Synchronized
    private fun getCurrManager(): IPushManager? {
        var manager = mCurrManager
        if (manager != null) return manager
        manager = firebaesManager
//        if (!googleIsAvailable(context) && ChannelHelper.isHuawei()) {
//            huaweiManager = getHuaweiMananger()
//            manager = huaweiManager
//        }
        mCurrManager = manager
        return manager
    }

    override fun init(context: Context) {
        getCurrManager()?.init(context)
    }

    override fun getGaid(context: Context): String? {
        return getCurrManager()?.getGaid(context)
    }

    override fun reportException(t: Throwable) {
        getCurrManager()?.reportException(t)
    }

    override fun getChannel(): Int = getCurrManager()?.getChannel() ?: 0

    override fun skipAppStore(context: Context, jumpAddress: String?) {
        getCurrManager()?.skipAppStore(context, jumpAddress)
    }

    override fun getAppInstanceId(context: Context): String {
        return mCurrManager?.getAppInstanceId(context).orEmpty()
    }

//    private fun getHuaweiMananger(): IPushManager {
//        try {
//            return Class.forName("HuaweiManager").newInstance() as IPushManager
//        } catch (e: Exception) {
//        }
//        return firebaesManager
//    }
}