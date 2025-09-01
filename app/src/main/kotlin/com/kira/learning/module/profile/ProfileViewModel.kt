package com.kira.learning.module.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kira.learning.network.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repo: ProfileRepository
): ViewModel() {

    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init { load() }

    fun load() {
        viewModelScope.launch {
            _uiState.value = ProfileUiState.Loading
            when(val res = repo.load()) {
                is ApiResult.Success -> _uiState.value = ProfileUiState.Data(profile = res.data)
                is ApiResult.Error -> _uiState.value = ProfileUiState.Error(res.message)
                ApiResult.NetworkUnavailable -> _uiState.value = ProfileUiState.Error("网络不可用")
            }
        }
    }

    fun startEdit() = mutateData { it.copy(editing = true, message = null) }
    fun cancelEdit() = mutateData { it.copy(editing = false, editName = it.profile.name, editEmail = it.profile.email, editBio = it.profile.bio, message = null) }

    fun setName(v: String) = mutateData { it.copy(editName = v) }
    fun setEmail(v: String) = mutateData { it.copy(editEmail = v) }
    fun setBio(v: String) = mutateData { it.copy(editBio = v) }

    fun saveEdit() = viewModelScope.launch {
        val data = _uiState.value as? ProfileUiState.Data ?: return@launch
        _uiState.update { (it as ProfileUiState.Data).copy(saving = true, message = null) }
        when(val r = repo.update(data.editName, data.editEmail, data.editBio)) {
            is ApiResult.Success -> _uiState.update { (it as ProfileUiState.Data).copy(profile = r.data, editing = false, saving = false, message = "已保存") }
            is ApiResult.Error -> _uiState.update { (it as ProfileUiState.Data).copy(saving = false, message = r.message) }
            ApiResult.NetworkUnavailable -> _uiState.update { (it as ProfileUiState.Data).copy(saving = false, message = "网络不可用") }
        }
    }

    fun randomUpdateAvatar() = viewModelScope.launch {
        val data = _uiState.value as? ProfileUiState.Data ?: return@launch
        val size = Random.nextInt(180, 260)
        when(val r = repo.updateAvatar("https://placekitten.com/${size}/${size}")) {
            is ApiResult.Success -> _uiState.update { data.copy(profile = r.data, message = "头像已更新") }
            is ApiResult.Error -> _uiState.update { data.copy(message = r.message) }
            ApiResult.NetworkUnavailable -> _uiState.update { data.copy(message = "网络不可用") }
        }
    }

    fun toggleDark() = toggleSetting { it.copy(darkMode = !it.darkMode) }
    fun toggleNotify() = toggleSetting { it.copy(notificationsEnabled = !it.notificationsEnabled) }
    fun toggleAutoPlay() = toggleSetting { it.copy(autoPlayVideo = !it.autoPlayVideo) }
    fun toggleAnalytics() = toggleSetting { it.copy(analyticsEnabled = !it.analyticsEnabled) }
    fun toggleCrash() = toggleSetting { it.copy(crashReportEnabled = !it.crashReportEnabled) }

    private fun toggleSetting(block: (AppSettings) -> AppSettings) {
        val data = _uiState.value as? ProfileUiState.Data ?: return
        val settings = block(data.settings)
        _uiState.update { data.copy(settings = settings, message = null) }
        viewModelScope.launch { repo.updateSettings(settings) }
    }

    private inline fun mutateData(transform: (ProfileUiState.Data) -> ProfileUiState.Data) {
        val cur = _uiState.value as? ProfileUiState.Data ?: return
        _uiState.value = transform(cur)
    }
}
