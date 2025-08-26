package com.common.kira.ui.modifier

import android.os.SystemClock

internal const val DefaultMs = 250L

internal fun debounceLambda(action: (() -> Unit), debounceMs: Long): (() -> Unit) {
    return {
        if (DebounceState.isClickAllowed(debounceMs)) {
            DebounceState.onClickInvoked()
            action.invoke()
        }
    }
}

private object DebounceState {

    private var lastClickTimestamp = 0L

    fun isClickAllowed(debounceMs: Long): Boolean {
        val delta = currentTimestamp() - lastClickTimestamp
        return delta !in 0L..debounceMs
    }

    fun onClickInvoked() {
        lastClickTimestamp = currentTimestamp()
    }

    private fun currentTimestamp(): Long {
        return SystemClock.elapsedRealtime()
    }
}