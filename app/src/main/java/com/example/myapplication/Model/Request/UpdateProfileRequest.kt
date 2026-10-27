package com.example.myapplication.Model.Request

import com.google.gson.annotations.SerializedName

data class UpdateProfileRequest(
    @SerializedName("firstName") val firstName: String,
    @SerializedName("lastName") val lastName: String,
    @SerializedName("phone") val phone: String? = null,
    @SerializedName("email") val email: String? = null,
    @SerializedName("address") val address: String? = null,
    @SerializedName("address1") val address1: String? = null,
    @SerializedName("address2") val address2: String? = null,
    @SerializedName("postalAddress") val postalAddress: String? = null,
    @SerializedName("state") val state: String,
    @SerializedName("district") val district: String,
    @SerializedName("mandal") val mandal: String,
    @SerializedName("pincode") val pincode: String,
    @SerializedName("crop_interests") val crop_interests: List<String>
)
