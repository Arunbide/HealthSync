package com.arb.HealthSync.ai.data


data class UserProfileData(
    val firstName: String,
    val lastName: String,
    val email: String,
    val age: Int? = null,
    val phone: String? = null,
    val address: String? = null,
    val gender: String? = null
)