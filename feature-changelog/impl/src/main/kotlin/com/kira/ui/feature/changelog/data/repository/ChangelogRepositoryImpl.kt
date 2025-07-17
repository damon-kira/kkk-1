

package com.kira.ui.feature.changelog.data.repository

import android.content.Context
import com.kira.ui.core.provider.coroutine.DispatcherProvider
import com.kira.ui.feature.changelog.R
import com.kira.ui.feature.changelog.data.converter.ReleaseConverter
import com.kira.ui.feature.changelog.domain.model.ReleaseModel
import com.kira.ui.feature.changelog.domain.repository.ChangelogRepository
import kotlinx.coroutines.withContext
import java.io.BufferedReader

class ChangelogRepositoryImpl(
    private val dispatcherProvider: DispatcherProvider,
    private val context: Context,
) : ChangelogRepository {

    override suspend fun loadChangelog(): List<ReleaseModel> {
        return withContext(dispatcherProvider.io()) {
            val inputStream = context.resources.openRawResource(R.raw.changelog)
            val changelogRaw = inputStream.bufferedReader().use(BufferedReader::readText)
            ReleaseConverter.toReleaseModels(changelogRaw)
        }
    }
}