package com.kira.learning.compose

import android.os.Bundle
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Menu
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
import com.kira.learning.compose.module.sample.SampleFeatureRoute
import com.kira.learning.compose.module.answer.AnswerRoute
import com.kira.learning.compose.module.profile.ProfileRoute
import com.kira.learning.compose.module.profile.ProfileUiState
import com.kira.learning.compose.module.profile.ProfileViewModel
import com.kira.learning.compose.ui.image.AppAvatar
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavDestination.Companion.hierarchy
import com.kira.learning.compose.module.chat.ChatRoute

private object Routes {
    // 重新排序更贴近微信: 聊天(微信) -> 发现 -> 学习 -> 我
    const val CHAT = "chat"
    const val SAMPLE = "sample"
    const val ANSWER = "answer"
    const val PROFILE = "profile"
}

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
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination

    val drawerState: DrawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val bottomItems = listOf(
        BottomItem(Routes.CHAT, "微信") { Icon(Icons.Default.Chat, null) },
        BottomItem(Routes.SAMPLE, "发现") { Icon(Icons.Default.Home, null) },
        BottomItem(Routes.ANSWER, "学习") { Icon(Icons.Default.School, null) },
        BottomItem(Routes.PROFILE, "我") { Icon(Icons.Default.Person, null) },
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(onNavigateProfile = {
                scope.launch { drawerState.close() }
                navController.navigate(Routes.PROFILE) {
                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            })
        }
    ) {
        Scaffold(
            bottomBar = {
                NavigationBar {
                    bottomItems.forEach { item ->
                        val selected = currentDestination.isRouteInHierarchy(item.route)
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = item.icon,
                            label = { Text(item.label) }
                        )
                    }
                }
            }
        ) { padding ->
            NavHost(
                navController = navController,
                startDestination = Routes.CHAT,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                composable(Routes.CHAT) { ChatRoute(Modifier.fillMaxSize(), openDrawer = { scope.launch { drawerState.open() } }) }
                composable(Routes.SAMPLE) { SampleFeatureRoute(Modifier.fillMaxSize(), openDrawer = { scope.launch { drawerState.open() } }) }
                composable(Routes.ANSWER) { AnswerRoute(Modifier.fillMaxSize(), openDrawer = { scope.launch { drawerState.open() } }) }
                composable(Routes.PROFILE) { ProfileRoute(Modifier.fillMaxSize(), onBack = null) }
            }
        }
    }
}

@Composable
private fun DrawerContent(
    onNavigateProfile: () -> Unit,
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    val state = profileViewModel.uiState.collectAsStateWithLifecycle()
    ModalDrawerSheet {
        Spacer(Modifier.height(32.dp))
        when (val s = state.value) {
            is ProfileUiState.Data -> {
                Row(
                    Modifier.padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AppAvatar(url = s.profile.avatarUrl, size = 64.dp)
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(
                            s.profile.name,
                            style = MaterialTheme.typography.titleMedium
                        ); Text(s.profile.email, style = MaterialTheme.typography.bodySmall)
                    }
                }
                Spacer(Modifier.height(12.dp))
            }

            is ProfileUiState.Loading -> Row(
                Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircularProgressIndicator(Modifier.size(32.dp)); Spacer(Modifier.width(12.dp)); Text(
                "加载中"
            )
            }

            is ProfileUiState.Error -> Text("用户信息加载失败", modifier = Modifier.padding(16.dp))
        }
        NavigationDrawerItem(
            label = { Text("个人信息") },
            selected = false,
            onClick = onNavigateProfile,
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
        NavigationDrawerItem(
            label = { Text("设置(占位)") },
            selected = false,
            onClick = {},
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
        NavigationDrawerItem(
            label = { Text("关于(占位)") },
            selected = false,
            onClick = {},
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
    }
}

@Composable
private fun PlaceholderScreen(text: String) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Text(text = text, modifier = Modifier)
    }
}

private fun NavDestination?.isRouteInHierarchy(route: String): Boolean =
    this?.hierarchy?.any { it.route == route } == true
