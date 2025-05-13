package com.kira.learning.module.defer

import com.kira.learning.manager.Launch
import dagger.hilt.android.AndroidEntryPoint

// 展期
@AndroidEntryPoint
open class DeferHisActivity : DeferActivity() {

    override fun checkOrderRepay() {
        Launch.skipHistoryActivity(this)
        finish()
    }
}