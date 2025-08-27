package com.kira.learning.compose

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.kira.learning.compose.extensions.toComposeColors

@Composable
internal fun MainScreen(
    savedInstanceState: Bundle? = null,
    viewModel: MainViewModel = hiltViewModel(),
) {
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()
    val colors = toComposeColors(viewState.colorScheme)

    val navController = rememberNavController()

}