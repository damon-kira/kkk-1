package com.kira.learning.module.profile

import com.kira.learning.model.ProfileUiState
import com.kira.learning.model.AppSettings
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kira.learning.utils.AppAvatar

@Composable
fun ProfileRoute(modifier: Modifier = Modifier, viewModel: ProfileViewModel = hiltViewModel(), onBack: (() -> Unit)? = null) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    ProfileScreen(
        state = state,
        onStartEdit = viewModel::startEdit,
        onCancelEdit = viewModel::cancelEdit,
        onSave = viewModel::saveEdit,
        onName = viewModel::setName,
        onEmail = viewModel::setEmail,
        onBio = viewModel::setBio,
        onRandomAvatar = viewModel::randomUpdateAvatar,
        onToggleDark = viewModel::toggleDark,
        onToggleNotify = viewModel::toggleNotify,
        onToggleAutoPlay = viewModel::toggleAutoPlay,
        onToggleAnalytics = viewModel::toggleAnalytics,
        onToggleCrash = viewModel::toggleCrash,
        onRetry = viewModel::load,
        modifier = modifier,
        onBack = onBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileScreen(
    state: ProfileUiState,
    onStartEdit: () -> Unit,
    onCancelEdit: () -> Unit,
    onSave: () -> Unit,
    onName: (String) -> Unit,
    onEmail: (String) -> Unit,
    onBio: (String) -> Unit,
    onRandomAvatar: () -> Unit,
    onToggleDark: () -> Unit,
    onToggleNotify: () -> Unit,
    onToggleAutoPlay: () -> Unit,
    onToggleAnalytics: () -> Unit,
    onToggleCrash: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    onBack: (() -> Unit)? = null,
) {
    when (state) {
        ProfileUiState.Loading -> Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
        is ProfileUiState.Error -> Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(state.message)
                Spacer(Modifier.height(12.dp))
                Button(onClick = onRetry) { Text("重试") }
            }
        }
        is ProfileUiState.Data -> {
            val d = state
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("个人信息") },
                        navigationIcon = {
                            if (onBack != null) {
                                IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null) }
                            }
                        },
                        actions = {
                            if (!d.editing) {
                                IconButton(onClick = onStartEdit) { Icon(Icons.Default.Edit, null) }
                                IconButton(onClick = onRandomAvatar) { Icon(Icons.Default.Refresh, null) }
                            }
                        }
                    )
                }
            ) { padding ->
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(padding)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AppAvatar(
                            url = d.profile.avatarUrl,
                            size = 96.dp,
                            modifier = Modifier
                        )
                        Spacer(Modifier.width(16.dp))
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(d.profile.name, style = MaterialTheme.typography.titleMedium)
                            Text(d.profile.email, style = MaterialTheme.typography.bodySmall)
                            Text(d.profile.bio, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }

                    if (d.editing) {
                        OutlinedTextField(value = d.editName, onValueChange = onName, label = { Text("姓名") }, modifier = Modifier.fillMaxWidth())
                        OutlinedTextField(value = d.editEmail, onValueChange = onEmail, label = { Text("邮箱") }, modifier = Modifier.fillMaxWidth())
                        OutlinedTextField(value = d.editBio, onValueChange = onBio, label = { Text("简介") }, modifier = Modifier.fillMaxWidth(), maxLines = 3)
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Button(onClick = onSave, enabled = !d.saving) { Text(if (d.saving) "保存中" else "保存") }
                            OutlinedButton(onClick = onCancelEdit, enabled = !d.saving) { Text("取消") }
                        }
                    }

                    SettingsSection(
                        settings = d.settings,
                        onToggleDark = onToggleDark,
                        onToggleNotify = onToggleNotify,
                        onToggleAutoPlay = onToggleAutoPlay,
                        onToggleAnalytics = onToggleAnalytics,
                        onToggleCrash = onToggleCrash,
                    )

                    if (d.message != null) Text(d.message, color = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}

@Composable
private fun SettingsSection(
    settings: AppSettings,
    onToggleDark: () -> Unit,
    onToggleNotify: () -> Unit,
    onToggleAutoPlay: () -> Unit,
    onToggleAnalytics: () -> Unit,
    onToggleCrash: () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("应用设置", style = MaterialTheme.typography.titleSmall)
        SettingRow("深色模式", settings.darkMode, onToggleDark)
        SettingRow("消息通知", settings.notificationsEnabled, onToggleNotify)
        SettingRow("视频自动播放", settings.autoPlayVideo, onToggleAutoPlay)
        SettingRow("分析上报", settings.analyticsEnabled, onToggleAnalytics)
        SettingRow("崩溃报告", settings.crashReportEnabled, onToggleCrash)
    }
}

@Composable
private fun SettingRow(label: String, checked: Boolean, onToggle: () -> Unit) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(label, modifier = Modifier.weight(1f))
        Switch(checked = checked, onCheckedChange = { onToggle() })
    }
}
