import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.arb.HealthSync.ai.presentation.navigation.ProfileScreen
import com.arb.HealthSync.ai.presentation.navigation.SignUpScreen
import com.arb.HealthSync.ai.presentation.navigation.mainScreen
import com.arb.HealthSync.ai.presentation.navigation.medicationReminderScreen
import com.arb.HealthSync.ai.presentation.navigation.onboardingScreen
import com.arb.HealthSync.ai.presentation.screens.HealthAppHomeScreen
import com.arb.HealthSync.ai.presentation.screens.MedicationReminderScreen
import com.arb.HealthSync.ui_Layer.Screens.ProfileScreenUI
import com.arb.HealthSync.ui_Layer.Screens.SignUpScreen
import com.arb.HealthSync.ui_Layer.Screens.WalkThroughUi


@Composable
fun App() {
    val navController = rememberNavController()
    NavHost(navController=navController, startDestination = onboardingScreen) {

        composable<mainScreen> {
            HealthAppHomeScreen(navController = navController)
        }
        composable<medicationReminderScreen> {
            MedicationReminderScreen(navController = navController)
        }

        composable<SignUpScreen> {
            SignUpScreen(navController = navController)

            }
        composable <ProfileScreen>

            {
                ProfileScreenUI(navController=navController)
            }
 composable <onboardingScreen>

            {
                WalkThroughUi(navController=navController)
            }

    }}