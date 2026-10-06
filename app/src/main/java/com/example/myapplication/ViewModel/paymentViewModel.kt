package com.example.myapplication.ViewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.Model.Request.CreateOrderRequest
import com.example.myapplication.Model.Request.VerifyPaymentRequest
import com.example.myapplication.Model.Response.CreateOrderResponse
import com.example.myapplication.Model.Response.VerifyPaymentResponse
import com.example.myapplication.Network.RetrofitClient
import com.example.myapplication.Network.RetrofitClient.pApiInterface
import com.example.myapplication.Utils.UserPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.String

class paymentViewModel(application: Application) : AndroidViewModel(application){

    private val _orderResponse = MutableStateFlow<CreateOrderResponse?>(null)
    val orderResponse: StateFlow<CreateOrderResponse?> = _orderResponse
    private val _paymentVerification = MutableStateFlow<VerifyPaymentResponse?>(null)
    val paymentVerification: StateFlow<VerifyPaymentResponse?> = _paymentVerification

    private val userPreferences = UserPreferences(application)

    fun createOrder(planId: String) {
        viewModelScope.launch {
            val token = userPreferences.getToken() ?: ""
            Log.e("token", token)
            try {
                val response = pApiInterface.createOrder("Bearer $token"
                    ,CreateOrderRequest(plan = planId)
                )

                _orderResponse.value = response
            } catch (exception: Exception) {
                Log.e("PaymentViewModel", "Order creation failed", exception)
            }
        }
    }

    fun verifyPayment(
        razorpayOrderId: String,
        razorpayPaymentId: String,
        razorpaySignature: String
    ) {
        viewModelScope.launch {
            try {
                val response = pApiInterface.verifyPayment(
                    VerifyPaymentRequest(
                        order_id = razorpayOrderId,
                payment_id = razorpayPaymentId,
                signature = razorpaySignature
                    )
                )

                _paymentVerification.value = response
            } catch (exception: Exception) {
                Log.e(
                    "PaymentViewModel",
                    "Payment verification failed",
                    exception
                )
            }
        }
    }
}
