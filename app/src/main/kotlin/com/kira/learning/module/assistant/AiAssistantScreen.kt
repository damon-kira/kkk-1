package com.kira.learning.module.assistant

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kira.learning.navigation.AppRoutes

internal data class AiFeature(
    val title: String,
    val icon: @Composable () -> Unit,
    val onClick: () -> Unit,
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
internal fun AiAssistantRoute(
    modifier: Modifier = Modifier,
    openDrawer: () -> Unit = {},
    navigate: (String) -> Unit,
) {
    val features = listOf(
        AiFeature("聊天", { Icon(Icons.AutoMirrored.Filled.Chat, null) }) { navigate(AppRoutes.CHAT) },
        AiFeature("图片识别", { Icon(Icons.Default.Image, null) }) { navigate(AppRoutes.OCR) },
        AiFeature("导航示例", { Icon(Icons.Default.List, null) }) { navigate(AppRoutes.NAV_DEMO) },
        AiFeature("设置(占位)", { Icon(Icons.Default.Settings, null) }) { },
    )
    Scaffold(
        modifier = modifier,
        topBar = { TopAppBar(title = { Text("AI助手") }) }
    ) { padding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(features) { f ->
                Card(
                    onClick = f.onClick,
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        f.icon()
                        Text(f.title, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}
