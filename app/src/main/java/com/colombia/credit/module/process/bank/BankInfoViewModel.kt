package com.colombia.credit.module.process.bank

import com.common.lib.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BankInfoViewModel @Inject constructor(private val repository: BankInfoRepository) :
    BaseViewModel() {
}