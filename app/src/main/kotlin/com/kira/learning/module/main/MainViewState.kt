package com.kira.learning.module.main

import androidx.compose.runtime.Immutable
import com.kira.learning.model.ColorScheme
import com.kira.ui.core.mvi.ViewState

@Immutable
data class MainViewState(
    val isLoading: Boolean = true,
    val colorScheme: ColorScheme? = null,
    val fullscreenMode: Boolean = false,
) : ViewState()