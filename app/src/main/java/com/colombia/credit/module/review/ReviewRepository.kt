package com.colombia.credit.module.review

import com.colombia.credit.net.ApiService
import com.common.lib.base.BaseRepository
import javax.inject.Inject


class ReviewRepository @Inject constructor(private val apiService: ApiService):BaseRepository() {
}