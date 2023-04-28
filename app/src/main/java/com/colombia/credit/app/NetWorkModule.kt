package com.colombia.credit.app

import com.colombia.credit.di.UploadApiService
import com.colombia.credit.net.ApiManager
import com.colombia.credit.net.ApiService
import com.colombia.credit.net.DataApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by weisl on 2019/10/15.
 */

@Module
@InstallIn(SingletonComponent::class)
class NetWorkModule {

    @Singleton
    @Provides
    fun provideApiService(): ApiService {
        return ApiManager.getInstance().createApiService()
    }

    @Singleton
    @Provides
    fun provideDataApiService(): DataApiService {
        return ApiManager.getInstance().getDataApiService()
    }

    @UploadApiService
    @Singleton
    @Provides
    fun provideApiUploadService(): ApiService {
        return ApiManager.getInstance().createUploadService()
    }

//    @CheckApiService
//    @Singleton
//    @Provides
//    fun provideCheckApiService(): ApiService {
//        return ApiManager.getInstance().createCheckApiService()
//    }

//    @DownloadApiService
//    @Singleton
//    @Provides
//    fun provideDownloadpiService(): ApiService {
//        return ApiManager.getInstance().createDownloadApiService()
//    }
}