package com.arb.HealthSync.ui_Layer.Screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.arb.HealthSync.ai.presentation.navigation.OnboardingPrefs
import com.arb.HealthSync.ai.presentation.navigation.mainScreen
import com.arb.HealthSync.ai.presentation.viewModel.UserProfileViewModel
data class UserProfileData(
    val firstName: String,
    val lastName: String,
    val email: String,
    val age: Int?,
    val phone: String? = null,
    val address: String? = null,
    val gender: String? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    navController: NavHostController,
    userProfileViewModel: UserProfileViewModel = viewModel()
) {
    val userProfile by userProfileViewModel.userProfileData.collectAsState()
    val context = LocalContext.current
    val onboardingPrefs = remember { OnboardingPrefs(context) }

    var firstName by remember { mutableStateOf(userProfile?.firstName ?: "") }
    var lastName by remember { mutableStateOf(userProfile?.lastName ?: "") }
    var email by remember { mutableStateOf(userProfile?.email ?: "") }
    var age by remember { mutableStateOf(userProfile?.age?.toString() ?: "") }
    var phone by remember { mutableStateOf(userProfile?.phone ?: "") }
    var address by remember { mutableStateOf(userProfile?.address ?: "") }
    var gender by remember { mutableStateOf(userProfile?.gender ?: "") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val firstNameError = remember { derivedStateOf { firstName.isBlank() } }
    val lastNameError = remember { derivedStateOf { lastName.isBlank() } }
    val emailError = remember { derivedStateOf { !email.matches(Regex("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}\$")) } }
    val ageError = remember { derivedStateOf { age.isBlank() || age.toIntOrNull()?.let { it <= 0 } ?: true } }
    val passwordError = remember { derivedStateOf { password.length < 8 } }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Create Account",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.align(Alignment.Start)
        )

        Text(
            text = "Join HealthSync to start your wellness journey",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(bottom = 24.dp)
        )

        Text(
            text = "Personal Information",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text("First Name") },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.small,
            isError = firstNameError.value,
            supportingText = {
                if (firstNameError.value) {
                    Text("Required field")
                }
            },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text("Last Name") },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.small,
            isError = lastNameError.value,
            supportingText = {
                if (lastNameError.value) {
                    Text("Required field")
                }
            },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Account Information",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.small,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            isError = emailError.value,
            supportingText = {
                if (emailError.value) {
                    Text("Invalid email format")
                }
            },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.small,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        if (passwordVisible) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff,
                        contentDescription = if (passwordVisible) "Hide password" else "Show password"
                    )
                }
            },
            isError = passwordError.value,
            supportingText = {
                if (passwordError.value) {
                    Text("Minimum 8 characters required")
                }
            },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Health Information Section
        Text(
            text = "Health Information",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = age,
            onValueChange = { age = it.filter { c -> c.isDigit() }.take(3) },
            label = { Text("Age") },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.small,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = ageError.value,
            supportingText = {
                if (ageError.value) {
                    Text("Please enter valid age")
                }
            },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = gender,
            onValueChange = { gender = it },
            label = { Text("Gender (Optional)") },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.small,
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it.filter { c -> c.isDigit() }.take(15) },
            label = { Text("Phone (Optional)") },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.small,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Address (Optional)
        OutlinedTextField(
            value = address,
            onValueChange = { address = it },
            label = { Text("Address (Optional)") },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.small,
            singleLine = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (!firstNameError.value && !lastNameError.value &&
                    !emailError.value && !ageError.value && !passwordError.value) {
                    userProfileViewModel.saveUserProfile(
                        firstName = firstName,
                        lastName = lastName,
                        email = email,
                        age = age,
                        phone = if (phone.isNotEmpty()) phone else null,
                        address = if (address.isNotEmpty()) address else null,
                        gender = if (gender.isNotEmpty()) gender else null
                    )
                    onboardingPrefs.setUserSignedIn(true)
                    navController.navigate(mainScreen) {
                        popUpTo(0)
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = MaterialTheme.shapes.medium,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text("Create Account", style = MaterialTheme.typography.labelLarge)
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}