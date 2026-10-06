package com.example.myapplication.ViewModel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.dataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.Model.Request.ContactRequest
import com.example.myapplication.Model.Request.LoginRequest
import com.example.myapplication.Model.Request.RegistrationRequest
import com.example.myapplication.Model.Request.VerifyOtpRequest
import com.example.myapplication.Network.RetrofitClient
import com.example.myapplication.Repository.LoginRepository
import com.example.myapplication.Repository.RegistrationRepository
import com.example.myapplication.Utils.ContactState
import com.example.myapplication.Utils.LoginState
import com.example.myapplication.Utils.RegistrationState
import com.example.myapplication.Utils.UserPreferences
import com.example.myapplication.Utils.VerifyOtpState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = LoginRepository(RetrofitClient.pApiInterface)

    private val _loginState =
        MutableStateFlow<LoginState>(LoginState.Idle)

    val loginState: StateFlow<LoginState> =
        _loginState

    private val userPreferences = UserPreferences(application)

    private val _verifyOtpState =
        MutableStateFlow<VerifyOtpState>(
            VerifyOtpState.Idle
        )

    val verifyOtpState: StateFlow<VerifyOtpState> =
        _verifyOtpState.asStateFlow()

    private val _contactState =
        MutableStateFlow<ContactState>(
            ContactState.Idle
        )

    val contactState: StateFlow<ContactState> =
        _contactState.asStateFlow()

    fun submitContact(
        name: String,
        phone: String,
        email: String,
        subject: String,
        message: String
    ) {
        viewModelScope.launch {
            _contactState.value = ContactState.Loading
            val url = "https://www.eruvaaka.com/android_api/public/api/contact"
            val request = ContactRequest(
                name = name,
                phone = phone,
                email = email.ifBlank { null },
                subject = subject,
                message = message
            )

            val result = repository.submitContact(url, request)

            result.onSuccess { response ->
                _contactState.value = ContactState.Success(response)
            }.onFailure { error ->
                _contactState.value = ContactState.Error(
                    error.message ?: "Failed to submit contact message"
                )
            }
        }
    }


    fun verifyOtp(
        phone: String,
        otp: String
    ) {

        viewModelScope.launch {

            _verifyOtpState.value = VerifyOtpState.Loading

            val result = repository.verifyOtp(phone = phone, otp = otp)

            result.onSuccess { response ->

                    // response is VerifyOtpResponse here

                    userPreferences.saveUserData(

                        token = response.token,

                        userId = response.user.id,

                        firstName = response.user.first_name,

                        lastName = response.user.last_name,

                        phone = response.user.phone,

                        email = response.user.email ?: "",

                        state = response.user.state,

                        district = response.user.district,

                        mandal = response.user.mandal,

                        pincode = response.user.pincode,

                        role = response.user.role
                    )

                val token = userPreferences.token.first()

                Log.e("token", token.toString())


                    _verifyOtpState.value = VerifyOtpState.Success(response)
                }

                .onFailure { error ->

                    _verifyOtpState.value =
                        VerifyOtpState.Error(
                            error.message
                                ?: "OTP verification failed"
                        )
                }
        }
    }


    /*fun verifyOtp(
        phone: String,
        otp: String
    ) {

        viewModelScope.launch {

            try {

                _verifyOtpState.value = VerifyOtpState.Loading

                val request = VerifyOtpRequest(
                    phone = phone,
                    otp = otp
                )

                val response = repository.verifyOtp(phone, otp)


                *//*
                 * Save token + user information
                 *//*

                userPreferences.saveUserData(

                    token = response.token,

                    userId = response.user.id,

                    firstName =
                        response.user.first_name,

                    lastName =
                        response.user.last_name,

                    phone =
                        response.user.phone,

                    email =
                        response.user.email,

                    state =
                        response.user.state,

                    district =
                        response.user.district,

                    mandal =
                        response.user.mandal,

                    pincode =
                        response.user.pincode,

                    role =
                        response.user.role
                )


                _verifyOtpState.value =
                    VerifyOtpState.Success(response)

            } catch (e: Exception) {

                _verifyOtpState.value =
                    VerifyOtpState.Error(
                        e.message
                            ?: "OTP verification failed"
                    )
            }
        }
    }*/

    fun loginUser(phone: String) {

        viewModelScope.launch {

            _loginState.value =
                LoginState.Loading

            val request = LoginRequest(
                phone = phone
            )

            val result =
                repository.loginUser(request)

            result
                .onSuccess { response ->

                    _loginState.value =
                        LoginState.Success(
                            message = response.message, mobile = phone,
                            otp = response.otp
                        )
                }

                .onFailure { error ->

                    _loginState.value =
                        LoginState.Error(
                            error.message
                                ?: "Login failed"
                        )
                }
        }
    }
}