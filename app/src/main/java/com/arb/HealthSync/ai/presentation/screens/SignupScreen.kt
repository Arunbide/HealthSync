package com.arb.HealthSync.ui_Layer.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.arb.HealthSync.ai.presentation.navigation.mainScreen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(navController: NavHostController) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F9FF)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopAppBar(
            title = { Text("Sign Up") },
            navigationIcon = {
                IconButton(onClick = { }) {
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Back")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFF5F9FF))
        )

        Spacer(modifier = Modifier.height(20.dp))

        // First Name Input
        TextField(
            value = firstName,
            onValueChange = { firstName = it },
            placeholder = { Text("First Name", fontSize = 14.sp) },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 30.dp),
            shape = RoundedCornerShape(7.dp),
        )

        Spacer(modifier = Modifier.height(15.dp))

        // Last Name Input
        TextField(
            value = lastName,
            onValueChange = { lastName = it },
            placeholder = { Text("Last Name", fontSize = 14.sp) },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 30.dp),
            shape = RoundedCornerShape(7.dp),
        )

        Spacer(modifier = Modifier.height(15.dp))

        // Email Input
        TextField(
            value = email,
            onValueChange = { email = it },
            placeholder = { Text("Email", fontSize = 14.sp) },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 30.dp),
            shape = RoundedCornerShape(7.dp),
        )

        Spacer(modifier = Modifier.height(15.dp))

        // Age Input
        TextField(
            value = age,
            onValueChange = { age = it.filter { it.isDigit() } },
            placeholder = { Text("Age", fontSize = 14.sp) },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 30.dp),
            shape = RoundedCornerShape(7.dp),
        )

        Spacer(modifier = Modifier.height(45.dp))

        // Save Button
        Button(
            onClick = {

               navController.navigate(mainScreen)
            },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 33.dp).height(55.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2D9CDB))
        ) {
            Text("Save", fontSize = 16.sp, color = Color.White)
        }
    }
}
