package com.example.myapplication.Model.Request

data class ValidateCouponRequest(
    val coupon_code: String,
    val plan: String
)