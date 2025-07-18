package com.kira.learning.app

import android.content.Context
import com.kira.ui.core.provider.coroutine.DispatcherProvider
import com.kira.ui.core.provider.resources.StringProvider
import com.kira.learning.module.supereditor.provider.coroutine.DispatcherProviderImpl
import com.kira.learning.module.supereditor.provider.resources.StringProviderImpl
import com.kira.learning.di.ApiActivitiesService
import com.kira.learning.di.UploadApiService
import com.kira.learning.net.ApiManager
import com.kira.learning.net.ApiService
import com.kira.learning.net.DataApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

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

    @ApiActivitiesService
    @Singleton
    @Provides
    fun provideApiActivitiesService(): ApiService {
        return ApiManager.getInstance().createApiActivitiesService()
    }


    @Provides
    @Singleton
    fun provideDispatcherProvider(): DispatcherProvider {
        return DispatcherProviderImpl()
    }

    @Provides
    @Singleton
    fun provideStringProvider(@ApplicationContext context: Context): StringProvider {
        return StringProviderImpl(context)
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