

package com.kira.ui.feature.servers.internal

import com.kira.ui.core.provider.coroutine.DispatcherProvider
import com.kira.ui.core.storage.database.AppDatabase
import com.kira.ui.feature.servers.data.repository.ServersRepositoryImpl
import com.kira.ui.feature.servers.domain.repository.ServersRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServersModule {

    @Provides
    @Singleton
    fun provideServersRepository(
        dispatcherProvider: DispatcherProvider,
        appDatabase: AppDatabase,
    ): ServersRepository {
        return ServersRepositoryImpl(dispatcherProvider, appDatabase)
    }
}