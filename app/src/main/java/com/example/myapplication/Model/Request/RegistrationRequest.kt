package com.example.myapplication.Model.Request

data class RegistrationRequest(
    val firstName: String,

    val lastName: String,

    val phone: String,

    val email: String?,

    val password: String,

    val state: String,

    val district: String,

    val mandal: String,

    val pincode: String,

    val crop_interests: List<String>?
)