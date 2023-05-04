package com.finance.analysis.service

import com.finance.analysis.FirebaseInfo.saveFcmToken
import com.finance.analysis.FirebaseMsgManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.util.lib.log.logger_d
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by weisl on 2019/11/12.
 */
@AndroidEntryPoint
class FirebaseMessageServiceImpl : FirebaseMessagingService() {

    private val TAG = "debug_FirebaseMessgeService"

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        log("From: " + remoteMessage.from)
        log("data>>>>: " + remoteMessage.data.toString())
        FirebaseMsgManager.getServices().forEach {
            it.onMessageReceived(this, com.finance.analysis.RemoteMessage.copyTo(remoteMessage))
        }
//        val extraValue = getExtraValue(remoteMessage.data, NotifyHelper.PushConstant.EXTRAS_KEY)
//        if (extraValue == NotifyHelper.PushConstant.PUSH_CONFIRM_LOAN && !isRepeatLoan()) {//确认放款，刷新等待审核页面，不展示Push消息
//            LiveDataBus.post(HomeEvent(HomeEvent.EVENT_REFRESH))
//            return
//        }
//        remoteMessage.notification?.let { notification ->
//            log("Message Notification Body: " + notification.body.orEmpty())
//            sendNotification(notification.title, notification.body, remoteMessage.data)
//        }
    }


    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        saveFcmToken(p0)
    }

//    private fun getExtraValue(data: Map<String, String>, key: String): String {
//        var value: String = ""
//        if (data.containsKey(key)) {
//            value = data[key].orEmpty()
//        }
//        return value
//    }
//
//    private fun sendNotification(title: String?, messageBody: String?, data: Map<String, String>) {
//        val r = Random()
//        val notifyId = r.nextInt(MAX_VALUE)
//        log(notifyId.toString() + "")
//        val skipIntent = getSkipIntent(data)
//
//        val pendingIntent = PendingIntent.getBroadcast(
//            this, notifyId /* Request code */, skipIntent, PendingIntent.FLAG_ONE_SHOT
//        )
//
//        val channelId = getString(R.string.default_notification_channel_id)
//        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
//        val notificationBuilder = NotificationCompat.Builder(this, channelId)
//            .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
//            .setSmallIcon(R.mipmap.ic_logo_notification)
//            .setContentTitle(title)
//            .setContentText(messageBody)
//            .setColor(resources.getColor(R.color.color_FF31A8F7))
//            .setAutoCancel(true)
//            .setSound(defaultSoundUri)
//            .setContentIntent(pendingIntent)
//
//        val bigText =
//            NotificationCompat.BigTextStyle().bigText(messageBody).setBigContentTitle(title)
//        notificationBuilder.setStyle(bigText)
//
//        val notificationManager =
//            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channel = NotificationChannel(
//                channelId,
//                "Channel human readable title",
//                NotificationManager.IMPORTANCE_DEFAULT
//            )
//            notificationManager.createNotificationChannel(channel)
//        }
//
//        try {
//            MainHandler.post {
//                notificationManager.notify(
//                    notifyId/* ID of notification */,
//                    notificationBuilder.build()
//                )
//            }
//        } catch (e: Exception) {
//            CrashManager.reportException(Exception("firebase message : ", e))
//        }
//    }
//
//    private fun getSkipIntent(data: Map<String, String>): Intent {
//        val id = getExtraValue(data, NotifyHelper.PushConstant.EXTRAS_PUSH_ID)
//        val extraValue = getExtraValue(data, NotifyHelper.PushConstant.EXTRAS_KEY)
//        val url = if (extraValue == NotifyHelper.PushConstant.PUSH_VALUE_H5) {
//            getExtraValue(data, NotifyHelper.PushConstant.EXTRA_URL)
//        } else ""
//
//        val intent = Intent()
//        intent.putExtra(NotifyHelper.PushConstant.EXTRAS_KEY,extraValue)
//        intent.putExtra(NotifyHelper.PushConstant.EXTRAS_PUSH_ID,id)
//        intent.putExtra(NotifyHelper.PushConstant.EXTRA_URL,url)
//
//        intent.setClass(this, com.finance.credit.module.service.NotificationClickReceiver::class.java)
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//        return intent
//    }


    private inline fun log(msg: String) {
        logger_d(TAG, msg)
    }
}