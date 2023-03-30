package com.colombia.credit.module.home

import com.colombia.credit.net.ApiService
import com.common.lib.base.BaseRepository
import javax.inject.Inject

class HomeLoanRepository @Inject constructor(private val apiService: ApiService): BaseRepository() {
}