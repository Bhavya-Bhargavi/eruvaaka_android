package com.example.myapplication.Model.Response

data class VerifyOtpResponse(  val message: String,
                               val token: String,
                               val user: User)

data class User(
    val id: Int,
    val first_name: String,
    val last_name: String,
    val phone: String,
    val email: String,
    val state: String,
    val district: String,
    val mandal: String,
    val pincode: String,
    val role: String
)
