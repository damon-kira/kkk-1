package com.kira.learning.compose

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kira.learning.compose.extensions.toComposeColors
import com.kira.learning.compose.module.SampleFeatureRoute
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize

private object Routes { const val SAMPLE = "sample" }

@Composable
internal fun MainScreen(
    savedInstanceState: Bundle? = null,
    viewModel: MainViewModel = hiltViewModel(),
) {
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()
    val colors = toComposeColors(viewState.colorScheme)

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.SAMPLE,
        modifier = Modifier.fillMaxSize()
    ) {
        composable(Routes.SAMPLE) {
            SampleFeatureRoute(Modifier.fillMaxSize())
        }
    }
}