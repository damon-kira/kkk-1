package com.colombia.credit.app

import com.colombia.credit.net.ApiService
import com.common.lib.base.CommonRepository
import okhttp3.MediaType
import javax.inject.Inject

/**
 * Created by weishl on 2023/3/27
 *
 */
open class BaseRepository : CommonRepository {

    protected val MEDIA_TYPE = MediaType.parse("application/json;charset=utf-8")
    protected val MEDIA_IMAGE = MediaType.parse("multipart/form-data;charset=utf-8")

    @Inject
    lateinit var apiService: ApiService
}