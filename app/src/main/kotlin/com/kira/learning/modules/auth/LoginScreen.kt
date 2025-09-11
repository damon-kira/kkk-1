package com.kira.learning.modules.auth

import android.os.Bundle
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kira.learning.modules.main.MainScreen
import com.kira.learning.base.components.InputField
import com.kira.learning.base.components.PasswordField
import com.kira.learning.base.mvi.UiEvent

@Composable
fun AppRoot(savedInstanceState: Bundle? = null) {
    val sessionVm: SessionViewModel = hiltViewModel()
    val loggedIn by sessionVm.loggedIn.collectAsStateWithLifecycle()
    val currentSession by sessionVm.currentSession.collectAsStateWithLifecycle()

    // 监听认证事件
    LaunchedEffect(Unit) {
        AuthEventBus.events.collect { event ->
            when (event) {
                is AuthEventBus.AuthEvent.SessionExpired -> {
                    // 会话过期处理
                }
                is AuthEventBus.AuthEvent.NetworkError -> {
                    // 网络错误处理
                }
                else -> { /* 其他事件 */ }
            }
        }
    }

    // 修复智能转换问题：使用本地变量
    val session = currentSession
    if (loggedIn && session != null && session.loginData.isTokenValid) {
        MainScreen(savedInstanceState)
    } else {
        LoginScreen(
            onLoginSuccess = { sessionVm.notifyLoginSuccess() },
            historicalSession = if (!loggedIn) session else null
        )
    }
}

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    historicalSession: com.kira.learning.models.UserSession? = null,
    viewModel: AuthViewModel = hiltViewModel(),
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
                    if (event.route == "main") {
                        onLoginSuccess()
                    }
                }
                else -> { /* 其他事件 */ }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // 应用标题
            Text(
                text = "Kira Learning",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // 显示历史会话信息（如果存在且当前未登录）
            historicalSession?.let { session ->
                if (!state.isSuccess) {
                    HistoricalSessionCard(
                        session = session,
                        onQuickLogin = {
                            // 使用历史会话快速登录
                            viewModel.handleAction(AuthEvent.QuickLogin(session))
                        },
                        onRemoveSession = {
                            // 移除历史会话
                            viewModel.handleAction(AuthEvent.RemoveSession(session))
                        }
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }

            // 登录表单
            LoginForm(
                state = state,
                onEmailChange = { email ->
                    viewModel.handleAction(AuthEvent.UpdateEmail(email))
                },
                onPasswordChange = { password ->
                    viewModel.handleAction(AuthEvent.UpdatePassword(password))
                },
                onRememberMeChange = { rememberMe ->
                    viewModel.handleAction(AuthEvent.UpdateRememberMe(rememberMe))
                },
                onLogin = {
                    viewModel.handleAction(AuthEvent.Login)
                }
            )

            // 错误信息
            state.errorMessage?.let { error ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = error,
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
        }
    }
}

@Composable
private fun HistoricalSessionCard(
    session: com.kira.learning.models.UserSession,
    onQuickLogin: () -> Unit,
    onRemoveSession: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "历史会话",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "上次登录: ${session.lastLoginTime}",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "IP地址: ${session.ipAddress}",
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onQuickLogin,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("快速登录")
                }

                OutlinedButton(
                    onClick = onRemoveSession,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("移除会话")
                }
            }
        }
    }
}

@Composable
private fun LoginForm(
    state: AuthViewState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRememberMeChange: (Boolean) -> Unit,
    onLogin: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 邮箱输入
        InputField(
            value = state.formState.fields["email"]?.value ?: "",
            onValueChange = onEmailChange,
            label = "邮箱",
            placeholder = "请输入邮箱地址",
            errorMessage = state.formState.fields["email"]?.error,
            imeAction = ImeAction.Next,
            enabled = !state.isLoading
        )

        // 密码输入
        PasswordField(
            value = state.formState.fields["password"]?.value ?: "",
            onValueChange = onPasswordChange,
            label = "密码",
            placeholder = "请输入密码",
            errorMessage = state.formState.fields["password"]?.error,
            imeAction = ImeAction.Done,
            enabled = !state.isLoading
        )

        // 记住密码��项
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Checkbox(
                checked = state.rememberMe,
                onCheckedChange = onRememberMeChange,
                enabled = !state.isLoading
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "记住登录状态",
                style = MaterialTheme.typography.bodyMedium
            )
        }

        // 登录按钮
        Button(
            onClick = onLogin,
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isLoading && state.formState.isValid
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    strokeWidth = 2.dp
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(if (state.isLoading) "登录中..." else "登录")
        }

        // Token刷新按钮（仅在有会话时显示）
        if (state.currentUser != null) {
            OutlinedButton(
                onClick = { /* 触发token刷新 */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("刷新Token")
            }
        }
    }
}
