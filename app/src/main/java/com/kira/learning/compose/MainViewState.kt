package com.kira.learning.compose

import androidx.compose.runtime.Immutable
import com.kira.learning.compose.model.ColorScheme
import com.kira.ui.core.mvi.ViewState

@Immutable
data class MainViewState(
    val isLoading: Boolean = true,
    val colorScheme: ColorScheme? = null,
    val fullscreenMode: Boolean = false,
) : ViewState()