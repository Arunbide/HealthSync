package com.arb.HealthSync.ai.presentation.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController

import androidx.core.content.edit

class OnboardingPrefs(context: Context) {
    private val sharedPrefs = context.getSharedPreferences("onboarding_prefs", Context.MODE_PRIVATE)

    fun isOnboardingCompleted(): Boolean {
        return sharedPrefs.getBoolean("onboarding_completed", false)
    }

    fun setOnboardingCompleted() {
        sharedPrefs.edit {
            putBoolean("onboarding_completed", true)
        }
    }

    fun isUserSignedIn(): Boolean {
        return sharedPrefs.getBoolean("user_signed_in", false)
    }
    fun clearAll() {
        sharedPrefs.edit() { clear() }
    }


    fun setUserSignedIn(signedIn: Boolean) {
        sharedPrefs.edit {
            putBoolean("user_signed_in", signedIn)
        }
    }
}

@Composable
fun OnboardingHandler(navController: NavHostController) {
    val context = LocalContext.current
    val onboardingPrefs = remember { OnboardingPrefs(context) }

    LaunchedEffect(Unit) {
        when {
            onboardingPrefs.isUserSignedIn() -> {
                navController.navigate(mainScreen) {
                    popUpTo(0)
                }
            }
            onboardingPrefs.isOnboardingCompleted() -> {
                navController.navigate(SignUpScreen) {
                    popUpTo(0)
                }
            }
            else -> {
                navController.navigate(onboardingScreen) {
                    popUpTo(0)
                }
            }
        }
    }
}