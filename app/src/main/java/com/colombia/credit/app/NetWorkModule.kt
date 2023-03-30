package com.colombia.credit.app

import com.colombia.credit.net.ApiManager
import com.colombia.credit.net.ApiService
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
//
//    @UploadApiService
//    @Singleton
//    @Provides
//    fun provideApiUploadService(): ApiService {
//        return ApiManager.getInstance().createApiUploadService()
//    }
//
//    @CheckApiService
//    @Singleton
//    @Provides
//    fun provideCheckApiService(): ApiService {
//        return ApiManager.getInstance().createCheckApiService()
//    }
//
//    @DownloadApiService
//    @Singleton
//    @Provides
//    fun provideDownloadpiService(): ApiService {
//        return ApiManager.getInstance().createDownloadApiService()
//    }
}