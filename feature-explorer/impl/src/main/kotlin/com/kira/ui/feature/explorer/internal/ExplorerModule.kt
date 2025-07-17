package com.kira.ui.feature.explorer.internal

import android.content.Context
import com.kira.ui.core.provider.coroutine.DispatcherProvider
import com.kira.ui.core.storage.Directories
import com.kira.ui.core.storage.keyvalue.SettingsManager
import com.kira.ui.feature.explorer.data.factory.FilesystemFactoryImpl
import com.kira.ui.feature.explorer.data.repository.ExplorerRepositoryImpl
import com.kira.ui.feature.explorer.domain.factory.FilesystemFactory
import com.kira.ui.feature.explorer.domain.repository.ExplorerRepository
import com.kira.ui.feature.servers.domain.repository.ServersRepository
import com.kira.ui.filesystem.base.Filesystem
import com.kira.ui.filesystem.local.utils.LocalFilesystem
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ExplorerModule {

    @Provides
    @Singleton
    fun provideExplorerRepository(
        @ApplicationContext context: Context,
        dispatcherProvider: DispatcherProvider,
        settingsManager: SettingsManager,
        filesystemFactory: FilesystemFactory,
    ): ExplorerRepository {
        return ExplorerRepositoryImpl(
            dispatcherProvider = dispatcherProvider,
            settingsManager = settingsManager,
            filesystemFactory = filesystemFactory,
            context = context,
        )
    }

    @Provides
    @Singleton
    fun provideFilesystemFactory(
        @ApplicationContext context: Context,
        serversRepository: ServersRepository,
    ): FilesystemFactory {
        return FilesystemFactoryImpl(serversRepository, Directories.ftpDir(context))
    }

    @Provides
    @Singleton
    @Named("Cache")
    fun provideCacheFilesystem(@ApplicationContext context: Context): Filesystem {
        return LocalFilesystem(Directories.filesDir(context))
    }
}