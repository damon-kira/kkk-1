package com.colombia.credit.permission

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.hardware.Camera
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import com.bigdata.lib.LocationHelp
import com.common.lib.base.BaseActivity
import com.util.lib.MainHandler
import com.util.lib.ThreadPoolUtil
import com.util.lib.log.logger_d
import com.util.lib.log.logger_e
import com.util.lib.log.logger_i


object PermissionHelper {

    private var mCheckPermissionDialog: Dialog? = null

    fun hasCalendar(context: Context): Boolean {
        return CalendarReadPermission().hasThisPermission(context) && CalendarWritePermission().hasThisPermission(
            context
        )
    }

    /** 所有的权限 */
    fun getNeedPermissions(context: Context): List<AbsPermissionEntity> {
        return appPermissions.toList()
    }

    /** 不包含日历权限
     * 在产品首页
     * @param isIncludeCamera 是否包含相机权限
     * */
    fun getExcludeCameraPermission(): ArrayList<AbsPermissionEntity> {
        val list = arrayListOf<AbsPermissionEntity>()
        with(list) {
            add(SmsPermission())
            add(ReadPhonePermission())
            add(LocationPermission())
        }
        return list
    }

    /**
     * 日历权限检查，
     *
     */
    fun checkCalendarPermissionDialog(
        activity: Activity,
        result: (granted: Boolean) -> Unit,
        noPermissionRightListener: () -> Unit = {}
    ) {
        val list = arrayListOf<AbsPermissionEntity>().also {
            it.add(CalendarReadPermission())
            it.add(CalendarWritePermission())
        }
        checkPermissions(list, activity) { deniedList ->
            if (deniedList.isEmpty()) {
                result.invoke(true)
                return@checkPermissions
            }
            val array = Array(deniedList.size) { index ->
                deniedList[index].permissionName()
            }

            activity.reqPermission({ selectCheckbox, isAll ->
                if (selectCheckbox && !isAll) {
                    activity.showNoPermissionDialog(deniedList, {
                        result.invoke(false)
                    }, rightListener = {
                        noPermissionRightListener.invoke()
                    })
                } else {
                    result.invoke(isAll)
                }
            }, *array)
        }
    }

    /**
     * 检查并获取权限，此方法目前只是首贷在身份拍照和活体页面调用
     * @param permissions 需要申请的权限
     * @param result 返回的是 $permissions 未给予的权限
     * @param isNeedShowDialog 是否需要弹权限声明弹窗 true:非复贷会弹  false:不弹
     * */
    fun showDialogIfNeed(
        activity: BaseActivity,
        result: (deniedList: List<AbsPermissionEntity>) -> Unit,
        skipSettingListener: () -> Unit = {}
    ) {
        // 是否显示权限声明弹窗
        if (PermissionDialogManager.getInstance().isShowDialogTips()) {
            checkPermissions(getNeedPermissions(activity) as ArrayList, activity) {
                val hintList = arrayListOf<AbsPermissionEntity>()
                hintList.addAll(appPermissions)
                PermissionDialogManager.getInstance()
                    .showPermissionTipsDialog(hintList, activity, dismiss = {
                        if (activity.isFinishing || activity.isDestroyed) {
                            return@showPermissionTipsDialog
                        }
                        checkPermissions(hintList, activity) { deniedList ->
                            showPermissionDialog(activity, deniedList, false, {
                                fixGroupPermission(activity)
                                result.invoke(hintList.filter {
                                    !it.hasThisPermission(
                                        activity
                                    )
                                })
                            }, skipSettingListener)
                        }
                    })
            }
        } else {
            result.invoke(arrayListOf())
        }
    }

    /**
     * @return 返回未获取的权限
     */
    fun checkPermissions(
        permissions: List<AbsPermissionEntity>,
        context: Context,
        body: (deniedList: ArrayList<AbsPermissionEntity>) -> Unit
    ) {
        ThreadPoolUtil.executor("检测全部权限") {
            val deniedList = arrayListOf<AbsPermissionEntity>()
            permissions.forEach {
                if (!it.hasThisPermission(context)) {
                    deniedList.add(it)
                }
            }
            MainHandler.post {
                body.invoke(deniedList)
            }
        }
    }

    fun checkPermission(context: Context, permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            context, permission
        ) == PermissionChecker.PERMISSION_GRANTED
    }

    /** 申请所有未获取的权限 */
    fun reqAllPermission(
        activity: BaseActivity,
        result: (isAllGranted: Boolean) -> Unit = {},
        skipSettingListener: () -> Unit
    ) {
        activity.checkAllPermission { deniedList: ArrayList<AbsPermissionEntity>, isAllGranted: Boolean ->
            if (activity.isFinishing || activity.isDestroyed) {
                return@checkAllPermission
            }
            fixGroupPermission(activity)
            if (!isAllGranted) {
                showPermissionDialog(
                    activity, deniedList, true, result, skipSettingListener = skipSettingListener
                )
            } else {
                result.invoke(isAllGranted)
            }
        }
    }

    /** 申请未获取的权限 */
    fun reqPermission(
        activity: BaseActivity,
        permissions: List<AbsPermissionEntity>,
        forceDialog: Boolean,
        isFixGroup: Boolean = true,
        result: (isAllGranted: Boolean) -> Unit = {},
        skipSettingListener: () -> Unit
    ) {
        checkPermissions(permissions, activity) { deniedList ->
            if (activity.isFinishing || activity.isDestroyed) {
                return@checkPermissions
            }
            if (isFixGroup) {
                fixGroupPermission(activity)
            }
            if (deniedList.isNotEmpty()) {
                showPermissionDialog(
                    activity,
                    deniedList, forceDialog,
                    result,
                    skipSettingListener = skipSettingListener
                )
            } else {
                result.invoke(true)
            }
        }
    }

    private fun isUIDestroyed(activity: BaseActivity): Boolean =
        activity.isFinishing || activity.isDestroyed


    private fun showPermissionDialog(
        activity: BaseActivity,
        deniedList: ArrayList<AbsPermissionEntity>,
        forceSettingDialog: Boolean,
        result: (isAllGranted: Boolean) -> Unit = {},
        skipSettingListener: () -> Unit
    ) {
        if (isUIDestroyed(activity)) {
            result.invoke(false)
            return
        }
        if (deniedList.isEmpty()) {
            result.invoke(true)
            return
        }

        if (isSDK_INT_UP_23()) {
            for (permission in deniedList) {
                logger_i(TAG, "${Build.BRAND}没有权限:$permission")
            }
            if (deniedList.size > 0) {
                val array = Array<String>(deniedList.size) { i ->
                    deniedList[i].permissionName()
                }
                activity.reqPermission({ isNotAsk: Boolean, isAll: Boolean ->
                    if (isUIDestroyed(activity)) {
                        return@reqPermission
                    }
                    logger_i(TAG, "${Build.BRAND} isNotAsk:$isNotAsk , isAll:$isAll")
                    // 上传imei信息
//                    uploadAuthInfo(true)
                    //statisticPermission(deniedList, getNotGrantedAllPermissionList())

                    if (deniedList.contains(LocationPermission())) {
                        if (!LocationPermission().hasThisPermission(activity)) {
                            LocationHelp.requestLocation()
                        }
                    }
//                    fixCalendarPermission()
                    logger_d(
                        "debug_PermissionHelper",
                        "result = ${(!isAll && isNotAsk && forceSettingDialog || forceSettingDialog)}"
                    )
                    if (!isAll && isNotAsk && forceSettingDialog || forceSettingDialog) {
                        mCheckPermissionDialog = activity.showNoPermissionDialog(deniedList.filter {
                            !it.hasThisPermission(activity)
                        }, cancel = {
                            result.invoke(true)
                        }, rightListener = skipSettingListener
                        )
                    } else {
                        result.invoke(isAll)
                    }
                }, *array)
            }
        } else {
            // 上传imei信息
//            uploadAuthInfo(true)
            mCheckPermissionDialog = activity.showNoPermissionDialog(deniedList) {
                result.invoke(false)
            }
        }
    }

    fun checkCameraPermission(
        activity: BaseActivity,
        function: (hasPermission: Boolean) -> Unit,
        skipSettingListener: () -> Unit
    ) {
        val arrayListOf = arrayListOf(CameraPermission())
        if (isSDK_INT_UP_23()) {
            reqPermission(
                activity,
                arrayListOf,
                true,
                isFixGroup =  true,
                function,
                skipSettingListener = skipSettingListener
            )
        } else {
            checkCameraPermissionUnder6(function)
        }
    }

    /**
     * 检查部分6.0以下手机 相机权限
     */
    fun checkCameraPermissionUnder6(hasPermission: (has: Boolean) -> Unit) {
        ThreadPoolUtil.executor("检查相机权限<6.0", Runnable {
            var has = false
            var mCamera: Camera? = null
            try {
                mCamera = Camera.open()
                val mParameters = mCamera?.parameters //针对魅族手机
                mCamera.parameters = mParameters
            } catch (e: Exception) {
                has = false
                logger_e("Camera", "e = $e")
            }
            if (mCamera != null) {
                try {
                    logger_i("Camera", "mCamera != null")
                    has = true
                    mCamera.release()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            MainHandler.post {
                hasPermission(has)
            }
        })
    }

    private val calendarPermissions =
        arrayOf(Manifest.permission.WRITE_CALENDAR, Manifest.permission.READ_CALENDAR)

    private val contactPermissions =
        arrayOf(/*Manifest.permission.GET_ACCOUNTS, */Manifest.permission.READ_CONTACTS)

    private val readPhonePermission =
        arrayOf(Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_PHONE_NUMBERS)

    private val smsPermissions =
        arrayOf(Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS)

    private val storagePermissions = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
    )


    /** 修复权限组，如果权限组中有一个同意了，在申请其它的权限时，不会出现权限申请弹窗，直接授权 */
    private fun fixGroupPermission(activity: Activity) {
        checkGroupPermission(activity) { result ->
            if (result != null && result.isNotEmpty()) {
                activity.reqPermission({ _, _ -> }, *result.toTypedArray())
            }
        }
    }

    private fun checkGroupPermission(
        activity: Activity, result: (deniedList: List<String>?) -> Unit
    ) {
        if (!isSDK_INT_UP_23()) {
            result.invoke(null)
            return
        }
        val deniedList = arrayListOf<String>()
//        checkGroup(activity, calendarPermissions)?.let {
//            deniedList.addAll(it)
//        }

        checkGroup(activity, contactPermissions)?.let {
            deniedList.addAll(it)
        }

        checkGroup(activity, smsPermissions)?.let {
            deniedList.addAll(it)
        }

        checkGroup(activity, storagePermissions)?.let {
            deniedList.addAll(it)
        }
        checkGroup(activity, readPhonePermission)?.let{
            deniedList.addAll(it)
        }
        result.invoke(deniedList)
    }

    /** @return 如果权限组中的权限没有允许的，则不需要fix组中的权限，如果有一个允许了，再次申请时需要fix组中的权限 */
    private fun checkGroup(activity: Activity, permissions: Array<String>): List<String>? {
        val deniedList = permissions.filter {
            ContextCompat.checkSelfPermission(
                activity, it
            ) == PermissionChecker.PERMISSION_DENIED
        }
        return if (deniedList.size == permissions.size) null else deniedList
    }
}