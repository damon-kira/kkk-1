package com.common.lib.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.WindowManager
import com.common.lib.R
import com.common.lib.expand.getActivityFromContext
import com.util.lib.log.logger_e

/**
 * Created by weisl on 2019/10/14.
 */
open class DefaultDialog : Dialog, IDialog {

    protected val WRAP = 10f
    protected val MATCH = 20f

    private var mCtx: Context? = null

    constructor(context: Context) : this(context, R.style.App_Dialog)

    constructor(context: Context, style: Int) : super(context, style) {
        this.mCtx = context
    }

    private var mListener: OnDialogDismissListener? = null

    protected fun setDisplaySize(widthPercent: Float, heightPercent: Float, isBottom:Boolean = false) {
        val window = window
        val layoutParams = window!!.attributes
        val wm = window.windowManager
        val d = wm.defaultDisplay // 获取屏幕宽、高用
        if (widthPercent == WRAP) {
            layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT
        } else if (widthPercent == MATCH) {
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        } else {
            layoutParams.width = (d.width * widthPercent).toInt()
        }
        if (heightPercent == WRAP) {
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        } else if (heightPercent == MATCH) {
            layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        } else {
            layoutParams.height = (d.height * heightPercent).toInt()
        }

        if (isBottom){
            window.setGravity(Gravity.BOTTOM)
        }

        window.attributes = layoutParams
    }

    /**
     * 显示对话框的时候，如果正好 Activity 关闭了，那么会抛出 Window Leaked 异常导致崩溃
     */
    override fun show() {
        try {
            if (isDestroyed()) return
            super.show()
        } catch (e: Exception) {
            logger_e("debug_DefaultDialog", e.toString())
        }
    }

    private fun isDestroyed(): Boolean {
        val ctx = getActivityFromContext(mCtx)
        if ( ctx is Activity) {
            if (ctx.isFinishing || ctx.isDestroyed) {
                return true
            }
        } else return true
        return false
    }

    override fun dismiss() {
        mListener?.onDialogDismiss()
        if (isShowing) {
            try {
                if (isDestroyed()) return
                super.dismiss()
            } catch (e: Exception) {
            }
        }
    }

    override fun setOnDialogDismissListener(listener: OnDialogDismissListener?) {
        mListener = listener
    }

    interface OnDialogDismissListener {
        fun onDialogDismiss()
    }
}