package com.example.myapplication.Model.Request

import com.google.gson.annotations.SerializedName

data class RegistrationRequest(
    @SerializedName("firstName") val firstName: String,
    @SerializedName("lastName") val lastName: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("password") val password: String,
    @SerializedName("state") val state: String,
    @SerializedName("district") val district: String,
    @SerializedName("mandal") val mandal: String,
    @SerializedName("pincode") val pincode: String,
    @SerializedName("email") val email: String? = null,
    @SerializedName("crop_interests") val crop_interests: List<String> = emptyList()
)
