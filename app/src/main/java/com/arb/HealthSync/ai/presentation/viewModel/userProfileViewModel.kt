package com.arb.HealthSync.ai.presentation.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arb.HealthSync.ai.data.repo.UserProfileRepository
import com.arb.HealthSync.ai.presentation.navigation.OnboardingPrefs
import com.arb.HealthSync.ui_Layer.Screens.UserProfileData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserProfileViewModel(context: Context) : ViewModel() {

    private val repository = UserProfileRepository(context)

    private val _userProfileData = MutableStateFlow<UserProfileData?>(null)
    val userProfileData: StateFlow<UserProfileData?> = _userProfileData.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()


    init {
        loadUserProfile()
    }

    fun loadUserProfile() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _userProfileData.value = repository.getUserProfile()
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = "Failed to load profile: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun saveUserProfile(
        firstName: String,
        lastName: String,
        email: String,
        age: String,
        phone: String? = null,
        address: String? = null,
        gender: String? = null
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val ageInt = age.toIntOrNull()
                val userProfile = UserProfileData(
                    firstName = firstName,
                    lastName = lastName,
                    email = email,
                    age = ageInt,
                    phone = phone,
                    address = address,
                    gender = gender
                )
                repository.saveUserProfile(userProfile)
                _userProfileData.value = userProfile
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = "Failed to save profile: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateUserProfile(userProfile: UserProfileData) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                repository.saveUserProfile(userProfile)
                _userProfileData.value = userProfile
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = "Failed to update profile: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    fun clearUserProfile(context: Context) {
        viewModelScope.launch {
            _isLoading.value = true
            try {

                repository.clearProfile()

                _userProfileData.value = null
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = "Failed to clear profile: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }}