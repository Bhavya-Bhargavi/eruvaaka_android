package com.example.myapplication.Model.Request

import com.google.gson.annotations.SerializedName

data class UpdateProfileRequest(
    @SerializedName("firstName") val firstName: String,
    @SerializedName("lastName") val lastName: String,
    @SerializedName("state") val state: String,
    @SerializedName("district") val district: String,
    @SerializedName("mandal") val mandal: String,
    @SerializedName("pincode") val pincode: String,
    @SerializedName("crop_interests") val crop_interests: List<String>
)
