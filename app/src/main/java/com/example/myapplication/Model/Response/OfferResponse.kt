package com.example.myapplication.Model.Response

import com.google.gson.annotations.SerializedName

data class OfferResponse(
    @SerializedName("status") val status: Boolean? = true,
    @SerializedName("message") val message: String? = null,
    @SerializedName("data") val data: List<OfferItem>? = emptyList(),
    @SerializedName("offers") val offers: List<OfferItem>? = emptyList()
) {
    fun getOfferList(): List<OfferItem> {
        return data?.ifEmpty { offers } ?: offers ?: emptyList()
    }
}

data class OfferItem(
    @SerializedName("id") val id: Int? = 0,
    @SerializedName("title") val title: String? = null,
    @SerializedName("description") val description: String? = null,
    @SerializedName("coupon_code") val coupon_code: String? = null,
    @SerializedName("discount_percentage") val discount_percentage: Int? = null,
    @SerializedName("valid_till") val valid_till: String? = null,
    @SerializedName("is_active") val is_active: Boolean? = true
)
