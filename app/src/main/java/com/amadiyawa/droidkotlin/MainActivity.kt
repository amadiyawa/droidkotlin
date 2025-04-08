package com.amadiyawa.droidkotlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.core.view.WindowCompat
import com.amadiyawa.droidkotlin.presentation.screen.appentry.MainScreen

/**
 * MainActivity is the main entry point of the application.
 *
 * This class extends [ComponentActivity] and is responsible for setting up
 * the application's user interface. It configures full-screen display, enables
 * Edge-to-Edge functionality, and sets the content of the application using the [MainScreen] composable.
 */
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge()
        setContent {
            MainScreen(windowSizeClass = calculateWindowSizeClass(activity = this@MainActivity))
        }
    }
}