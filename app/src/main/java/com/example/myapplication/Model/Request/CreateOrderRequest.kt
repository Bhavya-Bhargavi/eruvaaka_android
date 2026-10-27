package com.example.myapplication.Model.Request

//data class CreateOrderRequest(val plan: String)


data class CreateOrderRequest(
    val plan: String,
    val coupon_code: String? = null,
    val shipping_address: String? = null
)

/*data class ShippingAddressRequest(
    val full_name: String,
    val mobile: String,
    val address_line: String,
    val landmark: String,
    val city: String,
    val state: String,
    val pincode: String
)*/
