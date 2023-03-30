package com.common.lib.dialog

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import com.common.lib.base.BaseDialogFragment
import java.lang.ref.WeakReference


/**
 * Created by weishl on 2022/6/7
 *
 */
interface IDialog {

    fun isShowing(): Boolean

    fun show()

    fun dismiss()

    fun setOnDialogDismissListener(listener: DefaultDialog.OnDialogDismissListener?)

}

class FragmentDialogWrapper : IDialog {
    private var mHostRef: WeakReference<Any>? = null
    private var mDialogFragment: BaseDialogFragment
    private var mDismissListener: DefaultDialog.OnDialogDismissListener? = null

    constructor(host: FragmentActivity, dialogFragment: BaseDialogFragment) {
        mHostRef = WeakReference(host)
        mDialogFragment = dialogFragment
    }

    constructor(host: Fragment, dialogFragment: BaseDialogFragment) {
        mHostRef = WeakReference(host)
        mDialogFragment = dialogFragment
    }

    override fun isShowing(): Boolean {
        return mDialogFragment.dialog?.isShowing ?: false
    }

    override fun show() {
        val host = mHostRef?.get()
        val manager: FragmentManager? = when {
            host == null -> {
                null
            }
            host is Fragment && host.lifecycle.currentState != Lifecycle.State.DESTROYED && host.isVisible -> {
                host.childFragmentManager
            }
            host is FragmentActivity  && !host.isFinishing  && !host.isDestroyed && host.lifecycle.currentState != Lifecycle.State.DESTROYED -> {
                host.supportFragmentManager
            }
            else -> null
        }
        showOrDismiss(manager)
    }


    override fun dismiss() {
        if (mDialogFragment.dialog != null) {
            mDialogFragment.dismissAllowingStateLoss()
        } else {
            mDismissListener?.onDialogDismiss()
        }
    }

    override fun setOnDialogDismissListener(listener: DefaultDialog.OnDialogDismissListener?) {
        mDismissListener = listener
        mDialogFragment.setDismissListener(listener)
    }

    private fun showOrDismiss(manager: FragmentManager?) {
        if (manager != null) {
            mDialogFragment.show(manager)
        } else {
            dismiss()
        }
    }
}