package com.example.myapplication.Utils

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.first

private val Context.dataStore by preferencesDataStore(
    name = "user_preferences"
)

class UserPreferences(private val context: Context
) {

    companion object {

        private val TOKEN =
            stringPreferencesKey("token")

        private val USER_ID =
            stringPreferencesKey("user_id")

        private val FIRST_NAME =
            stringPreferencesKey("first_name")

        private val LAST_NAME =
            stringPreferencesKey("last_name")

        private val PHONE =
            stringPreferencesKey("phone")

        private val EMAIL =
            stringPreferencesKey("email")

        private val STATE =
            stringPreferencesKey("state")

        private val DISTRICT =
            stringPreferencesKey("district")

        private val MANDAL =
            stringPreferencesKey("mandal")

        private val PINCODE =
            stringPreferencesKey("pincode")

        private val ROLE =
            stringPreferencesKey("role")

        val IS_SUBSCRIBED =
            booleanPreferencesKey("is_subscribed")

    }

    val isSubscribed: Flow<Boolean> =
        context.dataStore.data.map { preferences ->
            preferences[IS_SUBSCRIBED] ?: false
        }

    suspend fun setSubscribed(value: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_SUBSCRIBED] = value
        }
    }

    suspend fun saveUserData(
        token: String,
        userId: Int,
        firstName: String,
        lastName: String,
        phone: String,
        email: String,
        state: String,
        district: String,
        mandal: String,
        pincode: String,
        role: String
    ) {
        context.dataStore.edit { preferences ->

            preferences[TOKEN] = token

            preferences[USER_ID] = userId.toString()

            preferences[FIRST_NAME] = firstName

            preferences[LAST_NAME] = lastName

            preferences[PHONE] = phone

            preferences[EMAIL] = email

            preferences[STATE] = state

            preferences[DISTRICT] = district

            preferences[MANDAL] = mandal

            preferences[PINCODE] = pincode

            preferences[ROLE] = role
        }
    }

    val token: Flow<String?> =
        context.dataStore.data.map {
            it[TOKEN]
        }

    val userId: Flow<String?> =
        context.dataStore.data.map {
            it[USER_ID]
        }

    val firstName: Flow<String?> =
        context.dataStore.data.map {
            it[FIRST_NAME]
        }

    val lastName: Flow<String?> =
        context.dataStore.data.map {
            it[LAST_NAME]
        }

    val phone: Flow<String?> =
        context.dataStore.data.map {
            it[PHONE]
        }

    val email: Flow<String?> =
        context.dataStore.data.map {
            it[EMAIL]
        }

    val state: Flow<String?> =
        context.dataStore.data.map {
            it[STATE]
        }

    val district: Flow<String?> =
        context.dataStore.data.map {
            it[DISTRICT]
        }

    val mandal: Flow<String?> =
        context.dataStore.data.map {
            it[MANDAL]
        }

    val pincode: Flow<String?> =
        context.dataStore.data.map {
            it[PINCODE]
        }

    val role: Flow<String?> =
        context.dataStore.data.map {
            it[ROLE]
        }


    suspend fun isLoggedIn(): Boolean {
        val token = context.dataStore.data
            .map { it[TOKEN] }
            .first()

        return !token.isNullOrBlank()
    }

    suspend fun getToken(): String? {
        return context.dataStore.data
            .map { it[TOKEN] }
            .first()
    }

    suspend fun getUserId(): Int? {
        return context.dataStore.data
            .map { it[USER_ID] }
            .first()
            ?.toIntOrNull()
    }

    suspend fun getPhone(): Int? {
        return context.dataStore.data
            .map { it[PHONE] }
            .first()
            ?.toIntOrNull()
    }

    suspend fun clearUserData() {

        context.dataStore.edit {
            it.clear()
        }
    }
}






