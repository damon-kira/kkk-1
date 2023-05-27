package com.colombia.credit.module.defer

import com.colombia.credit.manager.Launch
import dagger.hilt.android.AndroidEntryPoint

// 展期
@AndroidEntryPoint
open class DeferHisActivity : DeferActivity() {

    override fun checkOrderRepay() {
        Launch.skipHistoryActivity(this)
        finish()
    }
}