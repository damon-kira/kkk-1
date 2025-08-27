package com.steve28.stedit

import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.steve28.stedit.highlighter.Highlighter

class SteditField {
    var text = ""

    @Composable
    fun Compose(
        modifier: Modifier = Modifier, highlighter: Highlighter,
        maxLines: Int = Int.MAX_VALUE
    ) {
        val remText = remember { mutableStateOf(text) }
        TextField(
            value = remText.value,
            onValueChange = { remText.value = it; text = it },
            visualTransformation = highlighter,
            modifier = modifier,
            maxLines = maxLines,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFF003566),
                unfocusedContainerColor = Color(0xFF002240),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White.copy(alpha = 0.8f),
                cursorColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedLeadingIconColor = Color.White,
                unfocusedLeadingIconColor = Color.White.copy(alpha = 0.6f),
            )
        )
    }
}