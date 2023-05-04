package com.finance.analysis

import android.net.Uri

/**
 * Created by weishl on 2022/11/23
 *
 */
class RemoteMessage {

    companion object {

        fun copyTo(remoteMsg: com.google.firebase.messaging.RemoteMessage): RemoteMessage  {
            val localMsg = RemoteMessage()
            localMsg.data = remoteMsg.data
            localMsg.from = remoteMsg.from
            val remote = remoteMsg.notification ?: return localMsg
            val temp = Notification()
            temp.title = remote.title
            temp.titleLocKey = remote.titleLocalizationKey
            temp.titleLocArgs = remote.titleLocalizationArgs
            temp.body = remote.body
            temp.bodyLocKey = remote.bodyLocalizationKey
            temp.bodyLocArgs = remote.bodyLocalizationArgs
            temp.icon = remote.icon
            temp.imageUrl = remote.imageUrl?.toString()
            temp.sound = remote.sound
            temp.tag = remote.tag
            temp.color = remote.color
            temp.clickAction = remote.clickAction
            temp.channelId = remote.channelId
            temp.link = remote.link
            temp.ticker = remote.ticker
            temp.notificationPriority = remote.notificationPriority
            temp.visibility = remote.visibility
            temp.notificationCount = remote.notificationCount
            temp.lightSettings = remote.lightSettings
            temp.eventTime = remote.eventTime
            temp.sticky = remote.sticky
            temp.localOnly = remote.localOnly
            temp.defaultSound = remote.defaultSound
            temp.defaultLightSettings = remote.defaultVibrateSettings
            temp.vibrateTimings = remote.vibrateTimings
            localMsg.notification = temp
            return localMsg
        }
    }


    var from: String? = null

    private var data: Map<String, String> = mapOf()

    fun getData(): Map<String, String> = data

    var notification: Notification? = null

    class Notification{
        var title: String? = null
        var titleLocKey: String? = null
        var titleLocArgs: Array<String>? = null
        var body: String? = null
        var bodyLocKey: String? = null
        var bodyLocArgs: Array<String>? = null
        var icon: String? = null
        var imageUrl: String? = null
        var sound: String? = null
        var tag: String? = null
        var color: String? = null
        var clickAction: String? = null
        var channelId: String? = null
        var link: Uri? = null
        var ticker: String? = null
        var notificationPriority: Int? = null
        var visibility: Int? = null
        var notificationCount: Int? = null
        var lightSettings: IntArray? = null
        var eventTime: Long? = null
        var sticky = false
        var localOnly = false
        var defaultSound = false
        var defaultVibrateTimings = false
        var defaultLightSettings = false
        var vibrateTimings: LongArray? = null
    }
}