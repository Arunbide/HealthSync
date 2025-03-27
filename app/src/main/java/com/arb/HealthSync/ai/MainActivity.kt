package com.arb.HealthSync.ai

import App
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.arb.HealthSync.ui_Layer.Screens.SignUpScreen
import com.example.medicationreminder.ui.theme.HealthCompanionTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HealthCompanionTheme {
                App()
            }
        }
    }
}