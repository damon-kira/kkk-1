package com.colombia.credit.module.defer

import com.common.lib.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DeferViewModel @Inject constructor(private val repository: DeferRepository) : BaseViewModel() {

}