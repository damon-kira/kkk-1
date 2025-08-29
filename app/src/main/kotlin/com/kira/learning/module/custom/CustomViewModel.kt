package com.kira.learning.module.custom

import com.kira.learning.expand.mCustom
import com.common.lib.base.BaseViewModel
import com.common.lib.livedata.observerNonStickyForever
import javax.inject.Inject


class CustomViewModel @Inject constructor(private val repository: CustomRepository): BaseViewModel() {

    fun getCustomInfo() {
        repository.getInfo().observerNonStickyForever {
            mCustom = it.getData()
        }
    }
}