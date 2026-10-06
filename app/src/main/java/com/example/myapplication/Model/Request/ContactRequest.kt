package com.example.myapplication.Model.Request

data class ContactRequest(
    val name: String,
    val phone: String,
    val email: String?,
    val subject: String,
    val message: String
)
