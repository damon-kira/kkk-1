package com.kira.learning.module.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.kira.learning.utils.image.ProvideImageTheme
import com.kira.learning.utils.image.ImageTheme
import androidx.compose.material3.MaterialTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            ProvideImageTheme(
                ImageTheme(
                    placeholderColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                    errorColor = MaterialTheme.colorScheme.error.copy(alpha = 0.25f),
                    iconTint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            ) {
                MainScreen(savedInstanceState)
            }
        }
    }
}