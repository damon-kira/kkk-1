package com.kira.learning.messaging

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.kira.learning.R

/**
 * Firebase Cloud Messaging (FCM) 集成服务类
 * 处理设备令牌获取和消息接收
 */
class KiraFirebaseMessagingService : FirebaseMessagingService() {

    companion object {
        const val TAG = "FCM_Integration"
        
        // 1. 获取设备令牌
        fun getDeviceToken() {
            // 获取当前设备在FCM注册的唯一标识令牌
            FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // 成功获取令牌
                    val token = task.result
                    Log.d(TAG, "设备FCM令牌: $token")
                    
                    // TODO: 将此令牌发送到你自己的应用服务器进行存储
                    // sendTokenToServer(token)
                } else {
                    // 获取令牌失败
                    Log.w(TAG, "获取FCM令牌失败", task.exception)
                }
            }
        }
    }

    // 2. 当新令牌生成时的回调
    override fun onNewToken(token: String) {
        Log.d(TAG, "FCM令牌已刷新: $token")
        
        // TODO: 将新令牌发送到你的应用服务器进行更新
        // sendTokenToServer(token)
    }

    // 3. 处理接收到的FCM消息
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "收到来自FCM的消息: ${remoteMessage.data}")
        
        // 3.1 检查消息是否包含通知负载
        remoteMessage.notification?.let { notification ->
            // 如果消息包含通知内容，则在本地创建通知
            showNotification(
                notification.title ?: "新消息",  // 通知标题
                notification.body ?: "你有新消息"  // 通知内容
            )
        }

        // 3.2 检查消息是否包含数据负载（自定义键值对）
        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "消息数据负载: ${remoteMessage.data}")
            
            // 示例：处理自定义数据
            val customKey = remoteMessage.data["custom_key"]
            customKey?.let {
                Log.d(TAG, "收到自定义数据: $it")
                // TODO: 根据自定义数据执行操作
            }
        }
    }

    // 4. 在设备上显示通知
    private fun showNotification(title: String, body: String) {
        // 4.1 创建通知渠道（Android 8.0+ 要求）
        val channelId = "default_channel"  // 通知渠道ID
        createNotificationChannel(channelId)

        // 4.2 构建通知
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_logo) // 通知小图标
            .setContentTitle(title)                   // 通知标题
            .setContentText(body)                     // 通知内容
            .setPriority(NotificationCompat.PRIORITY_HIGH) // 通知优先级
            .setAutoCancel(true)                      // 点击后自动取消

        // 4.3 发送通知
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0 /* 通知ID */, notificationBuilder.build())
    }

    // 5. 创建通知渠道（Android 8.0+）
    private fun createNotificationChannel(channelId: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 渠道显示名称（用户可见）
            val channelName = "重要通知"
            // 渠道重要性级别
            val importance = NotificationManager.IMPORTANCE_HIGH
            
            // 创建通知渠道对象
            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = "重要应用通知通道"
            }
            
            // 向系统注册通知渠道
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}