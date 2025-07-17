

package com.kira.ui.feature.changelog.internal

import android.content.Context
import com.kira.ui.core.provider.coroutine.DispatcherProvider
import com.kira.ui.feature.changelog.data.repository.ChangelogRepositoryImpl
import com.kira.ui.feature.changelog.domain.repository.ChangelogRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ChangelogModule {

    @Provides
    @Singleton
    fun provideChangelogRepository(
        @ApplicationContext context: Context,
        dispatcherProvider: DispatcherProvider,
    ): ChangelogRepository {
        return ChangelogRepositoryImpl(dispatcherProvider, context)
    }
}