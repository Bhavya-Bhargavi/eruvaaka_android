package com.example.myapplication.Utils

import com.example.myapplication.Model.Request.UpdateProfileResponse
import com.example.myapplication.Model.Request.UserProfile

sealed class UpdateProfileState {


    data object Idle : UpdateProfileState()

    data object Loading : UpdateProfileState()

    data class Success(
         val profile: UpdateProfileResponse
    ) : UpdateProfileState()

    data class Error(
        val message: String
    ) : UpdateProfileState()
}