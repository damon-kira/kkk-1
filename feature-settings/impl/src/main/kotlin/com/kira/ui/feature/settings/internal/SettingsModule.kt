

package com.kira.ui.feature.settings.internal

import android.content.Context
import com.kira.ui.core.storage.keyvalue.SettingsManager
import com.kira.ui.feature.settings.data.repository.SettingsRepositoryImpl
import com.kira.ui.feature.settings.domain.repository.SettingsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SettingsModule {

    @Provides
    @Singleton
    fun provideSettingsRepository(
        @ApplicationContext context: Context,
        settingsManager: SettingsManager,
    ): SettingsRepository {
        return SettingsRepositoryImpl(settingsManager, context)
    }
}