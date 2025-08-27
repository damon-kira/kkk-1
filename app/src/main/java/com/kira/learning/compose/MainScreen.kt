package com.kira.learning.compose

import android.os.Bundle
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.kira.learning.compose.extensions.toComposeColors
import com.kira.learning.compose.module.SampleFeatureRoute
import com.kira.learning.compose.module.answer.AnswerRoute
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy

private object Routes { const val SAMPLE = "sample"; const val ANSWER = "answer"; const val CHAT = "chat" }

data class BottomItem(
    val route: String,
    val label: String,
    val icon: @Composable () -> Unit,
)

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("UNUSED_PARAMETER")
@Composable
internal fun MainScreen(
    savedInstanceState: Bundle? = null,
    viewModel: MainViewModel = hiltViewModel(),
) {
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()
    val colors = toComposeColors(viewState.colorScheme) // 预留主题转换
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination

    val bottomItems = listOf(
        BottomItem(Routes.SAMPLE, "首页") { Icon(Icons.Default.Home, null) },
        BottomItem(Routes.ANSWER, "答题") { Icon(Icons.Default.School, null) },
        BottomItem(Routes.CHAT, "聊天") { Icon(Icons.Default.Chat, null) },
    )

    Scaffold(
        bottomBar = {
            NavigationBar { bottomItems.forEach { item ->
                val selected = currentDestination.isRouteInHierarchy(item.route)
                NavigationBarItem(
                    selected = selected,
                    onClick = {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = item.icon,
                    label = { Text(item.label) }
                )
            } }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Routes.SAMPLE,
            modifier = Modifier.fillMaxSize().padding(padding)
        ) {
            composable(Routes.SAMPLE) { SampleFeatureRoute(Modifier.fillMaxSize()) }
            composable(Routes.ANSWER) { AnswerRoute(Modifier.fillMaxSize()) }
            composable(Routes.CHAT) { PlaceholderScreen("聊天功能占位") }
        }
    }
}

@Composable
private fun PlaceholderScreen(text: String) {
    Surface(modifier = Modifier.fillMaxSize()) {
        androidx.compose.material3.Text(text = text, modifier = Modifier)
    }
}

private fun NavDestination?.isRouteInHierarchy(route: String): Boolean = this?.hierarchy?.any { it.route == route } == true
