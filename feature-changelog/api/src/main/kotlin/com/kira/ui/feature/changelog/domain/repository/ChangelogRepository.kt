

package com.kira.ui.feature.changelog.domain.repository

import com.kira.ui.feature.changelog.domain.model.ReleaseModel

interface ChangelogRepository {

    suspend fun loadChangelog(): List<ReleaseModel>
}