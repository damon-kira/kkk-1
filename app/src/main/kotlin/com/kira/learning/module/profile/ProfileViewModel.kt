package com.kira.learning.module.profile

import androidx.lifecycle.viewModelScope
import com.kira.learning.model.UserProfile
import com.kira.learning.model.AppSettings
import com.kira.learning.network.ApiResult
import com.kira.learning.base.mvi.BaseUiState
import com.kira.learning.base.mvi.BaseViewModel
import com.kira.learning.base.mvi.UiEvent
import com.kira.learning.base.mvi.ViewState
import com.kira.learning.base.validation.FormState
import com.kira.learning.base.validation.FormField
import com.kira.learning.base.validation.Validators
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

// 重构后的ProfileViewState，使用通用BaseUiState
data class ProfileViewState(
    val profileData: BaseUiState<UserProfile> = BaseUiState.Idle,
    val settings: AppSettings = AppSettings(),
    val formState: FormState = FormState(),
    val isEditing: Boolean = false,
    val isSaving: Boolean = false,
    val message: String? = null
) : ViewState()

// 重构后的事件系统
sealed interface ProfileEvent {
    object LoadProfile : ProfileEvent
    object StartEdit : ProfileEvent
    object CancelEdit : ProfileEvent
    object SaveProfile : ProfileEvent
    object RandomAvatar : ProfileEvent
    data class UpdateName(val name: String) : ProfileEvent
    data class UpdateEmail(val email: String) : ProfileEvent
    data class UpdateBio(val bio: String) : ProfileEvent
    data class ToggleSetting(val setting: SettingType) : ProfileEvent
}

enum class SettingType {
    DARK_MODE, NOTIFICATIONS, AUTO_PLAY, ANALYTICS, CRASH_REPORTS
}

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repo: ProfileRepository
) : BaseViewModel<ProfileViewState, UiEvent>(
    initialState = ProfileViewState()
) {

    init {
        setupFormValidation()
        handleAction(ProfileEvent.LoadProfile)
    }

    override fun handleAction(action: Any) {
        when (action) {
            is ProfileEvent.LoadProfile -> loadProfile()
            is ProfileEvent.StartEdit -> startEditInternal()
            is ProfileEvent.CancelEdit -> cancelEditInternal()
            is ProfileEvent.SaveProfile -> saveProfile()
            is ProfileEvent.RandomAvatar -> updateAvatar()
            is ProfileEvent.UpdateName -> updateFormField("name", action.name)
            is ProfileEvent.UpdateEmail -> updateFormField("email", action.email)
            is ProfileEvent.UpdateBio -> updateFormField("bio", action.bio)
            is ProfileEvent.ToggleSetting -> toggleSetting(action.setting)
        }
    }

    private fun setupFormValidation() {
        val initialFormState = FormState(
            fields = mapOf(
                "name" to FormField(
                    validators = listOf(
                        Validators.required("姓名不能为空"),
                        Validators.maxLength(50, "姓名不能超过50个字符")
                    )
                ),
                "email" to FormField(
                    validators = listOf(
                        Validators.required("邮箱不能为空"),
                        Validators.email("请输入有效的邮箱地址")
                    )
                ),
                "bio" to FormField(
                    validators = listOf(
                        Validators.maxLength(200, "个人简介不能超过200个字符")
                    )
                )
            )
        )
        updateState { copy(formState = initialFormState) }
    }

    private fun loadProfile() {
        viewModelScope.launch {
            updateState { copy(profileData = BaseUiState.Loading) }

            when (val result = repo.load()) {
                is ApiResult.Success -> {
                    val settings = repo.getSettings()
                    updateState {
                        copy(
                            profileData = BaseUiState.Success(result.data),
                            settings = settings,
                            formState = formState.copy(
                                fields = formState.fields.mapValues { (key, field) ->
                                    when (key) {
                                        "name" -> field.copy(value = result.data.name)
                                        "email" -> field.copy(value = result.data.email)
                                        "bio" -> field.copy(value = result.data.bio)
                                        else -> field
                                    }
                                }
                            )
                        )
                    }
                }

                is ApiResult.Error -> {
                    updateState { copy(profileData = BaseUiState.Error(result.message)) }
                    sendEvent(UiEvent.ShowSnackbar(result.message))
                }

                ApiResult.NetworkUnavailable -> {
                    val message = "网络不可用"
                    updateState { copy(profileData = BaseUiState.Error(message)) }
                    sendEvent(UiEvent.ShowSnackbar(message))
                }
            }
        }
    }

    private fun startEditInternal() {
        updateState { copy(isEditing = true, message = null) }
    }

    private fun cancelEditInternal() {
        val profile = (currentState.profileData as? BaseUiState.Success)?.data
        if (profile != null) {
            updateState {
                copy(
                    isEditing = false,
                    formState = formState.copy(
                        fields = formState.fields.mapValues { (key, field) ->
                            when (key) {
                                "name" -> field.copy(value = profile.name)
                                "email" -> field.copy(value = profile.email)
                                "bio" -> field.copy(value = profile.bio)
                                else -> field
                            }
                        }
                    ),
                    message = null
                )
            }
        }
    }

    private fun updateFormField(fieldName: String, value: String) {
        updateState {
            copy(formState = formState.updateField(fieldName, value))
        }
    }

    private fun saveProfile() {
        val validatedForm = currentState.formState.validateAll()
        updateState { copy(formState = validatedForm) }

        if (!validatedForm.isValid) {
            sendEvent(UiEvent.ShowSnackbar("请检查输入信息"))
            return
        }

        viewModelScope.launch {
            updateState { copy(isSaving = true, message = null) }

            val name = validatedForm.fields["name"]?.value ?: ""
            val email = validatedForm.fields["email"]?.value ?: ""
            val bio = validatedForm.fields["bio"]?.value ?: ""

            when (val result = repo.update(name, email, bio)) {
                is ApiResult.Success -> {
                    updateState {
                        copy(
                            profileData = BaseUiState.Success(result.data),
                            isEditing = false,
                            isSaving = false,
                            message = "保存成功"
                        )
                    }
                    sendEvent(UiEvent.ShowSnackbar("保存成功"))
                }

                is ApiResult.Error -> {
                    updateState { copy(isSaving = false, message = result.message) }
                    sendEvent(UiEvent.ShowSnackbar(result.message))
                }

                ApiResult.NetworkUnavailable -> {
                    val message = "网络不可用"
                    updateState { copy(isSaving = false, message = message) }
                    sendEvent(UiEvent.ShowSnackbar(message))
                }
            }
        }
    }

    private fun updateAvatar() {
        viewModelScope.launch {
            val size = Random.nextInt(180, 260)

            when (val result = repo.updateAvatar("https://placekitten.com/${size}/${size}")) {
                is ApiResult.Success -> {
                    updateState {
                        copy(
                            profileData = BaseUiState.Success(result.data),
                            message = "头像已更新"
                        )
                    }
                    sendEvent(UiEvent.ShowSnackbar("头像已更新"))
                }

                is ApiResult.Error -> {
                    updateState { copy(message = result.message) }
                    sendEvent(UiEvent.ShowSnackbar(result.message))
                }

                ApiResult.NetworkUnavailable -> {
                    val message = "网络不可用"
                    updateState { copy(message = message) }
                    sendEvent(UiEvent.ShowSnackbar(message))
                }
            }
        }
    }

    private fun toggleSetting(settingType: SettingType) {
        val newSettings = when (settingType) {
            SettingType.DARK_MODE -> currentState.settings.copy(darkMode = !currentState.settings.darkMode)
            SettingType.NOTIFICATIONS -> currentState.settings.copy(notificationsEnabled = !currentState.settings.notificationsEnabled)
            SettingType.AUTO_PLAY -> currentState.settings.copy(autoPlayVideo = !currentState.settings.autoPlayVideo)
            SettingType.ANALYTICS -> currentState.settings.copy(analyticsEnabled = !currentState.settings.analyticsEnabled)
            SettingType.CRASH_REPORTS -> currentState.settings.copy(crashReportEnabled = !currentState.settings.crashReportEnabled)
        }

        updateState { copy(settings = newSettings, message = null) }

        viewModelScope.launch {
            repo.updateSettings(newSettings)
        }
    }

}
