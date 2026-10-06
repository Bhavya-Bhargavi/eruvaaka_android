package com.example.myapplication.ViewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.Model.Request.UpdateProfileRequest
import com.example.myapplication.Model.Request.UserProfile
import com.example.myapplication.Network.RetrofitClient
import com.example.myapplication.Repository.ProfileRepository
import com.example.myapplication.Utils.UpdateProfileState
import com.example.myapplication.Utils.UserPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine

import kotlinx.coroutines.launch

class ProfileViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val userPreferences = UserPreferences(application)
    private val profileRepository = ProfileRepository(RetrofitClient.pApiInterface)

    private val _profile = MutableStateFlow(UserProfile())
    val profile: StateFlow<UserProfile> = _profile.asStateFlow()

    init {
        loadUserProfile()
    }

    private val _updateProfileState =
        MutableStateFlow<UpdateProfileState>(UpdateProfileState.Idle)

    val updateProfileState = _updateProfileState.asStateFlow()

    fun updateProfile(request: UpdateProfileRequest) {
        viewModelScope.launch {
            _updateProfileState.value = UpdateProfileState.Loading

            try {
                val token = userPreferences.getToken()

                if (token.isNullOrBlank()) {
                    _updateProfileState.value =
                        UpdateProfileState.Error("User is not logged in")
                    return@launch
                }

                val result = profileRepository.updateProfile(
                    token,
                    request
                )

                result.onSuccess {
                    userPreferences.saveUserData(
                        token = token,
                        userId = userPreferences.getUserId() ?: 0,
                        firstName = request.firstName,
                        lastName = request.lastName,
                        phone = _profile.value.phone,
                        email = _profile.value.email,
                        state = request.state,
                        district = request.district,
                        mandal = request.mandal,
                        pincode = request.pincode,
                        role = _profile.value.role
                    )

                    _updateProfileState.value = UpdateProfileState.Success(it)
                    getProfileFromApi()
                }

                result.onFailure {
                    _updateProfileState.value =
                        UpdateProfileState.Error(
                            it.message ?: "Profile update failed"
                        )
                }

            } catch (e: Exception) {
                _updateProfileState.value =
                    UpdateProfileState.Error(
                        e.message ?: "Something went wrong"
                    )
            }
        }
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            combine(
                userPreferences.firstName,
                userPreferences.lastName,
                userPreferences.phone,
                userPreferences.email,
                userPreferences.state,
                userPreferences.district,
                userPreferences.mandal,
                userPreferences.pincode,
                userPreferences.role
            ) { values ->
                UserProfile(
                    first_name = values[0] ?: "",
                    last_name = values[1] ?: "",
                    phone = values[2] ?: "",
                    email = values[3] ?: "",
                    state = values[4] ?: "",
                    district = values[5] ?: "",
                    mandal = values[6] ?: "",
                    pincode = values[7] ?: "",
                    role = values[8] ?: ""
                )
            }.collect {
                _profile.value = it
            }
        }
    }

    fun getProfileFromApi() {
        viewModelScope.launch {
            try {
                val token = userPreferences.getToken()

                if (token.isNullOrEmpty()) {
                    return@launch
                }

                val response = profileRepository.getUserProfile(token)

                response.onSuccess { profileData ->
                    _profile.value = profileData
                }.onFailure { exception ->
                    Log.e("ProfileViewModel", "Get profile failed", exception)
                }

            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Profile API error", e)
            }
        }
    }
}
