package com.colombia.credit.module.process.work

import com.common.lib.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WorkInfoViewModel @Inject constructor(private val repository: WorkInfoRepository) :
    BaseViewModel() {
}