package com.kira.learning.modules.auth

import android.os.Bundle
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    val loggedIn by sessionVm.loggedIn.collectAsState()
    if (loggedIn) {
        MainScreen(savedInstanceState)
    } else {
        LoginScreen(onLoginSuccess = { sessionVm.notifyLoginSuccess() })
    }
}

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
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
                else -> {}
            }
        }
    }

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            onLoginSuccess()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        LoginContent(
            state = state,
            onAction = viewModel::handleAction,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        )
    }
}

@Composable
private fun LoginContent(
    state: AuthViewState,
    onAction: (AuthEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
    ) {
        // 标题
        Text(
            text = "欢迎回来",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "请登录您的账户",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(32.dp))

        // 登录表单
        LoginForm(
            state = state,
            onAction = onAction,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun LoginForm(
    state: AuthViewState,
    onAction: (AuthEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        val emailField = state.formState.fields["email"]
        val passwordField = state.formState.fields["password"]

        // 邮箱输入
        InputField(
            value = emailField?.value ?: "",
            onValueChange = { onAction(AuthEvent.UpdateEmail(it)) },
            label = "邮箱",
            placeholder = "请输入邮箱地址",
            errorMessage = emailField?.error,
            enabled = !state.isLoading,
            imeAction = ImeAction.Next
        )

        // 密码输入
        PasswordField(
            value = passwordField?.value ?: "",
            onValueChange = { onAction(AuthEvent.UpdatePassword(it)) },
            label = "密码",
            placeholder = "请输入密码",
            errorMessage = passwordField?.error,
            enabled = !state.isLoading,
            imeAction = ImeAction.Done,
            onImeAction = {
                if (state.formState.isValid && !state.isLoading) {
                    onAction(AuthEvent.Login)
                }
            }
        )

        // 错误消息
        state.errorMessage?.let { message ->
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text(
                    text = message,
                    modifier = Modifier.padding(12.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 登录按钮
        Button(
            onClick = { onAction(AuthEvent.Login) },
            enabled = state.formState.isValid && !state.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("登录中...")
            } else {
                Text("登录")
            }
        }

        // Google 登录按钮（占位）
        OutlinedButton(
            onClick = { /* TODO: Google Sign-In 集成 */ },
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isLoading
        ) {
            Text("Google 登录 (占位)")
        }

        // 忘记密码链接
        TextButton(
            onClick = { /* TODO: 实现忘记密码功能 */ },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("忘记密码？")
        }
    }
}
