package com.arb.HealthSync.ai.presentation.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.arb.HealthSync.ai.presentation.componants.AddEditMedicationDialog
import com.arb.HealthSync.ai.presentation.componants.MedicationSummaryCard
import com.arb.HealthSync.ai.presentation.uiState.EmptyMedicationState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MedicationViewModel : ViewModel() {
    private val _medications = MutableStateFlow<List<MedicationItem>>(emptyList())
    val medications: StateFlow<List<MedicationItem>> = _medications.asStateFlow()

    init {
        _medications.value = listOf(
            MedicationItem(
                id = 1,
                name = "Paracetamol",
                dosage = "500mg",
                time = "8:00 AM",
                frequency = "Daily",
                icon = Icons.Rounded.Close,
                isTaken = false
            ),
            MedicationItem(
                id = 2,
                name = "Ibuprofen",
                dosage = "200mg",
                time = "2:00 PM",
                frequency = "Every 8 hours",
                icon = Icons.Rounded.LocationOn,
                isTaken = false
            )
        )
    }

    fun addMedication(medication: MedicationItem) {
        viewModelScope.launch {
            val currentList = _medications.value.toMutableList()
            medication.id = (currentList.maxOfOrNull { it.id } ?: 0) + 1
            currentList.add(medication)
            _medications.value = currentList
        }
    }

    fun updateMedication(medication: MedicationItem) {
        viewModelScope.launch {
            _medications.update { currentList ->
                currentList.map {
                    if (it.id == medication.id) medication else it
                }
            }
        }
    }

    fun deleteMedication(medication: MedicationItem) {
        viewModelScope.launch {
            _medications.update { it.filter { med -> med.id != medication.id } }
        }
    }

    fun toggleMedicationTaken(medication: MedicationItem) {
        viewModelScope.launch {
            _medications.update { currentList ->
                currentList.map {
                    if (it.id == medication.id) it.copy(isTaken = !it.isTaken) else it
                }
            }
        }
    }
}

data class MedicationItem(
    var id: Int = 0,
    val name: String,
    val dosage: String,
    val time: String,
    val frequency: String,
    val icon: Any,
    var isTaken: Boolean = false,
    val notes: String = ""
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicationReminderScreen(
    navController: NavController,
    viewModel: MedicationViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    var showDialog by remember { mutableStateOf(false) }
    var selectedMedication by remember { mutableStateOf<MedicationItem?>(null) }

    val medications by viewModel.medications.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Medication Tracker",
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Filled.Settings, contentDescription = "Settings")
                    }
                    IconButton(onClick = {  }) {
                        Icon(Icons.Filled.Notifications, contentDescription = "Notifications")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    selectedMedication = null
                    showDialog = true
                },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add Medication")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            MedicationSummaryCard(medications)

            Text(
                text = "Today's Medications",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            if (medications.isEmpty()) {
                EmptyMedicationState()
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    items(medications) { medication ->
                        MedicationListItem(
                            medication = medication,
                            onDeleteClick = { viewModel.deleteMedication(it) },
                            onEditClick = {
                                selectedMedication = it
                                showDialog = true
                            },
                            onTakeClick = { viewModel.toggleMedicationTaken(it) }
                        )
                    }
                }
            }
        }

        if (showDialog) {
            AddEditMedicationDialog(
                medication = selectedMedication,
                onDismiss = { showDialog = false },
                onSave = { medicationItem ->
                    if (selectedMedication == null) {
                        viewModel.addMedication(medicationItem)
                    } else {
                        viewModel.updateMedication(medicationItem)
                    }
                    showDialog = false
                }
            )
        }
    }
}

@Composable
fun MedicationListItem(
    medication: MedicationItem,
    onDeleteClick: (MedicationItem) -> Unit,
    onEditClick: (MedicationItem) -> Unit,
    onTakeClick: (MedicationItem) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .animateContentSize(),
        colors = CardDefaults.cardColors(
            containerColor = if (medication.isTaken)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = medication.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${medication.dosage} • ${medication.time}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = medication.frequency,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Row {
                IconButton(
                    onClick = { onTakeClick(medication) },
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.small)
                        .background(
                            if (medication.isTaken)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.primaryContainer
                        )
                ) {
                    Icon(
                        imageVector = if (medication.isTaken)
                            Icons.Filled.Check
                        else
                            Icons.Filled.Add,
                        contentDescription = "Take Medication",
                        tint = if (medication.isTaken)
                            MaterialTheme.colorScheme.onPrimary
                        else
                            MaterialTheme.colorScheme.primary
                    )
                }

                var expanded by remember { mutableStateOf(false) }
                Box {
                    IconButton(onClick = { expanded = true }) {
                        Icon(
                            Icons.Filled.MoreVert,
                            contentDescription = "More Options"
                        )
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Edit") },
                            onClick = {
                                onEditClick(medication)
                                expanded = false
                            },
                            leadingIcon = {
                                Icon(
                                    Icons.Filled.Edit,
                                    contentDescription = "Edit Medication"
                                )
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Delete") },
                            onClick = {
                                onDeleteClick(medication)
                                expanded = false
                            },
                            leadingIcon = {
                                Icon(
                                    Icons.Filled.Delete,
                                    contentDescription = "Delete Medication"
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}