package com.colombia.credit.app

import com.colombia.credit.net.ApiService
import com.common.lib.base.CommonRepository
import javax.inject.Inject

/**
 * Created by weishl on 2023/3/27
 *
 */
open class BaseRepository : CommonRepository {

    @Inject
    lateinit var apiService: ApiService
}