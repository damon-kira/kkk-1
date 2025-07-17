

package com.kira.ui.feature.shortcuts.internal

import com.kira.ui.core.provider.coroutine.DispatcherProvider
import com.kira.ui.core.storage.keyvalue.SettingsManager
import com.kira.ui.feature.shortcuts.domain.repository.ShortcutsRepository
import com.kira.ui.feature.shortcuts.repository.ShortcutsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ShortcutsModule {

    @Provides
    @Singleton
    fun provideShortcutsRepository(
        dispatcherProvider: DispatcherProvider,
        settingsManager: SettingsManager,
    ): ShortcutsRepository {
        return ShortcutsRepositoryImpl(dispatcherProvider, settingsManager)
    }
}