package com.colombia.credit.module.repay

import com.common.lib.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RepayTabViewModel @Inject constructor(private val repository: RepayTabRepository):BaseViewModel() {
}