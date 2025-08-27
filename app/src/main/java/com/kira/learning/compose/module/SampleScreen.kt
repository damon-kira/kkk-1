package com.kira.learning.compose.module

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.ExperimentalMaterialApi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SampleFeatureRoute(
    modifier: Modifier = Modifier,
    viewModel: SampleViewModel = hiltViewModel(),
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
            modifier = modifier
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
                actions = {
                    TextButton(onClick = onRefresh, enabled = !success.refreshing) {
                        Text(if (success.refreshing) "刷新中..." else "刷新")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
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
    val painter = rememberAsyncImagePainter(model = item.thumbnailUrl)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painter,
            contentDescription = item.title,
            modifier = Modifier.size(64.dp),
            contentScale = ContentScale.Crop
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
