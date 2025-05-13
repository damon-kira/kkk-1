package com.kira.learning.module.config

import com.kira.learning.app.BaseRepository
import com.common.lib.net.ApiServiceLiveDataProxy
import com.google.gson.JsonObject
import javax.inject.Inject

class ConfigRepository @Inject constructor(): BaseRepository() {

    fun getConfig(keys: String) =
        ApiServiceLiveDataProxy.requestIgnoreLogin {
            val jobj = JsonObject()
            jobj.addProperty("ovaGBHVDSubds8sd", keys)
            apiService.getConfig(createRequestBody(jobj.toString()))
        }
}