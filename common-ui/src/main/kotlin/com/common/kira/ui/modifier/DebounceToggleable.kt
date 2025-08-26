package com.common.kira.ui.modifier

import androidx.compose.foundation.Indication
import androidx.compose.foundation.IndicationNodeFactory
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.selection.toggleable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.semantics.Role

fun Modifier.debounceToggleable(
    value: Boolean,
    role: Role? = null,
    enabled: Boolean = true,
    debounce: Boolean = true,
    debounceMs: Long = DefaultMs,
    onClick: () -> Unit
) = composed(
    inspectorInfo = debugInspectorInfo {
        name = "debounceToggleable"
        properties["value"] = value
        properties["role"] = role
        properties["enabled"] = enabled
        properties["debounce"] = debounce
        properties["debounceMs"] = debounceMs
        properties["onClick"] = onClick
    }
) {
    val localIndication = LocalIndication.current
    val interactionSource = if (localIndication is IndicationNodeFactory) {
        // We can fast path here as it will be created inside clickable lazily
        null
    } else {
        // We need an interaction source to pass between the indication modifier and clickable, so
        // by creating here we avoid another composed down the line
        remember { MutableInteractionSource() }
    }
    Modifier.debounceToggleable(
        value = value,
        interactionSource = interactionSource,
        indication = localIndication,
        role = role,
        enabled = enabled,
        debounce = debounce,
        debounceMs = debounceMs,
        onClick = onClick
    )
}

fun Modifier.debounceToggleable(
    value: Boolean,
    interactionSource: MutableInteractionSource?,
    indication: Indication?,
    role: Role? = null,
    enabled: Boolean = true,
    debounce: Boolean = true,
    debounceMs: Long = DefaultMs,
    onClick: (() -> Unit)? = null,
): Modifier {
    val onClickLambda = onClick?.let { lambda ->
        if (debounce) {
            debounceLambda(lambda, debounceMs)
        } else {
            lambda
        }
    }
    if (onClickLambda == null) {
        return this
    }
    return this.toggleable(
        value = value,
        interactionSource = interactionSource,
        indication = indication,
        role = role,
        enabled = enabled,
        onValueChange = { onClickLambda() },
    )
}