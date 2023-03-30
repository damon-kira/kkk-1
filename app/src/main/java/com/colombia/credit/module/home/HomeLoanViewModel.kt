package com.colombia.credit.module.home

import com.common.lib.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeLoanViewModel @Inject constructor(private val repository: HomeLoanRepository) :
    BaseViewModel() {
}