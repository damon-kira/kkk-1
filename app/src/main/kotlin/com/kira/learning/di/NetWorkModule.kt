package com.kira.learning.di

import android.content.Context
import com.kira.learning.xml.modules.supereditor.provider.DispatcherProviderImpl
import com.kira.learning.xml.modules.supereditor.provider.StringProviderImpl
import com.kira.ui.core.provider.coroutine.DispatcherProvider
import com.kira.ui.core.provider.resources.StringProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetWorkModule {

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

}