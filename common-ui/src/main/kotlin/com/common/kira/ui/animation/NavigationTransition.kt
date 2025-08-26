package com.common.kira.ui.animation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally

object NavigationTransition {

    private const val DURATION = 250

    val EnterTransition = fadeIn(tween(DURATION)) + slideInHorizontally { it / 2 }
    val ExitTransition = fadeOut(tween(DURATION)) + slideOutHorizontally { -it / 2 }
    val PopEnterTransition = fadeIn(tween(DURATION)) + slideInHorizontally { -it / 2 }
    val PopExitTransition = fadeOut(tween(DURATION)) + slideOutHorizontally { it / 2 }
}