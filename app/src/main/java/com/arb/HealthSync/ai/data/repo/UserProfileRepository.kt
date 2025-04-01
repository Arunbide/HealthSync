package com.arb.HealthSync.ai.data.repo


import android.content.Context
import android.content.SharedPreferences
import com.arb.HealthSync.ui_Layer.Screens.UserProfileData
import androidx.core.content.edit

class UserProfileRepository(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        "user_profile_prefs", Context.MODE_PRIVATE
    )

    fun getUserProfile(): UserProfileData? {
        val firstName = sharedPreferences.getString(KEY_FIRST_NAME, null)
        val lastName = sharedPreferences.getString(KEY_LAST_NAME, null)
        val email = sharedPreferences.getString(KEY_EMAIL, null)

        if (firstName == null || lastName == null || email == null) {
            return null
        }

        return UserProfileData(
            firstName = firstName,
            lastName = lastName,
            email = email,
            age = sharedPreferences.getInt(KEY_AGE, -1).takeIf { it != -1 },
            phone = sharedPreferences.getString(KEY_PHONE, null),
            address = sharedPreferences.getString(KEY_ADDRESS, null),
            gender = sharedPreferences.getString(KEY_GENDER, null)
        )
    }

    fun saveUserProfile(userProfile: UserProfileData) {
        sharedPreferences.edit().apply {
            putString(KEY_FIRST_NAME, userProfile.firstName)
            putString(KEY_LAST_NAME, userProfile.lastName)
            putString(KEY_EMAIL, userProfile.email)
            if (userProfile.age != null) {
                putInt(KEY_AGE, userProfile.age)
            } else {
                remove(KEY_AGE)
            }
            putString(KEY_PHONE, userProfile.phone)
            putString(KEY_ADDRESS, userProfile.address)
            putString(KEY_GENDER, userProfile.gender)
            apply()
        }
    }

    fun clearProfile() {
        sharedPreferences.edit() { clear() }
    }

    companion object {
        private const val KEY_FIRST_NAME = "first_name"
        private const val KEY_LAST_NAME = "last_name"
        private const val KEY_EMAIL = "email"
        private const val KEY_AGE = "age"
        private const val KEY_PHONE = "phone"
        private const val KEY_ADDRESS = "address"
        private const val KEY_GENDER = "gender"
    }
}