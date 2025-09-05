package com.kira.learning.base.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * 底部导航项数据模型
 */
data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector,
    val selectedIcon: ImageVector? = null,
    val badge: String? = null
)

/**
 * 通用底部导航栏
 */
@Composable
fun CommonBottomNavigation(
    items: List<BottomNavItem>,
    currentRoute: String?,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(modifier = modifier) {
        items.forEach { item ->
            val selected = currentRoute == item.route
            NavigationBarItem(
                icon = {
                    BadgedBox(
                        badge = {
                            if (item.badge != null) {
                                Badge { Text(item.badge) }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (selected && item.selectedIcon != null) {
                                item.selectedIcon
                            } else {
                                item.icon
                            },
                            contentDescription = item.label
                        )
                    }
                },
                label = { Text(item.label) },
                selected = selected,
                onClick = { onItemClick(item.route) }
            )
        }
    }
}

/**
 * 抽屉导航项数据模型
 */
data class DrawerNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector,
    val badge: String? = null
)

/**
 * 通用抽屉导航内容
 */
@Composable
fun CommonDrawerContent(
    items: List<DrawerNavItem>,
    currentRoute: String?,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    header: @Composable (() -> Unit)? = null
) {
    ModalDrawerSheet(modifier = modifier) {
        if (header != null) {
            header()
            HorizontalDivider()
        }

        items.forEach { item ->
            NavigationDrawerItem(
                icon = {
                    BadgedBox(
                        badge = {
                            if (item.badge != null) {
                                Badge { Text(item.badge) }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.label
                        )
                    }
                },
                label = { Text(item.label) },
                selected = currentRoute == item.route,
                onClick = { onItemClick(item.route) },
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
            )
        }
    }
}
