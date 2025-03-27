package com.arb.HealthSync.ai.presentation.viewModel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDate

class HealthTrackingViewModel : ViewModel() {
    // Water tracking
    private val _waterIntake = MutableStateFlow(0)
    val waterIntake: StateFlow<Int> = _waterIntake

    private val _waterGoal = MutableStateFlow(8)
    val waterGoal: StateFlow<Int> = _waterGoal

    // Sleep tracking
    private val _sleepDuration = MutableStateFlow(0)  // in minutes
    val sleepDuration: StateFlow<Int> = _sleepDuration

    private val _sleepQuality = MutableStateFlow("Not recorded")
    val sleepQuality: StateFlow<String> = _sleepQuality

    // Steps tracking
    private val _stepCount = MutableStateFlow(0)
    val stepCount: StateFlow<Int> = _stepCount

    private val _stepGoal = MutableStateFlow(5000)
    val stepGoal: StateFlow<Int> = _stepGoal

    // Date for tracking
    private val _currentDate = MutableStateFlow(LocalDate.now())
    val currentDate: StateFlow<LocalDate> = _currentDate

    // Function to add water
    fun addWater(glasses: Int = 1) {
        val current = _waterIntake.value
        _waterIntake.value = minOf(current + glasses, _waterGoal.value)
    }

    // Function to reset water
    fun resetWater() {
        _waterIntake.value = 0
    }

    // Function to set water goal
    fun setWaterGoal(glasses: Int) {
        _waterGoal.value = glasses
    }

    // Function to log sleep
    fun logSleep(hours: Int, minutes: Int, quality: String) {
        _sleepDuration.value = hours * 60 + minutes
        _sleepQuality.value = quality
    }

    // Function to add steps
    fun addSteps(steps: Int) {
        _stepCount.value += steps
    }

    // Function to reset steps
    fun resetSteps() {
        _stepCount.value = 0
    }

    // Function to set step goal
    fun setStepGoal(steps: Int) {
        _stepGoal.value = steps
    }

    // Function to get sleep hours and minutes as formatted string
    fun getSleepFormatted(): String {
        val hours = _sleepDuration.value / 60
        val minutes = _sleepDuration.value % 60
        return "${hours}h ${minutes}m"
    }

    // Sample function to simulate sensor data updates
    fun simulateSensorUpdates() {
        // This would be replaced with actual sensor data in a real app
        addSteps((50..200).random())
    }

    // Load saved data (in a real app, this would come from a database)
    init {
        // Simulated data for preview purposes
        _waterIntake.value = 4
        _sleepDuration.value = 7 * 60 + 30 // 7h 30m
        _sleepQuality.value = "Good"
        _stepCount.value = 2345
    }
}
