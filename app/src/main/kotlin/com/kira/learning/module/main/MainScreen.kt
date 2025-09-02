package com.kira.learning.module.main

import android.os.Bundle
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.kira.learning.module.sample.SampleFeatureRoute
import com.kira.learning.module.answer.AnswerRoute
import com.kira.learning.module.profile.ProfileRoute
import com.kira.learning.module.profile.ProfileUiState
import com.kira.learning.module.profile.ProfileViewModel
import com.kira.learning.utils.image.AppAvatar
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
import com.kira.learning.module.assistant.AiAssistantRoute
import com.kira.learning.module.chat.ChatRoute
import com.kira.learning.module.chat.ChatDialog
import com.kira.learning.module.navigation.NavigationDemoRoute
import com.kira.learning.module.uidemo.UiComponentsDemoRoute
import com.kira.learning.module.ocr.ImageOcrRoute

private object Routes {
    const val CHAT = "chat"
    const val SAMPLE = "sample"
    const val ANSWER = "answer"
    const val PROFILE = "profile"
    const val NAV_DEMO = "nav_demo"
    const val UI_DEMO = "ui_demo"
    const val OCR = "ocr" // 图片识别
    const val ASSISTANT = "assistant" // AI 助手
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
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination

    val drawerState: DrawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // 全局聊天 Dialog 显示状态
    var showGlobalChat by remember { mutableStateOf(false) }

    val bottomItems = listOf(
        BottomItem(Routes.CHAT, "微信") { Icon(Icons.AutoMirrored.Filled.Chat, null) },
        BottomItem(Routes.SAMPLE, "发现") { Icon(Icons.Default.Home, null) },
        BottomItem(Routes.ANSWER, "学习") { Icon(Icons.Default.School, null) },
        BottomItem(Routes.PROFILE, "我") { Icon(Icons.Default.Person, null) },
    )

    ModalNavigationDrawer(
        drawerState = drawerState, drawerContent = {
            DrawerContent(currentRoute = currentDestination?.route, onNavigateProfile = {
                scope.launch { drawerState.close() }
                navController.navigate(Routes.PROFILE) {
                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            }, onNavigateAssistant = {
                scope.launch { drawerState.close() }
                navController.navigate(Routes.ASSISTANT) {
                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            }, onNavigateNavDemo = {
                scope.launch { drawerState.close() }
                navController.navigate(Routes.NAV_DEMO) {
                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            }, onNavigateUiDemo = {
                scope.launch { drawerState.close() }
                navController.navigate(Routes.UI_DEMO) {
                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            }, onNavigateOcr = {
                scope.launch { drawerState.close() }
                navController.navigate(Routes.OCR) {
                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            })
        }) {
        Scaffold(
            bottomBar = {
                NavigationBar {
                    bottomItems.forEach { item ->
                        val selected = currentDestination.isRouteInHierarchy(item.route)
                        NavigationBarItem(selected = selected, onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }, icon = item.icon, label = { Text(item.label) })
                    }
                }
            }) { padding ->
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        start = 0.dp,
                        top = 0.dp,
                        end = 0.dp,
                        bottom = padding.calculateBottomPadding()
                    )
            ) {
                // 主内容 NavHost
                NavHost(
                    navController = navController,
                    startDestination = Routes.CHAT,
                    modifier = Modifier.fillMaxSize()
                ) {
                    composable(Routes.CHAT) {
                        ChatRoute(
                            Modifier.fillMaxSize(),
                            openDrawer = { scope.launch { drawerState.open() } })
                    }
                    composable(Routes.SAMPLE) {
                        SampleFeatureRoute(
                            Modifier.fillMaxSize(),
                            openDrawer = { scope.launch { drawerState.open() } })
                    }
                    composable(Routes.ANSWER) {
                        AnswerRoute(
                            Modifier.fillMaxSize(),
                            openDrawer = { scope.launch { drawerState.open() } })
                    }
                    composable(Routes.PROFILE) {
                        ProfileRoute(
                            Modifier.fillMaxSize(), onBack = null
                        )
                    }
                    composable(Routes.NAV_DEMO) {
                        NavigationDemoRoute(
                            Modifier.fillMaxSize(),
                            openDrawer = { scope.launch { drawerState.open() } })
                    }
                    composable(Routes.UI_DEMO) {
                        UiComponentsDemoRoute(
                            Modifier.fillMaxSize(),
                            openDrawer = { scope.launch { drawerState.open() } })
                    }
                    composable(Routes.OCR) {
                        ImageOcrRoute(
                            Modifier.fillMaxSize(),
                            openDrawer = { scope.launch { drawerState.open() } })
                    }
                    composable(Routes.ASSISTANT) {
                        AiAssistantRoute(Modifier.fillMaxSize(), navigate = { route ->
                            navController.navigate(route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true; restoreState = true
                            }
                        })
                    }
                }

                // 可拖动悬浮按钮
                val density = LocalDensity.current
                val margin = 16.dp
                val buttonSize = 56.dp
                val marginPx = with(density) { margin.toPx() }
                val buttonSizePx = with(density) { buttonSize.toPx() }
                val bottomBarHeightPx = with(density) { 80.dp.toPx() } // 近似底部导航高度

                var offsetX by remember { mutableFloatStateOf(0f) }
                var offsetY by remember { mutableFloatStateOf(0f) }
                var inited by remember { mutableStateOf(false) }

                val maxW = constraints.maxWidth.toFloat()
                val maxH = constraints.maxHeight.toFloat()
                LaunchedEffect(maxW, maxH) {
                    if (!inited && maxW > 0 && maxH > 0) {
                        offsetX = maxW - buttonSizePx - marginPx
                        offsetY = maxH - buttonSizePx - bottomBarHeightPx - marginPx
                        inited = true
                    }
                }

                FloatingActionButton(
                    onClick = { showGlobalChat = true },
                    modifier = Modifier
                        .offset {
                            IntOffset(
                                offsetX.roundToInt(), offsetY.roundToInt()
                            )
                        }
                        .pointerInput(maxW, maxH) {
                            detectDragGestures { change, dragAmount ->
                                change.consume()
                                val newX =
                                    (offsetX + dragAmount.x).coerceIn(0f, maxW - buttonSizePx)
                                val newY =
                                    (offsetY + dragAmount.y).coerceIn(0f, maxH - buttonSizePx)
                                offsetX = newX
                                offsetY = newY
                            }
                        }) {
                    Icon(Icons.AutoMirrored.Filled.Chat, contentDescription = "聊天")
                }
            }
        }
        // 放在 Scaffold 之后以确保覆盖层级
        if (showGlobalChat) {
            ChatDialog(onDismiss = { showGlobalChat = false })
        }
    }
}

// 修改抽屉，添加 AI助手 项
@Composable
private fun DrawerContent(
    currentRoute: String?,
    onNavigateProfile: () -> Unit,
    onNavigateAssistant: () -> Unit,
    onNavigateNavDemo: () -> Unit,
    onNavigateUiDemo: () -> Unit,
    onNavigateOcr: () -> Unit,
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
                            s.profile.name, style = MaterialTheme.typography.titleMedium
                        ); Text(s.profile.email, style = MaterialTheme.typography.bodySmall)
                    }
                }
                Spacer(Modifier.height(12.dp))
            }

            is ProfileUiState.Loading -> Row(
                Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically
            ) {
                CircularProgressIndicator(Modifier.size(32.dp)); Spacer(Modifier.width(12.dp)); Text(
                "加载中"
            )
            }

            is ProfileUiState.Error -> Text("用户信息加载失败", modifier = Modifier.padding(16.dp))
        }
        NavigationDrawerItem(
            label = { Text("个人信息") },
            selected = currentRoute == Routes.PROFILE,
            onClick = onNavigateProfile,
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
        NavigationDrawerItem(
            label = { Text("AI助手") },
            selected = currentRoute == Routes.ASSISTANT,
            onClick = onNavigateAssistant,
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
        NavigationDrawerItem(
            label = { Text("导航示例") },
            selected = currentRoute == Routes.NAV_DEMO,
            onClick = onNavigateNavDemo,
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
        NavigationDrawerItem(
            label = { Text("UI组件演示") },
            selected = currentRoute == Routes.UI_DEMO,
            onClick = onNavigateUiDemo,
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
        NavigationDrawerItem(
            label = { Text("图片识别") },
            selected = currentRoute == Routes.OCR,
            onClick = onNavigateOcr,
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
        NavigationDrawerItem(
            label = { Text("设置(占位)") },
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
