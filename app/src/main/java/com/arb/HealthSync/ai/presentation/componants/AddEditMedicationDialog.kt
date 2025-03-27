package com.arb.HealthSync.ai.presentation.componants

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.arb.HealthSync.ai.presentation.screens.MedicationItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditMedicationDialog(
    medication: com.arb.HealthSync.ai.presentation.screens.MedicationItem? = null,
    onDismiss: () -> Unit,
    onSave: (com.arb.HealthSync.ai.presentation.screens.MedicationItem) -> Unit
) {
    var name by remember { mutableStateOf(medication?.name ?: "") }
    var dosage by remember { mutableStateOf(medication?.dosage ?: "") }
    var time by remember { mutableStateOf(medication?.time ?: "") }
    var frequency by remember { mutableStateOf(medication?.frequency ?: "") }
    var notes by remember { mutableStateOf(medication?.notes ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (medication == null) "Add Medication" else "Edit Medication",
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Medication Name") },
                    singleLine = true
                )
                TextField(
                    value = dosage,
                    onValueChange = { dosage = it },
                    label = { Text("Dosage") },
                    singleLine = true
                )
                TextField(
                    value = time,
                    onValueChange = { time = it },
                    label = { Text("Time") },
                    singleLine = true
                )
                TextField(
                    value = frequency,
                    onValueChange = { frequency = it },
                    label = { Text("Frequency") },
                    singleLine = true
                )
                TextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notes") },
                    minLines = 2
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val newMedication = MedicationItem(
                        id = medication?.id ?: 0,
                        name = name,
                        dosage = dosage,
                        time = time,
                        frequency = frequency,
                        icon = medication?.icon ?: Icons.Rounded.Close,
                        isTaken = medication?.isTaken ?: false,
                        notes = notes
                    )
                    onSave(newMedication)
                },
                enabled = name.isNotBlank() && dosage.isNotBlank() && time.isNotBlank()
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}