

package com.kira.ui.feature.settings.domain.repository

import com.kira.ui.feature.settings.domain.model.KeyModel

interface SettingsRepository {
    fun keyboardPreset(): List<KeyModel>
}