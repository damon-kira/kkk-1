package com.colombia.credit.module.defer

import dagger.hilt.android.AndroidEntryPoint

// 展期
@AndroidEntryPoint
open class RepayTabDeferActivity : DeferActivity() {

    override fun getInfo() {
        intent.getStringExtra(EXTRA_INFO)?.let {

        }
    }
}