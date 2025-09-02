package com.kira.learning.module.auth

import android.os.Bundle
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kira.learning.module.main.MainScreen

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    vm: AuthViewModel = hiltViewModel(),
) {
    val uiState by vm.uiState.collectAsState()
    val focusManager = LocalFocusManager.current
    var showPwd by remember { mutableStateOf(false) }

    LaunchedEffect(uiState) {
        if (uiState is LoginUiState.Success) {
            onLoginSuccess()
        }
    }

    Surface(Modifier.fillMaxSize()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Text("登录", style = MaterialTheme.typography.headlineMedium)
                OutlinedTextField(
                    value = vm.email.collectAsState().value,
                    onValueChange = vm::onEmailChange,
                    leadingIcon = { Icon(Icons.Default.Email, null) },
                    label = { Text("邮箱") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                )
                OutlinedTextField(
                    value = vm.password.collectAsState().value,
                    onValueChange = vm::onPasswordChange,
                    leadingIcon = { Icon(Icons.Default.Lock, null) },
                    label = { Text("密码") },
                    singleLine = true,
                    visualTransformation = if (showPwd) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val text = if (showPwd) "隐藏" else "显示"
                        TextButton(onClick = { showPwd = !showPwd }) { Text(text) }
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus(); if (uiState !is LoginUiState.Loading) vm.login() })
                )
                val loading = uiState is LoginUiState.Loading
                Button(
                    onClick = { vm.login() },
                    enabled = !loading,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (loading) CircularProgressIndicator(
                        Modifier.size(18.dp),
                        strokeWidth = 2.dp
                    ) else Text("邮箱密码登录")
                }
                OutlinedButton(
                    onClick = { /* TODO: Google Sign-In 集成 */ },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !loading
                ) { Text("Google 登录 (占位)") }
                when (uiState) {
                    is LoginUiState.Error -> Text(
                        (uiState as LoginUiState.Error).message,
                        color = MaterialTheme.colorScheme.error
                    )

                    LoginUiState.Success -> Text(
                        "登录成功，跳转中...",
                        color = MaterialTheme.colorScheme.primary
                    )

                    else -> {}
                }
            }
        }
    }
}
