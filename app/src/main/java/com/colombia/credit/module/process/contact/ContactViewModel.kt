package com.colombia.credit.module.process.contact

import com.common.lib.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ContactViewModel @Inject constructor(private val repository: ContactRepository) :
    BaseViewModel() {
}