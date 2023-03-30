package com.colombia.credit.module.repeat

import com.common.lib.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RepeatViewModel @Inject constructor(private val repository: RepeatRepository) :
    BaseViewModel() {
}