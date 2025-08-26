

package com.kira.ui.feature.settings.data.repository

import android.content.Context
import com.kira.ui.core.storage.keyvalue.SettingsManager
import com.kira.ui.feature.settings.domain.model.KeyModel
import com.kira.ui.feature.settings.domain.repository.SettingsRepository
import com.common.kira.ui.R as UiR

class SettingsRepositoryImpl(
    private val settingsManager: SettingsManager,
    private val context: Context,
) : SettingsRepository {

    override fun keyboardPreset(): List<KeyModel> {
        val preset = "\t" + settingsManager.keyboardPreset
        return preset.map { char ->
            val display = if (char == '\t') {
                context.getString(UiR.string.common_tab)
            } else {
                char.toString()
            }
            KeyModel(display, char)
        }
    }
}