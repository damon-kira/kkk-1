package com.colombia.credit.module.login

import com.colombia.credit.net.ApiService
import com.common.lib.base.BaseRepository
import javax.inject.Inject

class LoginRepository @Inject constructor(private val apiService: ApiService): BaseRepository() {

    fun login() {
    }
}