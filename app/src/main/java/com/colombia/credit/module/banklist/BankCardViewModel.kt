package com.colombia.credit.module.banklist

import com.common.lib.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BankCardViewModel @Inject constructor(private val repository: BankCardRepository) :
    BaseViewModel() {
}