

package com.kira.ui.feature.themes.internal

import android.content.Context
import com.kira.ui.core.provider.coroutine.DispatcherProvider
import com.kira.ui.core.storage.database.AppDatabase
import com.kira.ui.core.storage.keyvalue.SettingsManager
import com.kira.ui.feature.themes.data.repository.ThemesRepositoryImpl
import com.kira.ui.feature.themes.domain.repository.ThemesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object ThemesModule {

    @Provides
    @ViewModelScoped
    fun provideThemesRepository(
        dispatcherProvider: DispatcherProvider,
        settingsManager: SettingsManager,
        appDatabase: AppDatabase,
        @ApplicationContext context: Context,
    ): ThemesRepository {
        return ThemesRepositoryImpl(
            dispatcherProvider = dispatcherProvider,
            settingsManager = settingsManager,
            appDatabase = appDatabase,
            context = context,
        )
    }
}