package com.kira.learning.module.sample

import com.kira.learning.model.SampleUiState
import com.kira.learning.model.SampleEvent
import com.kira.learning.model.SampleImage
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kira.learning.utils.AppImage
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.foundation.layout.WindowInsets

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SampleFeatureRoute(
    modifier: Modifier = Modifier,
    viewModel: SampleViewModel = hiltViewModel(),
    openDrawer: () -> Unit = {}
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state) {
        if (state is SampleUiState.Error) {
            snackbarHostState.showSnackbar((state as SampleUiState.Error).message)
        }
    }

    when (state) {
        SampleUiState.Idle, SampleUiState.Loading -> LoadingState(modifier)
        is SampleUiState.Error -> ErrorState(
            (state as SampleUiState.Error).message,
            { viewModel.dispatch(SampleEvent.Retry()) },
            modifier
        )

        is SampleUiState.Success -> SuccessList(
            success = state as SampleUiState.Success,
            onRefresh = { viewModel.dispatch(SampleEvent.Refresh) },
            snackbarHostState = snackbarHostState,
            modifier = modifier,
            openDrawer = openDrawer
        )
    }
}

@Composable
private fun LoadingState(modifier: Modifier) {
    Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
}

@Composable
private fun ErrorState(message: String, onRetry: () -> Unit, modifier: Modifier) {
    Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error
            )
            Spacer(Modifier.height(12.dp))
            Button(onClick = onRetry) { Text("重试") }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
private fun SuccessList(
    success: SampleUiState.Success,
    onRefresh: () -> Unit,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier,
    openDrawer: () -> Unit,
) {
    // material pullRefresh 状态
    val pullRefreshState = rememberPullRefreshState(
        refreshing = success.refreshing,
        onRefresh = onRefresh
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Compose 示例") },
                navigationIcon = { IconButton(onClick = openDrawer) { Icon(Icons.Default.Menu, null) } },
                actions = {
                    TextButton(onClick = onRefresh, enabled = !success.refreshing) {
                        Text(if (success.refreshing) "刷新中..." else "刷新")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        contentWindowInsets = WindowInsets(0) // 避免顶部额外留白
    ) { padding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .pullRefresh(pullRefreshState)
        ) {
            LazyColumn(Modifier.fillMaxSize()) {
                items(success.data, key = { it.id }) { item -> SampleRow(item) }
            }
            PullRefreshIndicator(
                refreshing = success.refreshing,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}

@Composable
private fun SampleRow(item: SampleImage) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AppImage(
            url = item.thumbnailUrl,
            contentDescription = item.title,
            modifier = Modifier.size(64.dp)
        )
        Spacer(Modifier.width(12.dp))
        Text(
            text = item.title,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
    }
    HorizontalDivider()
}
