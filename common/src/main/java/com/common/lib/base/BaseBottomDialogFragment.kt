package com.common.lib.base

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import com.common.lib.R
import com.common.lib.dialog.DefaultDialog

abstract class BaseBottomDialogFragment : BaseDialogFragment() {


    override fun getTheme(): Int {
        return R.style.StyleDialogBottomAnim
    }

    final override fun setWindowStyle() {
        dialog?.window?.apply {
            val size = getSize(windowManager)
            val params = attributes
            params.width = size.x
            params.height = size.y
            params.gravity = Gravity.BOTTOM
            attributes = params
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomDialog(getSupportContext()).also {
            it.window?.setGravity(Gravity.BOTTOM)
        }
    }
}

class BottomDialog(context: Context) : DefaultDialog(context, R.style.StyleDialogBottomAnim)