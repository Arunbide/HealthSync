package com.arb.HealthSync.ai.presentation.screens
import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.arb.HealthSync.ai.presentation.navigation.ProfileScreen
import com.arb.HealthSync.ai.presentation.navigation.medicationReminderScreen
import com.arb.HealthSync.ai.presentation.viewModel.ChatViewModel
import com.arb.HealthSync.ai.presentation.viewModel.HealthTrackingViewModel
import kotlinx.coroutines.launch
import java.time.LocalTime


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HealthAppHomeScreen(
    viewModel: HealthTrackingViewModel = viewModel(),
    navController: NavHostController,
    chatViewModel: ChatViewModel = viewModel()
) {
    var selectedTab by remember { mutableStateOf(0) }
    val scrollState = rememberScrollState()
    var userInput by remember { mutableStateOf("") }
    var chatMessages by remember { mutableStateOf(listOf<Pair<String, Boolean>>()) }
    var isTyping by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val chatScrollState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    BackHandler {
        (context as? Activity)?.finish()
    }

    LaunchedEffect(chatMessages.size, isTyping) {
        if (chatMessages.isNotEmpty() || isTyping) {
            coroutineScope.launch {
                chatScrollState.animateScrollToItem(chatMessages.size + if (isTyping) 1 else 0)
            }
        }
    }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                val items = listOf(
                    Triple("Home", Icons.Filled.Home, 0),
                    Triple("Medications", Icons.Filled.Medication, 1),
                    Triple("Advisor", Icons.Filled.MarkUnreadChatAlt, 2),
                    Triple("Profile", Icons.Filled.Person, 3)
                )

                items.forEach { (title, icon, index) ->
                    NavigationBarItem(
                        icon = {
                            when (icon) {
                                else -> Icon(icon, contentDescription = title)
                            }
                        },
                        label = { Text(title, fontSize = 12.sp) },
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }
        },

    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.background,
                            MaterialTheme.colorScheme.background.copy(alpha = 0.95f)
                        )
                    )
                )
        ) {
            when (selectedTab) {
                0 -> { // Home tab
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .verticalScroll(scrollState),
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        GreetingSection()
                        FunctionalHealthMetricsSection()
                        MedicationTrackerSection()
                        EmergencyCallButton()
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
                1 -> {
                    navController.navigate(medicationReminderScreen)
                }
                2 -> {
                    ChatContent(
                        userInput = userInput,
                        onUserInputChange = { userInput = it },
                        chatMessages = chatMessages,
                        isTyping = isTyping,
                        scrollState = chatScrollState,
                        sendUserMessage = {
                            if (userInput.isNotBlank()) {
                                sendMessage(userInput, chatViewModel, chatMessages, isTyping) { messages, typing ->
                                    chatMessages = messages
                                    isTyping = typing
                                }
                                userInput = ""
                                focusManager.clearFocus()
                            }
                        }
                    )
                }
                3 -> {
                    navController.navigate(ProfileScreen)
                }
            }
        }
    }
}
@Composable
fun GreetingSection() {
    var selectedMood by remember { mutableStateOf(-1) }
    val currentHour = LocalTime.now().hour
    val greeting = when {
        currentHour in 5..11 -> "Good morning"
        currentHour in 12..17 -> "Good afternoon"
        else -> "Good evening"
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "$greeting",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "How are you feeling today?",
                style = MaterialTheme.typography.bodyLarge
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                val moods = listOf(
                    Triple("😊", "Happy", Color(0xFF4CAF50)),
                    Triple("😐", "Neutral", Color(0xFFFFC107)),
                    Triple("😞", "Sad", Color(0xFF2196F3)),
                    Triple("😴", "Tired", Color(0xFF9C27B0)),
                    Triple("😷", "Sick", Color(0xFFF44336))
                )

                moods.forEachIndexed { index, (emoji, text, color) ->
                    MoodItem(
                        emoji = emoji,
                        text = text,
                        color = color,
                        selected = selectedMood == index,
                        onClick = { selectedMood = index }
                    )
                }
            }
        }
    }
}

@Composable
fun MoodItem(
    emoji: String,
    text: String,
    color: Color,
    selected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .shadow(
                    elevation = if (selected) 8.dp else 2.dp,
                    shape = CircleShape,
                    spotColor = if (selected) color else Color.Gray
                )
                .background(
                    if (selected) color else Color.White,
                    CircleShape
                )
                .padding(4.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = emoji,
                fontSize = 24.sp
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = text,
            fontSize = 12.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
            color = if (selected) color else MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun HealthMetricsSection(
    waterIntake: Int,
    waterGoal: Int,
    sleepFormatted: String,
    sleepQuality: String,
    stepCount: Int,
    stepGoal: Int,
    onAddWater: () -> Unit,
    onLogSleep: () -> Unit,
    onAddSteps: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Today's Health",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                // Add a refresh button to simulate sensor updates
                IconButton(onClick = onAddSteps) {
                    Icon(
                        imageVector = Icons.Filled.Refresh,
                        contentDescription = "Refresh Data"
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Water intake
                MetricCard(
                    icon = Icons.Outlined.WaterDrop,
                    title = "Water",
                    value = "$waterIntake/$waterGoal",
                    unit = "glasses",
                    progress = waterIntake.toFloat() / waterGoal,
                    color = Color(0xFF03A9F4),
                    modifier = Modifier.weight(1f),
                    onClick = onAddWater
                )

                Spacer(modifier = Modifier.width(12.dp))

                // Sleep quality
                MetricCard(
                    icon = Icons.Outlined.Bedtime,
                    title = "Sleep",
                    value = sleepFormatted,
                    unit = sleepQuality,
                    progress = if (sleepFormatted.contains("0h 0m")) 0f else 0.75f,
                    color = Color(0xFF9C27B0),
                    modifier = Modifier.weight(1f),
                    onClick = onLogSleep
                )

                Spacer(modifier = Modifier.width(12.dp))

                // Steps
                MetricCard(
                    icon = Icons.Outlined.DirectionsWalk,
                    title = "Steps",
                    value = stepCount.toString(),
                    unit = "of $stepGoal",
                    progress = stepCount.toFloat() / stepGoal,
                    color = Color(0xFF4CAF50),
                    modifier = Modifier.weight(1f),
                    onClick = onAddSteps
                )
            }
        }
    }
}

@Composable
fun MetricCard(
    icon: ImageVector,
    title: String,
    value: String,
    unit: String,
    progress: Float,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(1000, easing = LinearEasing),
        label = "progress"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .clickable { onClick() }
            .padding(12.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            CircularProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier.size(60.dp),
                color = color,
                trackColor = color.copy(alpha = 0.2f),
                strokeCap = StrokeCap.Round,
                strokeWidth = 6.dp
            )

            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = color,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )

        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = unit,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WaterIntakeDialog(
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit
) {
    var waterAmount by remember { mutableStateOf(1) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Add Water Intake")
        },
        text = {
            Column {
                Text("How many glasses of water did you drink?")
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { if (waterAmount > 1) waterAmount-- }
                    ) {
                        Icon(Icons.Filled.Close, contentDescription = "Decrease")
                    }

                    Text(
                        text = waterAmount.toString(),
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    IconButton(
                        onClick = { waterAmount++ }
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = "Increase")
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(waterAmount) }
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text("Cancel")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SleepLogDialog(
    onDismiss: () -> Unit,
    onConfirm: (Int, Int, String) -> Unit
) {
    var hours by remember { mutableStateOf(7) }
    var minutes by remember { mutableStateOf(30) }
    var selectedQuality by remember { mutableStateOf("Good") }

    val qualityOptions = listOf("Poor", "Fair", "Good", "Excellent")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Log Sleep")
        },
        text = {
            Column {
                Text("How long did you sleep?")
                Spacer(modifier = Modifier.height(16.dp))

                // Hours and minutes selection
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Hours
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Hours", style = MaterialTheme.typography.labelMedium)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = { if (hours > 0) hours-- }
                            ) {
                                Icon(Icons.Filled.Remove, contentDescription = "Decrease Hours")
                            }

                            Text(
                                text = hours.toString(),
                                style = MaterialTheme.typography.titleLarge
                            )

                            IconButton(
                                onClick = { if (hours < 12) hours++ }
                            ) {
                                Icon(Icons.Filled.Add, contentDescription = "Increase Hours")
                            }
                        }
                    }

                    // Minutes
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Minutes", style = MaterialTheme.typography.labelMedium)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = {
                                    minutes = when {
                                        minutes >= 15 -> minutes - 15
                                        else -> 0
                                    }
                                }
                            ) {
                                Icon(Icons.Filled.Remove, contentDescription = "Decrease Minutes")
                            }

                            Text(
                                text = minutes.toString(),
                                style = MaterialTheme.typography.titleLarge
                            )

                            IconButton(
                                onClick = {
                                    minutes = when {
                                        minutes < 45 -> minutes + 15
                                        else -> 0
                                    }
                                }
                            ) {
                                Icon(Icons.Filled.Add, contentDescription = "Increase Minutes")
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Sleep quality selection
                Text("How was your sleep quality?")
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    qualityOptions.forEach { quality ->
                        FilterChip(
                            selected = selectedQuality == quality,
                            onClick = { selectedQuality = quality },
                            label = { Text(quality) }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(hours, minutes, selectedQuality) }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun FunctionalHealthMetricsSection() {
    val viewModel: HealthTrackingViewModel = viewModel()

    val waterIntake by viewModel.waterIntake.collectAsState()
    val waterGoal by viewModel.waterGoal.collectAsState()
    val sleepFormatted = viewModel.getSleepFormatted()
    val sleepQuality by viewModel.sleepQuality.collectAsState()
    val stepCount by viewModel.stepCount.collectAsState()
    val stepGoal by viewModel.stepGoal.collectAsState()

    var showWaterDialog by remember { mutableStateOf(false) }
    var showSleepDialog by remember { mutableStateOf(false) }

    if (showWaterDialog) {
        WaterIntakeDialog(
            onDismiss = { showWaterDialog = false },
            onConfirm = { amount ->
                viewModel.addWater(amount)
                showWaterDialog = false
            }
        )
    }

    if (showSleepDialog) {
        SleepLogDialog(
            onDismiss = { showSleepDialog = false },
            onConfirm = { hours, minutes, quality ->
                viewModel.logSleep(hours, minutes, quality)
                showSleepDialog = false
            }
        )
    }

    HealthMetricsSection(
        waterIntake = waterIntake,
        waterGoal = waterGoal,
        sleepFormatted = sleepFormatted,
        sleepQuality = sleepQuality,
        stepCount = stepCount,
        stepGoal = stepGoal,
        onAddWater = { showWaterDialog = true },
        onLogSleep = { showSleepDialog = true },
        onAddSteps = { viewModel.simulateSensorUpdates() }
    )
}


@Composable
fun MedicationTrackerSection(
    viewModel: MedicationViewModel = viewModel()
) {
    val medications by viewModel.medications.collectAsState()

    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Today's Medications",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )

                IconButton(onClick = { /* Navigate to full medication screen */ }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                        contentDescription = "View all medications",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // Display medications or empty state
            if (medications.isEmpty()) {
                NoMedicationsMessage()
            } else {
                medications.take(3).forEach { medication ->
                    CompactMedicationItem(
                        medication = medication,
                        onTakeClick = { viewModel.toggleMedicationTaken(it) }
                    )
                }

                // Show more medications indicator if more than 3
                if (medications.size > 3) {
                    Text(
                        text = "+${medications.size - 3} more medications",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                            .alpha(0.7f)
                    )
                }
            }
        }
    }
}

@Composable
fun NoMedicationsMessage() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.Medication,
            contentDescription = "No Medications",
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "No medications scheduled",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
        )
    }
}

@Composable
fun CompactMedicationItem(
    medication: MedicationItem,
    onTakeClick: (MedicationItem) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Medication Details
        Column {
            Text(
                text = medication.name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = if (medication.isTaken) FontWeight.Normal else FontWeight.Bold
            )
            Text(
                text = "${medication.dosage} • ${medication.time}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Take Medication Button
        IconButton(
            onClick = { onTakeClick(medication) },
            modifier = Modifier
                .size(40.dp)
                .background(
                    color = if (medication.isTaken)
                        MaterialTheme.colorScheme.primaryContainer
                    else
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                    shape = CircleShape
                )
        ) {
            Icon(
                imageVector = if (medication.isTaken)
                    Icons.Filled.Check
                else
                    Icons.Filled.Add,
                contentDescription = "Take Medication",
                tint = if (medication.isTaken)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.primary
            )
        }
    }
}



@Composable
fun EmergencyCallButton() {
    Button(
        onClick = { /* Call emergency */ },
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(28.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFE53935)
        ),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Filled.LocalHospital, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "EMERGENCY CALL",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}