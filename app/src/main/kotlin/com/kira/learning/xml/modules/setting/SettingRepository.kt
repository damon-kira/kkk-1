package com.kira.learning.xml.modules.setting

import com.kira.learning.di.BaseRepository
import com.kira.learning.bean.res.RspResult
import com.common.lib.net.ApiServiceLiveDataProxy
import javax.inject.Inject

class SettingRepository @Inject constructor() : BaseRepository() {

    fun logout() = ApiServiceLiveDataProxy.request(RspResult::class.java) {
        apiService.logout(createRequestBody("{}"))
    }
}