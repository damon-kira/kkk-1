package com.colombia.credit.permission

import android.app.Activity
import android.app.Dialog
import com.cache.lib.SharedPrefGlobal
import com.colombia.credit.R
import com.colombia.credit.manager.SharedPrefKeyManager
import com.util.lib.log.logger_d

class PermissionDialogManager {

    companion object {
        private val _instance = PermissionDialogManager()

        fun getInstance()= _instance

        /** 是否显示过权限弹窗 */
        const val KEY_SHOW_PERMISSION_DIA_FLAG: String = "key_show_permission"
    }

    private var mPermissionTipsDialog: Dialog? = null
    private var mCheckPermissionDialog: Dialog? = null

    fun showPermissionTipsDialog(
        deniedList: ArrayList<AbsPermissionEntity>,
        activity: Activity,
        dismiss: () -> Unit
    ) {
        if (deniedList.isEmpty()) {
            dismiss.invoke()
            return
        }

        // 添加APPlist和相册声明
        val tempList = arrayListOf<AbsPermissionEntity>()
        tempList.addAll(deniedList)
        if (tempList.size >= 3) {
            tempList.add(3, AppListPermission())
        }

        if (mPermissionTipsDialog?.isShowing == true) {
            mPermissionTipsDialog?.setOnDismissListener(null)
            mPermissionTipsDialog?.dismiss()
            mPermissionTipsDialog = null
        }

        // 如果有任何权限没有赋予，则弹出提示，告诉用户我们需要权限，然后再弹出权限申请对话框
        val symbolChar = ", "
//        var notPermissionText = getNotPermissionText2(tempList, activity, symbolChar)
//        val lastIndex = notPermissionText.lastIndexOf(symbolChar)
//        if (lastIndex >= 0) {
//            notPermissionText = notPermissionText.replaceRange(
//                lastIndex,
//                lastIndex + (symbolChar.length),
//                " " + activity.getString(R.string.permission_dlg_text_last_and_text) + " "
//            )
//        }
//        val message = activity.getString(R.string.permission_dlg_text1, notPermissionText)

        mPermissionTipsDialog = PermissionDialog(activity).also {
            it.setData(
                {
                    mPermissionTipsDialog?.dismiss()
                },
                tempList
            )
        }
        mPermissionTipsDialog?.setOnDismissListener {
            mPermissionTipsDialog?.setOnKeyListener(null)
            mPermissionTipsDialog?.setOnDismissListener(null)
            dismiss.invoke()
        }
        if (!activity.isFinishing) {
            mPermissionTipsDialog?.show()
            setShowDialogTips(true)
        }
    }


    /** 是否显示 */
    fun isShowDialogTips(): Boolean {
        val boolean =
            SharedPrefGlobal.getBoolean(SharedPrefKeyManager.KEY_SHOW_PERMISSION_DIA_FLAG, true)
        logger_d("logout", "是否已经显示过>>>$boolean")
        return !boolean
//        return true
    }

    /**
     * @param isShow 设置下一次是否显示
     */
    fun setShowDialogTips(isShow: Boolean) {
        SharedPrefGlobal.setBoolean(KEY_SHOW_PERMISSION_DIA_FLAG, isShow)
    }


    fun onDestroy() {
        mPermissionTipsDialog?.setOnDismissListener(null)
        mPermissionTipsDialog?.dismiss()
        mPermissionTipsDialog = null

        mCheckPermissionDialog?.dismiss()
        mCheckPermissionDialog = null
    }
}