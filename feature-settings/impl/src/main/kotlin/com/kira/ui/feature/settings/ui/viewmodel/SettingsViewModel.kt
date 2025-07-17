

package com.kira.ui.feature.settings.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kira.ui.core.mvi.ViewEvent
import com.kira.ui.core.provider.resources.StringProvider
import com.kira.ui.core.storage.keyvalue.SettingsManager
import com.kira.ui.feature.settings.R
import com.kira.ui.feature.settings.ui.adapter.PreferenceHeader
import com.kira.ui.feature.settings.ui.navigation.SettingsScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    stringProvider: StringProvider,
    private val settingsManager: SettingsManager,
) : ViewModel() {

    private val _headersState = MutableStateFlow(
        listOf(
            PreferenceHeader(
                title = stringProvider.getString(R.string.pref_header_application_title),
                subtitle = stringProvider.getString(R.string.pref_header_application_summary),
                selected = false,
                screen = SettingsScreen.Application,
            ),
            PreferenceHeader(
                title = stringProvider.getString(R.string.pref_header_editor_title),
                subtitle = stringProvider.getString(R.string.pref_header_editor_summary),
                selected = false,
                screen = SettingsScreen.Editor,
            ),
            PreferenceHeader(
                title = stringProvider.getString(R.string.pref_header_codeStyle_title),
                subtitle = stringProvider.getString(R.string.pref_header_codeStyle_summary),
                selected = false,
                screen = SettingsScreen.CodeStyle,
            ),
            PreferenceHeader(
                title = stringProvider.getString(R.string.pref_header_files_title),
                subtitle = stringProvider.getString(R.string.pref_header_files_summary),
                selected = false,
                screen = SettingsScreen.Files,
            ),
            PreferenceHeader(
                title = stringProvider.getString(R.string.pref_header_keybindings_title),
                subtitle = stringProvider.getString(R.string.pref_header_keybindings_summary),
                selected = false,
                screen = SettingsScreen.Keybindings,
            ),
            PreferenceHeader(
                title = stringProvider.getString(R.string.pref_header_cloud_title),
                subtitle = stringProvider.getString(R.string.pref_header_cloud_summary),
                selected = false,
                screen = SettingsScreen.Cloud,
            ),
            PreferenceHeader(
                title = stringProvider.getString(R.string.pref_header_about_title),
                subtitle = stringProvider.getString(R.string.pref_header_about_summary),
                selected = false,
                screen = SettingsScreen.About,
            ),
        ),
    )
    val headersState: StateFlow<List<PreferenceHeader>> = _headersState.asStateFlow()

    private val _viewEvent = Channel<ViewEvent>(Channel.BUFFERED)
    val viewEvent: Flow<ViewEvent> = _viewEvent.receiveAsFlow()

    var fullscreenMode: Boolean
        get() = settingsManager.fullScreenMode
        set(value) { settingsManager.fullScreenMode = value }

    fun selectHeader(header: PreferenceHeader) {
        viewModelScope.launch {
            _viewEvent.send(ViewEvent.Navigation(header.screen))
        }
    }
}