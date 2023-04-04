package com.colombia.credit.module.process.personalinfo

import com.common.lib.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PersonalViewModel @Inject constructor(private val repository: PersonalRepository) :
    BaseViewModel() {
}