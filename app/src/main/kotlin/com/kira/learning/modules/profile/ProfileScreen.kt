package com.kira.learning.modules.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kira.learning.models.UserProfile
import com.kira.learning.utils.AppAvatar
import com.kira.learning.base.components.*
import com.kira.learning.base.mvi.UiEvent

@Composable
fun ProfileRoute(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel(),
    onBack: (() -> Unit)? = null,
    onNavigateToLogin: (() -> Unit)? = null
) {
    val state by viewModel.viewState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    // 收集事件
    LaunchedEffect(viewModel) {
        viewModel.viewEvent.collect { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(event.message)
                }
                is UiEvent.Navigate -> {
                    if (event.route == "login") {
                        onNavigateToLogin?.invoke()
                    }
                }
                else -> {}
            }
        }
    }

    Scaffold(
        topBar = {
            ProfileTopBar(
                isEditing = state.isEditing,
                onBack = onBack,
                onStartEdit = { viewModel.handleAction(ProfileEvent.StartEdit) },
                onRandomAvatar = { viewModel.handleAction(ProfileEvent.RandomAvatar) }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        StateHandler(
            state = state.profileData,
            onRetry = { viewModel.handleAction(ProfileEvent.LoadProfile) }
        ) { profile, stateModifier ->
            ProfileContent(
                profile = profile,
                state = state,
                onAction = viewModel::handleAction,
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .then(stateModifier)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileTopBar(
    isEditing: Boolean,
    onBack: (() -> Unit)?,
    onStartEdit: () -> Unit,
    onRandomAvatar: () -> Unit
) {
    TopAppBar(
        title = { Text("个人信息") },
        navigationIcon = {
            if (onBack != null) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                }
            }
        },
        actions = {
            if (!isEditing) {
                IconButton(onClick = onStartEdit) {
                    Icon(Icons.Default.Edit, null)
                }
                IconButton(onClick = onRandomAvatar) {
                    Icon(Icons.Default.Refresh, null)
                }
            }
        }
    )
}

@Composable
private fun ProfileContent(
    profile: UserProfile,
    state: ProfileViewState,
    onAction: (ProfileEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // 头像和基本信息
        ProfileHeader(
            profile = profile,
            isEditing = state.isEditing
        )

        // 编辑表单
        if (state.isEditing) {
            ProfileEditForm(
                formState = state.formState,
                isSaving = state.isSaving,
                onUpdateName = { onAction(ProfileEvent.UpdateName(it)) },
                onUpdateEmail = { onAction(ProfileEvent.UpdateEmail(it)) },
                onUpdateBio = { onAction(ProfileEvent.UpdateBio(it)) },
                onSave = { onAction(ProfileEvent.SaveProfile) },
                onCancel = { onAction(ProfileEvent.CancelEdit) }
            )
        }

        // 设置选项
        ProfileSettings(
            settings = state.settings,
            onToggleSetting = { onAction(ProfileEvent.ToggleSetting(it)) },
            onLogout = { onAction(ProfileEvent.Logout) } // 添加登出事件处理
        )

        // 消息提示
//        state.message?.let { message ->
//            Card(
//                colors = CardDefaults.cardColors(
//                    containerColor = MaterialTheme.colorScheme.primaryContainer
//                )
//            ) {
//                Text(
//                    text = message,
//                    modifier = Modifier.padding(16.dp),
//                    style = MaterialTheme.typography.bodyMedium
//                )
//            }
//        }
    }
}

@Composable
private fun ProfileHeader(
    profile: UserProfile,
    isEditing: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AppAvatar(
            url = profile.avatarUrl,
            size = 80.dp
        )

        Column {
            Text(
                text = profile.name,
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = profile.email,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (!isEditing && profile.bio.isNotEmpty()) {
                Text(
                    text = profile.bio,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun ProfileEditForm(
    formState: com.kira.learning.base.validation.FormState,
    isSaving: Boolean,
    onUpdateName: (String) -> Unit,
    onUpdateEmail: (String) -> Unit,
    onUpdateBio: (String) -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit
) {
    Card {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "编辑信息",
                style = MaterialTheme.typography.titleMedium
            )

            val nameField = formState.fields["name"]
            InputField(
                value = nameField?.value ?: "",
                onValueChange = onUpdateName,
                label = "姓名",
                errorMessage = nameField?.error,
                enabled = !isSaving,
                imeAction = ImeAction.Next
            )

            val emailField = formState.fields["email"]
            InputField(
                value = emailField?.value ?: "",
                onValueChange = onUpdateEmail,
                label = "邮箱",
                errorMessage = emailField?.error,
                enabled = !isSaving,
                imeAction = ImeAction.Next
            )

            val bioField = formState.fields["bio"]
            MultilineInputField(
                value = bioField?.value ?: "",
                onValueChange = onUpdateBio,
                label = "个人简介",
                errorMessage = bioField?.error,
                enabled = !isSaving
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End)
            ) {
                TextButton(
                    onClick = onCancel,
                    enabled = !isSaving
                ) {
                    Text("取消")
                }

                Button(
                    onClick = onSave,
                    enabled = !isSaving && formState.isValid
                ) {
                    if (isSaving) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("保存")
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileSettings(
    settings: com.kira.learning.models.AppSettings,
    onToggleSetting: (SettingType) -> Unit,
    onLogout: () -> Unit // 添加登出事件的回调
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(0.dp)
        ) {
            // 标题区域，使用紫色渐变背景
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = androidx.compose.ui.graphics.Brush.horizontalGradient(
                            colors = listOf(
                                androidx.compose.ui.graphics.Color(0xFF8B5CF6), // 紫色
                                androidx.compose.ui.graphics.Color(0xFFA855F7)  // 偏粉的紫色
                            )
                        )
                    )
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = null,
                        tint = androidx.compose.ui.graphics.Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "个人设置",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold
                        ),
                        color = androidx.compose.ui.graphics.Color.White
                    )
                }
            }

            // 设置项列表
            Column(
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                SettingItemWithIcon(
                    title = "深色模式",
                    description = "切换应用主题色调",
                    icon = Icons.Default.DarkMode,
                    checked = settings.darkMode,
                    onCheckedChange = { onToggleSetting(SettingType.DARK_MODE) }
                )

                SettingItemWithIcon(
                    title = "推送通知",
                    description = "接收重要消息提醒",
                    icon = Icons.Default.Notifications,
                    checked = settings.notificationsEnabled,
                    onCheckedChange = { onToggleSetting(SettingType.NOTIFICATIONS) }
                )

                SettingItemWithIcon(
                    title = "自动播放视频",
                    description = "视频内容自动播放",
                    icon = Icons.Default.PlayArrow,
                    checked = settings.autoPlayVideo,
                    onCheckedChange = { onToggleSetting(SettingType.AUTO_PLAY) }
                )

                SettingItemWithIcon(
                    title = "数据分析",
                    description = "帮助改善应用体验",
                    icon = Icons.Default.Analytics,
                    checked = settings.analyticsEnabled,
                    onCheckedChange = { onToggleSetting(SettingType.ANALYTICS) }
                )

                SettingItemWithIcon(
                    title = "崩溃报告",
                    description = "自动发送错误报告",
                    icon = Icons.Default.BugReport,
                    checked = settings.crashReportEnabled,
                    onCheckedChange = { onToggleSetting(SettingType.CRASH_REPORTS) }
                )
            }

            // 登出按钮
            Divider(modifier = Modifier.padding(vertical = 8.dp))

            TextButton(
                onClick = onLogout,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Logout,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "登出",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = MaterialTheme.colorScheme.error
                    )
                )
            }
        }
    }
}

@Composable
private fun SettingItemWithIcon(
    title: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        onClick = { onCheckedChange(!checked) },
        color = androidx.compose.ui.graphics.Color.Transparent
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 图标背景
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = androidx.compose.ui.graphics.Color(0xFF8B5CF6).copy(alpha = 0.1f),
                        shape = androidx.compose.foundation.shape.CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = androidx.compose.ui.graphics.Color(0xFF8B5CF6),
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // 文本内容
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Medium
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // 紫色主题的开关
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = androidx.compose.ui.graphics.Color.White,
                    checkedTrackColor = androidx.compose.ui.graphics.Color(0xFF8B5CF6),
                    uncheckedThumbColor = androidx.compose.ui.graphics.Color.White,
                    uncheckedTrackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                )
            )
        }
    }
}
