package com.colombia.credit.permission

import android.Manifest
import android.content.Context
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import com.colombia.credit.R
import com.util.lib.expand.getExternalCacheFile

abstract class AbsPermissionEntity() {
    //权限名
    abstract fun permissionName(): String

    //检查6.0以下的系统是否开启了此权限
    abstract fun hasPermissionUnder6(context: Context): Boolean

    //如果没有此权限给用户的提示语
    abstract fun getHintIfNoPermission(): Pair<Int, Int>

    open fun hasThisPermission(context: Context): Boolean {
        return if (isSDK_INT_UP_23()) {
            //6.0以上系统
            ContextCompat.checkSelfPermission(
                context,
                permissionName()
            ) == PermissionChecker.PERMISSION_GRANTED
        } else {
            if (isDefaultOpenPermission()) {
                true
            } else {
                hasPermissionUnder6(context)
            }
        }
    }
}

//短信权限
class SmsPermission : AbsPermissionEntity() {
    override fun permissionName(): String {
        return Manifest.permission.READ_SMS
    }

    override fun hasPermissionUnder6(context: Context): Boolean {
        return true
    }

    override fun getHintIfNoPermission(): Pair<Int, Int> {
        return Pair(R.string.permission_sms, R.string.permission_dlg_text_sms)
    }
}

//短信权限
class ReceivePermission : AbsPermissionEntity() {
    override fun permissionName(): String {
        return Manifest.permission.RECEIVE_SMS
    }

    override fun hasPermissionUnder6(context: Context): Boolean {
        return true
    }

    override fun getHintIfNoPermission(): Pair<Int, Int> {
        return Pair(R.string.permission_sms, R.string.permission_dlg_text_sms)
    }
}

//定位权限
class LocationPermission : AbsPermissionEntity() {
    override fun permissionName(): String {
        return Manifest.permission.ACCESS_FINE_LOCATION
    }

    override fun hasPermissionUnder6(context: Context): Boolean {
  /*      var locationHas = false
        val location = LocationHelp.getLocationInfo()
        if (location != null) {
            if (location.longitude.toInt() != -1) {
                locationHas = true
            }
        }*/
        return true
    }

    override fun getHintIfNoPermission(): Pair<Int, Int> {
        return Pair(R.string.permission_location, R.string.permission_dlg_text_location)
    }
}

//相机权限
class CameraPermission : AbsPermissionEntity() {
    override fun permissionName(): String {
        return Manifest.permission.CAMERA
    }

    override fun hasPermissionUnder6(context: Context): Boolean {
//        var has = false
//        var mCamera: Camera? = null
//        try {
//            mCamera = Camera.open()
//            val mParameters = mCamera!!.getParameters() //针对魅族手机
//            mCamera.setParameters(mParameters)
//        } catch (e: Exception) {
//            has = false
//            logger_e("Camera", "e = $e")
//        }
//        if (mCamera != null) {
//            try {
//                logger_i("Camera", "mCamera != null")
//                has = true
//                mCamera.release()
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
        return true
    }

    override fun getHintIfNoPermission(): Pair<Int, Int> {
        return Pair(R.string.permission_camera, R.string.permission_dlg_text_camera)
    }

    override fun hasThisPermission(context: Context): Boolean {
        return if (isSDK_INT_UP_23()) {
            //6.0以上系统
            ContextCompat.checkSelfPermission(
                context,
                permissionName()
            ) == PermissionChecker.PERMISSION_GRANTED
        } else {
            true
        }
    }
}

//相册
class PhotoAlbumPermission: AbsPermissionEntity(){

    override fun permissionName(): String {
        return Manifest.permission.READ_EXTERNAL_STORAGE
    }

    override fun hasPermissionUnder6(context: Context): Boolean {
        return if (isSDK_INT_UP_23()) {
            //6.0以上系统
            ContextCompat.checkSelfPermission(
                context,
                permissionName()
            ) == PermissionChecker.PERMISSION_GRANTED
        } else {
            true
        }
    }

    override fun getHintIfNoPermission(): Pair<Int, Int> {
        return Pair(R.string.permission_album, R.string.permission_dlg_text_album)
    }

}

//联系人权限
class ContactPermission : AbsPermissionEntity() {
    override fun permissionName(): String {
        return Manifest.permission.READ_CONTACTS
    }

    override fun hasPermissionUnder6(context: Context): Boolean {
        return true
    }

    override fun getHintIfNoPermission(): Pair<Int, Int> {
        return Pair(R.string.permission_contact, R.string.permission_dlg_text_contacts)
    }
}

/** 读取日历权限 */
class CalendarReadPermission : AbsPermissionEntity() {
    override fun permissionName(): String {
        return Manifest.permission.READ_CALENDAR
    }

    override fun hasPermissionUnder6(context: Context): Boolean {
        return true
    }

    override fun getHintIfNoPermission(): Pair<Int, Int> {
        return Pair(R.string.permission_calendar, R.string.permission_dlg_text_calendar)
    }
}

/** 写入日历权限 */
class CalendarWritePermission : AbsPermissionEntity() {
    override fun permissionName(): String {
        return Manifest.permission.WRITE_CALENDAR
    }

    override fun hasPermissionUnder6(context: Context): Boolean {
        return true
    }

    override fun getHintIfNoPermission(): Pair<Int, Int> {
        return Pair(R.string.permission_calendar, R.string.permission_dlg_text_calendar)
    }
}

/** 读取IMEI */
class ReadPhonePermission : AbsPermissionEntity() {
    override fun permissionName(): String {
        return Manifest.permission.READ_PHONE_STATE
    }

    override fun hasPermissionUnder6(context: Context): Boolean {
        return true
    }

    override fun getHintIfNoPermission(): Pair<Int, Int> {
        return Pair(R.string.permission_read_phone_state, R.string.permission_dlg_text_readphone)
    }
}

//存储卡权限
class StoragePermission : AbsPermissionEntity() {
    override fun permissionName(): String {
        return Manifest.permission.WRITE_EXTERNAL_STORAGE
    }

    override fun hasPermissionUnder6(context: Context): Boolean {
        var hasStorage = false
        try {
            val file = getExternalCacheFile(context)
            if (file.exists()) {
                hasStorage = true
            }
        } catch (e: Exception) {
            hasStorage = false
        }
        return hasStorage
    }

    override fun getHintIfNoPermission(): Pair<Int, Int> {
        return Pair(R.string.permission_storage, R.string.permission_dlg_text_storage)
    }

}

//账户权限
class AccountPermission : AbsPermissionEntity() {
    override fun permissionName(): String {
        return Manifest.permission.GET_ACCOUNTS
    }

    override fun hasPermissionUnder6(context: Context): Boolean {
        return true
    }

    override fun getHintIfNoPermission(): Pair<Int, Int> {
        return Pair(R.string.permission_account, R.string.permission_dlg_text_account)
    }
}

//app list权限
class AppListPermission : AbsPermissionEntity() {
    override fun permissionName(): String {
        return ""
    }

    override fun hasPermissionUnder6(context: Context): Boolean = true

    override fun getHintIfNoPermission(): Pair<Int, Int> {
        return Pair(R.string.app_list, R.string.app_list_content)
    }

    override fun hasThisPermission(context: Context): Boolean {
        return true
    }
}