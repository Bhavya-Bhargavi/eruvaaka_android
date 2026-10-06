package com.example.myapplication.Utils

import com.example.myapplication.Model.Response.ContactResponse

sealed class ContactState {
    object Idle : ContactState()
    object Loading : ContactState()
    data class Success(val response: ContactResponse) : ContactState()
    data class Error(val message: String) : ContactState()
}
