package com.colombia.credit.module.homerepay

import com.common.lib.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeRepayViewModel @Inject constructor(private val repository: HomeRepayRepository) :
    BaseViewModel() {
}