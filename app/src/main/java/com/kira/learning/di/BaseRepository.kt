package com.kira.learning.di

import com.common.lib.base.CommonRepository
import com.kira.learning.net.ApiService
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

open class BaseRepository : CommonRepository {

    protected val MEDIA_TYPE = "application/json;charset=utf-8".toMediaType()
    protected val MEDIA_IMAGE = "multipart/form-data;charset=utf-8".toMediaType()

    @Inject
    lateinit var apiService: ApiService

    protected fun createRequestBody(jsonStr: String): RequestBody {
        return RequestBody.Companion.create(MEDIA_TYPE, jsonStr)
    }

    /**
     * 创建文件body
     */
    protected fun createFileRequestBody(file: File): RequestBody {
        return file.asRequestBody("application/octet-stream".toMediaType())
    }
}