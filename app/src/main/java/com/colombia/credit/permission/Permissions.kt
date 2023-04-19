package com.colombia.credit.permission

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Build
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import androidx.annotation.ColorRes
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import com.colombia.credit.R
import com.common.lib.dialog.DefaultDialog
import com.common.lib.dialog.DialogManager
import com.tbruyelle.rxpermissions2.RxPermissions
import com.util.lib.MainHandler
import com.util.lib.ThreadPoolUtil
import com.util.lib.log.isDebug
import com.util.lib.log.logger_i

/**
 * Created by weisl on 2019/10/18.
 */

val TAG = "debug_Permissions"

//需要新权限只需在此数组添加即可
val appPermissions = arrayOf(
    SmsPermission(),
    ReceivePermission(),
    ContactPermission(),
    ReadPhonePermission(),
    LocationPermission(),
    AccountPermission(),
    CameraPermission(),
    PhotoAlbumPermission(),
    CalendarReadPermission(),
    CalendarWritePermission()
)
//是否是6.0以及以上系统

fun isSDK_INT_UP_23(): Boolean {
    return Build.VERSION.SDK_INT >= 23
}

//是否默认打开了权限
fun isDefaultOpenPermission(): Boolean {
    var default = true
    if (isXiaomi() || isLenovo()) {
        default = false
    }
    logger_i(TAG, "is default open permission ============== $default")
    return default
}

//是否是小米手机
private fun isXiaomi(): Boolean {
    return Build.BRAND.toUpperCase() == "XIAOMI"
}

// 是否是联想手机
private fun isLenovo(): Boolean {
    return Build.BRAND.toUpperCase() == "LENOVO"
}

/**
 * 权限检测
 */
fun Activity.checkAllPermission(
    body: (deniedList: ArrayList<AbsPermissionEntity>, isAll: Boolean) -> Unit
    = { list: ArrayList<AbsPermissionEntity>, isAll: Boolean -> }
) {
    ThreadPoolUtil.executor("检测全部权限") {
//        val deniedList = ArrayList<AbsPermissionEntity>()
//        for (per in appNeedPermissions) {
//            if (!per.hasThisPermission()) {
//                isAllPermission = false
//                deniedList.add(per)
//            }
//        }
        val deniedList = PermissionHelper.getNeedPermissions(this) as ArrayList
        val isAllPermission = deniedList.isEmpty()
        logger_i(TAG, "有 联系人 短信 通话记录 定位权限: $isAllPermission")
        MainHandler.post {
            body(deniedList, isAllPermission)
        }
    }
}


fun Activity.showCheckCameraPermissionDialog(
    onAllGranted: (isAllGranted: Boolean) -> Unit = {},
    showNoPermissionListener: () -> Unit
) {
    checkCameraAndStoragePermission() { deniedList: ArrayList<AbsPermissionEntity>, isAllGranted: Boolean ->
        if (!isAllGranted) {
            if (isSDK_INT_UP_23()) {
                for (permission in deniedList) {
                    logger_i(TAG, "${Build.BRAND}没有权限:$permission")
                }
                if (deniedList.size > 0) {
                    var array = Array<String>(deniedList.size) { i ->
                        deniedList[i].permissionName()
                    }
                    reqPermission({ isNotAsk: Boolean, isAll: Boolean ->
                        logger_i(TAG, "${Build.BRAND} isNotAsk:$isNotAsk , isAll:$isAll")
                        if (!isAll && isNotAsk) {
                            showNoPermissionListener.invoke()
//                            showNoPermissionDialog(
//                                context,
//                                getNotGrantedCameraAndStoragePermissionList(context),
//                                cancel = {
//                                    onAllGranted(false)
//                                })
                        } else if (isAll) {
                            onAllGranted(true)
                        } else {
                            onAllGranted(false)
                        }
                    }, *array)
                }
            } else {
                showNoPermissionListener.invoke()
//                showNoPermissionDialog(context, deniedList, cancel = {
//                    onAllGranted(false)
//                })
            }
        } else {
            onAllGranted(true)
        }
    }
}


/**
 * 检测相机和存储权限
 */
fun Activity.checkCameraAndStoragePermission(
    body: (deniedList: ArrayList<AbsPermissionEntity>, isAll: Boolean) -> Unit
    = { list: ArrayList<AbsPermissionEntity>, isAll: Boolean -> }
) {
    val deniedList = ArrayList<AbsPermissionEntity>()
    var isAllPermission = true
    val permissions =
        arrayOf(CameraPermission())
    for (per in permissions) {
        if (!per.hasThisPermission(this)) {
            isAllPermission = false
            deniedList.add(per)
        }
    }
    logger_i(TAG, "is default open all permission:${isAllPermission}")
    MainHandler.post {
        body(deniedList, isAllPermission)
    }
}


fun Activity.getNotGrantedCameraAndStoragePermissionList(): ArrayList<AbsPermissionEntity> {
    val list = ArrayList<AbsPermissionEntity>()
    val array = arrayOf(CameraPermission())
    for (permission in array) {
        if (!permission.hasThisPermission(this)) {
            list.add(permission)
        }
    }
    return list
}

@SuppressLint("CheckResult")
fun Activity.reqPermission(
    body: (selectCheckbox: Boolean, isAll: Boolean) -> Unit,
    vararg permissions: String
) {
    if (isSDK_INT_UP_23()) {
        // 如果tag存在则先移除RxPermissionsFragment，为了修复某些机型权限无法获取的bug (手机:moto e6 play)
        // RxPermissions 227行 mRxPermissionsFragment.getSubjectByPermission(permission) 会获取到有PublishSubject，
        // 在228行 unrequestedPermissions.isEmpty() 会为true，所以不会请求权限
        removeRxPermissions(this)
        val rxPermissions = RxPermissions(this)
        rxPermissions.setLogging(isDebug())
        rxPermissions.requestEachCombined(*permissions)
            .subscribe({ permission ->
                var isNotAskChecked = false
                if (!permission.granted) {
                    for (p in permissions) {
                        if (isNotAskChecked(p)) {
                            isNotAskChecked = true
                            break
                        }
                    }
                }
                body(isNotAskChecked, permission.granted)
            }, {
                body(false, false)
            })
    } else {
        body(false, true)
    }
}

private fun removeRxPermissions(activity: Activity) {
    val tag = "RxPermissions"
    val fm = activity.fragmentManager
    val tagFragment = fm.findFragmentByTag(tag)
    if (tagFragment != null && tagFragment.isAdded) {
        fm.beginTransaction().remove(tagFragment).commitAllowingStateLoss()
        try {
            fm.executePendingTransactions()
        } catch (e: Exception) {
        }
    }
}

/**
 * 用户是否勾选了不再提示
 */
inline fun Activity.isNotAskChecked(permission: String): Boolean {
    return !ActivityCompat.shouldShowRequestPermissionRationale(this, permission) &&
            ContextCompat.checkSelfPermission(
                this,
                permission
            ) == PermissionChecker.PERMISSION_DENIED
}

/**
 * 权限对话框
 */
fun Activity.showNoPermissionDialog(
    deniedList: List<AbsPermissionEntity>,
    cancel: () -> Unit = {},
    rightListener: () -> Unit = {}
): DefaultDialog? {

    if (deniedList.isEmpty()) {
        cancel.invoke()
        return null
    }

    logger_i("dialog", "show permission dialog")

    val connectorChar = this.getString(R.string.permission_connector_char)

    val notPermissiontText = getNotPermissionText(deniedList, this, connectorChar)
    val message = this.getString(R.string.permission_dialog_message, notPermissiontText)

    val dialog = HintDialog(this)
        .showClose(false)
        .setMessage(message)
        .setOnClickListener { rightListener.invoke() }
    dialog.show()
    return dialog
}

fun Activity.showDialog(
    message: String?,
    left: String,
    right: String,
    leftListener: () -> Unit = {},
    rightListener: () -> Unit = {},
    view: View? = null,
    cancelable: Boolean = true
): Dialog? {
    if (isFinishing) {
        return null
    }
    return DialogManager.Builder().messageContent(message)
        .leftButton(left)
        .rightButton(right)
        .leftListener(leftListener)
        .rightListener(rightListener)
        .view(view)
        .isCancelable(cancelable)
        .builder()
        .show(this)
}

fun getNotPermissionText(
    deniedList: List<AbsPermissionEntity>,
    context: Context,
    connectorChar: String?
): String {
    val linkSet = LinkedHashSet<String>()
    for (index in deniedList.indices) {
        val permission = deniedList[index]
        linkSet.add(context.getString(permission.getHintIfNoPermission().first))
    }
    /*if (isXiaomi()) {
        if (buffer.isEmpty()) {
            buffer.append(context.getString(R.string.xiaomi_notify_sms))
        } else {
            buffer.append(connectorChar).append(context.getString(R.string.xiaomi_notify_sms))
        }
    }*/
    return linkSet.joinToString(connectorChar ?: "")
}


fun getNotPermissionText2(
    deniedList: List<AbsPermissionEntity>,
    context: Context,
    connectorChar: String?
): String {
//    val callLogPermission = getCallLogPermission()
    // 读Call Log和读取设备信息(同一组权限)，如果两个都有，则干掉一个，避免重复添加提示文案
    /*   if (deniedList.contains(callLogPermission)
               && deniedList.contains(Manifest.permission.READ_PHONE_STATE)) {
           deniedList.remove(Manifest.permission.READ_PHONE_STATE)
       }*/

    val linkSet = LinkedHashSet<String>()
    for (index in deniedList.indices) {
        val permission = deniedList[index]
        linkSet.add(context.getString(permission.getHintIfNoPermission().first))
    }
/*
    if (isXiaomi()) {
        if (buffer.isEmpty()) {
            buffer.append(context.getString(R.string.xiaomi_notify_sms))
        } else {
            buffer.append(connectorChar).append(context.getString(R.string.xiaomi_notify_sms))
        }
    }*/

    return linkSet.joinToString(connectorChar ?: "")
}

fun setAgreementClickableSpan(
    @ColorRes colorRes: Int = R.color.colorPrimary,
    context: Context,//防止内存泄漏，上下文对象使用applicationContext
    spanText: SpannableStringBuilder,
    clickableText: String,
    onClick: () -> Unit
) {
    val index = spanText.indexOf(clickableText)
    if (index < 0) return
    spanText.setSpan(object : ClickableSpan() {
        override fun onClick(view: View) {
            onClick?.invoke()
        }

        override fun updateDrawState(paint: TextPaint) {
            super.updateDrawState(paint)
            paint.color = ContextCompat.getColor(context, colorRes)
        }
    }, index, index + clickableText.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
}