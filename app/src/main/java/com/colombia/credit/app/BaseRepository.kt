package com.colombia.credit.app

import com.colombia.credit.net.ApiService
import com.common.lib.base.CommonRepository
import com.util.lib.GsonUtil
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.File
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

    protected fun createRequestBody(jsonStr: String): RequestBody {
        return RequestBody.create(MEDIA_TYPE, jsonStr)
    }

    /**
     * 创建文件body
     */
    protected fun createFileRequestBody(file: File): RequestBody {
        return RequestBody.create(MediaType.parse("application/octet-stream"), file)
    }
}