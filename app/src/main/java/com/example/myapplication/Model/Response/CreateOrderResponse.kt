package com.example.myapplication.Model.Response

/*data class CreateOrderResponse(
    val message: String,
    val order_id: String,
    val payment_key: String,
    val amount: Int,
    val currency: String,
    val receipt: String
)*/

data class CreateOrderResponse(
    val message: String,
    val order_id: String,
    val payment_key: String,
    val order: RazorpayOrder
)

data class RazorpayOrder(
    val id: String,
    val amount: Int,
    val currency: String,
    val receipt: String,
    val key_id: String
)
