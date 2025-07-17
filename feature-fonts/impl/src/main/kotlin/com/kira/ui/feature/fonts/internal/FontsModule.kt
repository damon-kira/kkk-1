

package com.kira.ui.feature.fonts.internal

import android.content.Context
import com.kira.ui.core.provider.coroutine.DispatcherProvider
import com.kira.ui.core.storage.database.AppDatabase
import com.kira.ui.core.storage.keyvalue.SettingsManager
import com.kira.ui.feature.fonts.data.repository.FontsRepositoryImpl
import com.kira.ui.feature.fonts.domain.repository.FontsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object FontsModule {

    @Provides
    @ViewModelScoped
    fun provideFontsRepository(
        @ApplicationContext context: Context,
        dispatcherProvider: DispatcherProvider,
        settingsManager: SettingsManager,
        appDatabase: AppDatabase,
    ): FontsRepository {
        return FontsRepositoryImpl(
            dispatcherProvider = dispatcherProvider,
            settingsManager = settingsManager,
            appDatabase = appDatabase,
            context = context,
        )
    }
}