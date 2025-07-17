

package com.kira.ui.feature.editor.internal

import android.content.Context
import com.kira.ui.core.provider.coroutine.DispatcherProvider
import com.kira.ui.core.storage.database.AppDatabase
import com.kira.ui.core.storage.keyvalue.SettingsManager
import com.kira.ui.feature.editor.data.repository.DocumentRepositoryImpl
import com.kira.ui.feature.editor.domain.repository.DocumentRepository
import com.kira.ui.feature.explorer.domain.factory.FilesystemFactory
import com.kira.ui.filesystem.base.Filesystem
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Named

@Module
@InstallIn(ViewModelComponent::class)
object EditorModule {

    @Provides
    @ViewModelScoped
    fun provideDocumentRepository(
        @ApplicationContext context: Context,
        dispatcherProvider: DispatcherProvider,
        settingsManager: SettingsManager,
        appDatabase: AppDatabase,
        filesystemFactory: FilesystemFactory,
        @Named("Cache") cacheFilesystem: Filesystem,
    ): DocumentRepository {
        return DocumentRepositoryImpl(
            dispatcherProvider = dispatcherProvider,
            settingsManager = settingsManager,
            appDatabase = appDatabase,
            filesystemFactory = filesystemFactory,
            cacheFilesystem = cacheFilesystem,
            context = context,
        )
    }
}