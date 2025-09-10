package com.kira.learning.modules.sample

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kira.learning.models.SampleImage
import com.kira.learning.utils.AppImage
import com.kira.learning.base.components.RefreshableList
import com.kira.learning.base.components.StateHandler
import com.kira.learning.base.mvi.BaseUiState
import com.kira.learning.base.mvi.UiEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SampleFeatureRoute(
    modifier: Modifier = Modifier,
    viewModel: SampleViewModel = hiltViewModel(),
    openDrawer: () -> Unit = {}
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

                else -> {}
            }
        }
    }

    Scaffold(topBar = {
        CenterAlignedTopAppBar(title = { Text("Compose 示例") }, navigationIcon = {
            IconButton(onClick = openDrawer) {
                Icon(Icons.Default.Menu, null)
            }
        }, actions = {
            TextButton(
                onClick = { viewModel.handleAction(SampleEvent.RefreshSamples) },
                enabled = state.data !is BaseUiState.Loading
            ) {
                Text("刷新")
            }
        })
    }, snackbarHost = { SnackbarHost(snackbarHostState) }) { paddingValues ->
        StateHandler(
            state = state.data,
            onRetry = { viewModel.handleAction(SampleEvent.RetrySamples) }) { data, stateModifier ->
            SampleSuccessContent(
                images = data,
                refreshing = (state.data as? BaseUiState.Success)?.refreshing == true,
                onRefresh = { viewModel.handleAction(SampleEvent.RefreshSamples) },
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .then(stateModifier)
            )
        }
    }
}

@Composable
private fun SampleSuccessContent(
    images: List<SampleImage>,
    refreshing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    RefreshableList(
        refreshing = refreshing, onRefresh = onRefresh, modifier = modifier, // 应用传入的 modifier
        contentPadding = PaddingValues(16.dp)
    ) {
        items(images) { image ->
            SampleImageItem(
                image = image, modifier = Modifier.padding(bottom = 12.dp)
            )
        }
    }
}

@Composable
private fun SampleImageItem(
    image: SampleImage, modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            AppImage(
                url = image.thumbnailUrl, modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = image.title, style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "ID: ${image.id}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
