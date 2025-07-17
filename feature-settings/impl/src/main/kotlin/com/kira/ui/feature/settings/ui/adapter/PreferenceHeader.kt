

package com.kira.ui.feature.settings.ui.adapter

import com.kira.ui.core.navigation.Screen

data class PreferenceHeader(
    val title: String,
    val subtitle: String,
    val selected: Boolean,
    val screen: Screen<*>,
)