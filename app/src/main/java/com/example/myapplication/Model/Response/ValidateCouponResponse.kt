package com.example.myapplication.Model.Response


data class ValidateCouponResponse(
    val valid: Boolean,
    val original_amount: Double,
    val discount: Double,
    val final_amount: Double,
    val currency: String
)