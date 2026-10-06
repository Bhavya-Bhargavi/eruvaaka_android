package com.example.myapplication.Model.Request

data class VerifyPaymentRequest(val order_id: String, val payment_id:String, val signature: String)
