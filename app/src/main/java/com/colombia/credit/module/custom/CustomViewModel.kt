package com.colombia.credit.module.custom

import com.colombia.credit.expand.mCustom
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