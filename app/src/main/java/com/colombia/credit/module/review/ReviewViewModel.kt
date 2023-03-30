package com.colombia.credit.module.review

import com.common.lib.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(private val repository: ReviewRepository) :
    BaseViewModel() {
}