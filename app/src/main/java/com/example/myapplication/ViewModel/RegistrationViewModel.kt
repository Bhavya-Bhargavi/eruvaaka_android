package com.example.myapplication.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.Model.Request.RegistrationRequest
import com.example.myapplication.Model.Response.RegistrationResponse
import com.example.myapplication.Network.RetrofitClient
import com.example.myapplication.Repository.RegistrationRepository
import com.example.myapplication.Utils.RegistrationState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegistrationViewModel : ViewModel() {

    private val repository = RegistrationRepository(RetrofitClient.pApiInterface)

    private val _registrationState = MutableStateFlow<RegistrationState>(RegistrationState.Idle)

    val registrationState: StateFlow<RegistrationState> = _registrationState

    fun registerUser(request: RegistrationRequest) {

        viewModelScope.launch {

            _registrationState.value = RegistrationState.Loading

            val result = repository.registerUser(request)

            result
                .onSuccess { response ->

                    _registrationState.value =
                        RegistrationState.Success(
                            response.message, request.phone
                        )
                }

                .onFailure { error ->

                    _registrationState.value = RegistrationState.Error(
                            error.message
                                ?: "Registration failed"
                        )
                }
        }
    }
}