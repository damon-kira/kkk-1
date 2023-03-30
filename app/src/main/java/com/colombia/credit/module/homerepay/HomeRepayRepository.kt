package com.colombia.credit.module.homerepay

import com.colombia.credit.net.ApiService
import com.common.lib.base.BaseRepository
import javax.inject.Inject


class HomeRepayRepository @Inject constructor(private val apiService: ApiService) :
    BaseRepository() {
}