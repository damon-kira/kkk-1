package com.colombia.credit.module.config

import com.colombia.credit.app.BaseRepository
import com.common.lib.net.ApiServiceLiveDataProxy
import com.google.gson.JsonObject
import javax.inject.Inject

class ConfigRepository @Inject constructor(): BaseRepository() {

    fun getConfig(keys: String) =
        ApiServiceLiveDataProxy.request {
            val jobj = JsonObject()
            jobj.addProperty("ovaGBHVDSubds8sd", keys)
            apiService.getConfig(createRequestBody(jobj.toString()))
        }
}