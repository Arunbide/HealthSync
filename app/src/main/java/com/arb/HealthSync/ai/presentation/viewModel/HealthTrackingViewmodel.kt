package com.arb.HealthSync.ai.presentation.viewModel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDate

class HealthTrackingViewModel : ViewModel() {
    private val _waterIntake = MutableStateFlow(0)
    val waterIntake: StateFlow<Int> = _waterIntake

    private val _waterGoal = MutableStateFlow(8)
    val waterGoal: StateFlow<Int> = _waterGoal

    private val _sleepDuration = MutableStateFlow(0)  // in minutes
    val sleepDuration: StateFlow<Int> = _sleepDuration

    private val _sleepQuality = MutableStateFlow("Not recorded")
    val sleepQuality: StateFlow<String> = _sleepQuality

    private val _stepCount = MutableStateFlow(0)
    val stepCount: StateFlow<Int> = _stepCount

    private val _stepGoal = MutableStateFlow(5000)
    val stepGoal: StateFlow<Int> = _stepGoal

    private val _currentDate = MutableStateFlow(LocalDate.now())
    val currentDate: StateFlow<LocalDate> = _currentDate

    fun addWater(glasses: Int = 1) {
        val current = _waterIntake.value
        _waterIntake.value = minOf(current + glasses, _waterGoal.value)
    }

    fun resetWater() {
        _waterIntake.value = 0
    }

    fun setWaterGoal(glasses: Int) {
        _waterGoal.value = glasses
    }

    fun logSleep(hours: Int, minutes: Int, quality: String) {
        _sleepDuration.value = hours * 60 + minutes
        _sleepQuality.value = quality
    }

    fun addSteps(steps: Int) {
        _stepCount.value += steps
    }

    fun resetSteps() {
        _stepCount.value = 0
    }

    fun setStepGoal(steps: Int) {
        _stepGoal.value = steps
    }

    fun getSleepFormatted(): String {
        val hours = _sleepDuration.value / 60
        val minutes = _sleepDuration.value % 60
        return "${hours}h ${minutes}m"
    }

    fun simulateSensorUpdates() {
        addSteps((50..200).random())
    }

    init {
        _waterIntake.value = 4
        _sleepDuration.value = 7 * 60 + 30 // 7h 30m
        _sleepQuality.value = "Good"
        _stepCount.value = 2345
    }
}
