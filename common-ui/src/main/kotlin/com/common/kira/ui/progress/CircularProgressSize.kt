package com.common.kira.ui.progress

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class CircularProgressSize(
    val circleSize: Dp,
    val strokeWidth: Dp,
)

object CircularProgressSizeDefaults {

    val XS: CircularProgressSize
        get() = CircularProgressSize(
            circleSize = 12.dp,
            strokeWidth = 2.dp,
        )

    val S: CircularProgressSize
        get() = CircularProgressSize(
            circleSize = 28.dp,
            strokeWidth = 3.dp,
        )

    val M: CircularProgressSize
        get() = CircularProgressSize(
            circleSize = 40.dp,
            strokeWidth = 4.dp,
        )
}