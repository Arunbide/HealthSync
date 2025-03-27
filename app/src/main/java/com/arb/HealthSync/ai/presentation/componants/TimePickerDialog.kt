package com.arb.HealthSync.ai.presentation.componants


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
@Composable
fun TimePickerDialog(
    onDismiss: () -> Unit,
    onTimeSelected: (String) -> Unit
) {
    var hour by remember { mutableStateOf(8) }
    var minute by remember { mutableStateOf(0) }
    var is24Hour by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Select Time",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Button(
                            onClick = { if (hour < 23) hour++ else hour = 0 },
                            modifier = Modifier.size(48.dp),
                            shape = CircleShape,
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Icon(Icons.Filled.KeyboardArrowUp, contentDescription = "Increase hour")
                        }

                        Text(
                            text = hour.toString().padStart(2, '0'),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )

                        Button(
                            onClick = { if (hour > 0) hour-- else hour = 23 },
                            modifier = Modifier.size(48.dp),
                            shape = CircleShape,
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Icon(Icons.Filled.KeyboardArrowDown, contentDescription = "Decrease hour")
                        }
                    }

                    Text(
                        text = ":",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )

                    Column {
                        Button(
                            onClick = { if (minute < 55) minute += 5 else minute = 0 },
                            modifier = Modifier.size(48.dp),
                            shape = CircleShape,
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Icon(Icons.Filled.KeyboardArrowUp, contentDescription = "Increase minute")
                        }

                        Text(
                            text = minute.toString().padStart(2, '0'),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )

                        Button(
                            onClick = { if (minute > 0) minute -= 5 else minute = 55 },
                            modifier = Modifier.size(48.dp),
                            shape = CircleShape,
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Icon(Icons.Filled.KeyboardArrowDown, contentDescription = "Decrease minute")
                        }
                    }

                    if (!is24Hour) {
                        Spacer(modifier = Modifier.width(16.dp))

                        Column {
                            val isPM = hour >= 12

                            Button(
                                onClick = {
                                    hour = if (isPM) hour - 12 else hour + 12
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (!isPM) Color(0xFF6200EE) else Color.Gray
                                ),
                                modifier = Modifier.width(48.dp)
                            ) {
                                Text("AM")
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Button(
                                onClick = {
                                    hour = if (!isPM) hour + 12 else hour - 12
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isPM) Color(0xFF6200EE) else Color.Gray
                                ),
                                modifier = Modifier.width(48.dp)
                            ) {
                                Text("PM")
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    Text("24-hour format")
                    Spacer(modifier = Modifier.width(8.dp))
                    Switch(
                        checked = is24Hour,
                        onCheckedChange = { is24Hour = it }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {
                            val displayHour = if (!is24Hour) {
                                when {
                                    hour == 0 -> 12 // 12 AM
                                    hour > 12 -> hour - 12 // PM
                                    else -> hour // AM
                                }
                            } else {
                                hour
                            }

                            val amPm = if (!is24Hour) {
                                if (hour < 12) "AM" else "PM"
                            } else {
                                ""
                            }

                            val timeString = if (is24Hour) {
                                "${displayHour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}"
                            } else {
                                "$displayHour:${minute.toString().padStart(2, '0')} $amPm"
                            }

                            onTimeSelected(timeString)
                        }
                    ) {
                        Text("OK")
                    }
                }
            }
        }
    }
}