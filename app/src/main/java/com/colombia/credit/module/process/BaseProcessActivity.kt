package com.colombia.credit.module.process

import android.os.Bundle
import com.colombia.credit.R
import com.common.lib.base.BaseActivity
import com.util.lib.StatusBarUtil.setStatusBar

abstract class BaseProcessActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBar(false, R.color.colorPrimary, false)
    }
}