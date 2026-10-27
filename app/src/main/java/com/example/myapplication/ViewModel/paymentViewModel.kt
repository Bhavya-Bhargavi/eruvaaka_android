
package com.example.myapplication.ViewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.Model.Request.CreateOrderRequest
import com.example.myapplication.Model.Request.ValidateCouponRequest
import com.example.myapplication.Model.Request.VerifyPaymentRequest
import com.example.myapplication.Model.Response.CreateOrderResponse
import com.example.myapplication.Model.Response.ValidateCouponResponse
import com.example.myapplication.Model.Response.VerifyPaymentResponse
import com.example.myapplication.Network.RetrofitClient.pApiInterface
import com.example.myapplication.Utils.UserPreferences
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class paymentViewModel(application: Application) : AndroidViewModel(application) {

    private val userPreferences = UserPreferences(application)

    private val _orderResponse =
        MutableStateFlow<CreateOrderResponse?>(null)
    val orderResponse: StateFlow<CreateOrderResponse?> = _orderResponse

    private val _paymentVerification =
        MutableStateFlow<VerifyPaymentResponse?>(null)
    val paymentVerification: StateFlow<VerifyPaymentResponse?> =
        _paymentVerification

    private val _couponResponse =
        MutableStateFlow<ValidateCouponResponse?>(null)
    val couponResponse: StateFlow<ValidateCouponResponse?> =
        _couponResponse

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun clearCoupon() {
        _couponResponse.value = null
        _errorMessage.value = null
    }

    fun clearOrderResponse() {
        _orderResponse.value = null
    }

    fun clearPaymentVerification() {
        _paymentVerification.value = null
    }

    fun clearError() {
        _errorMessage.value = null
    }

    fun validateCoupon(couponCode: String, planId: String) {
        Log.e("validateCoupon", couponCode)
        Log.e("validateCoupon", planId)
        if (_isLoading.value) return

        viewModelScope.launch {
            _isLoading.value = true
            _couponResponse.value = null
            _errorMessage.value = null

            try {
                val code = couponCode.trim()
                require(code.isNotEmpty()) {
                    "Please enter a coupon code"
                }

                val token = userPreferences.getToken()
                    ?.takeIf { it.isNotBlank() }
                    ?: error("Session expired. Please login again.")

                val result = pApiInterface.validateCoupon(
                    "Bearer $token",
                    ValidateCouponRequest(
                        coupon_code = code,
                        plan = planId
                    )
                )

                if (result.valid) {
                    _couponResponse.value = result
                } else {
                    _errorMessage.value = "Invalid or expired coupon code"
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                Log.e(
                    "PaymentViewModel",
                    "Coupon validation failed",
                    exception
                )
                _errorMessage.value =
                    exception.message ?: "Coupon validation failed"
            } finally {
                _isLoading.value = false
            }
        }
    }


    fun createOrder(
        planId: String,
        couponCode: String = "",
        shippingAddress: String? = null,
        isPrintMagazine: Boolean = false
    ) {
        if (_isLoading.value) return

        viewModelScope.launch {
            _isLoading.value = true
            _orderResponse.value = null
            _errorMessage.value = null

            try {
                val token = userPreferences.getToken()
                    ?.takeIf { it.isNotBlank() }
                    ?: error("Session expired. Please login again.")

                if (isPrintMagazine) {
                    require(!shippingAddress.isNullOrBlank()) {
                        "Shipping address is required"
                    }
                }

                val code = couponCode.trim()

                if (code.isNotEmpty()) {
                    val couponResult = pApiInterface.validateCoupon(
                        "Bearer $token",
                        ValidateCouponRequest(
                            coupon_code = code,
                            plan = planId
                        )
                    )

                    require(couponResult.valid) {
                        "Invalid or expired coupon code"
                    }

                    _couponResponse.value = couponResult
                } else {
                    _couponResponse.value = null
                }

                val response = pApiInterface.createOrder(
                    "Bearer $token",
                    CreateOrderRequest(
                        plan = planId,
                        coupon_code = code.takeIf { it.isNotEmpty() },
                        shipping_address = if (isPrintMagazine) {
                            shippingAddress?.trim()
                        } else {
                            null
                        }
                    )
                )

                _orderResponse.value = response

            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                Log.e(
                    "PaymentViewModel",
                    "Order creation failed",
                    exception
                )

                _errorMessage.value =
                    exception.message ?: "Unable to create order"
            } finally {
                _isLoading.value = false
            }
        }
    }


    fun verifyPayment(
        razorpayOrderId: String,
        razorpayPaymentId: String,
        razorpaySignature: String
    ) {
        viewModelScope.launch {
            _errorMessage.value = null

            try {
                val response = pApiInterface.verifyPayment(
                    VerifyPaymentRequest(
                        order_id = razorpayOrderId,
                        payment_id = razorpayPaymentId,
                        signature = razorpaySignature
                    )
                )

                _paymentVerification.value = response
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                Log.e(
                    "PaymentViewModel",
                    "Payment verification failed",
                    exception
                )
                _errorMessage.value =
                    exception.message ?: "Payment verification failed"
            }
        }
    }
}
