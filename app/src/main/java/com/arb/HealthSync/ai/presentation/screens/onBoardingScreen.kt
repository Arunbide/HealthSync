package com.arb.HealthSync.ui_Layer.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.arb.HealthSync.ai.R
import com.arb.HealthSync.ai.presentation.navigation.OnboardingPrefs
import com.arb.HealthSync.ai.presentation.navigation.SignUpScreen
import com.arb.HealthSync.ai.presentation.navigation.mainScreen

@Composable
fun WalkThroughUi(navController: NavHostController) {
    val context = LocalContext.current
    val onboardingPrefs = remember { OnboardingPrefs(context) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.onboarding),
            contentDescription = "Health Tracking Illustration",
            modifier = Modifier
                .size(300.dp)
                .padding(bottom = 20.dp)
        )

        Text(
            text = "Personalize Your\nHealth Journey",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 35.sp,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Text(
            text = "Monitor, Track, and Improve\nYour Wellness with HealthSync",
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 15.dp, bottom = 40.dp)
        )

        Button(
            onClick = {
                onboardingPrefs.setOnboardingCompleted()
                if (onboardingPrefs.isUserSignedIn()) {
                    navController.navigate(mainScreen) {
                        popUpTo(0) // Clear back stack
                    }
                } else {
                    navController.navigate(SignUpScreen)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp, vertical = 10.dp)
                .height(55.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text(
                "Get Started",
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }

        Text(
            "By continuing, you accept our Privacy Policy",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
            modifier = Modifier.padding(top = 20.dp, bottom = 30.dp)
        )
    }
}