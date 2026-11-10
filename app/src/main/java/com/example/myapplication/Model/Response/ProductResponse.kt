package com.example.myapplication.Model.Response

import com.google.gson.annotations.SerializedName

data class ProductResponse(
    @SerializedName("status") val status: Boolean? = true,
    @SerializedName("message") val message: String? = null,
    @SerializedName("data") val data: List<ProductItem>? = emptyList(),
    @SerializedName("items") val items: List<ProductItem>? = emptyList()
) {
    fun getProductList(): List<ProductItem> {
        return data?.ifEmpty { items } ?: items ?: emptyList()
    }
}

data class ProductItem(
    @SerializedName("id") val id: Int? = 0,
    @SerializedName("title") val title: String? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("description") val description: String? = null,
    @SerializedName("price") val price: String? = null,
    @SerializedName("image") val image: String? = null,
    @SerializedName("image_url") val image_url: String? = null,
    @SerializedName("category") val category: String? = null,
    @SerializedName("link") val link: String? = null
) {
    fun getDisplayName(): String {
        return title?.takeIf { it.isNotBlank() } ?: name?.takeIf { it.isNotBlank() } ?: "Agricultural Product"
    }

    fun getDisplayImage(): String? {
        return image_url?.takeIf { it.isNotBlank() } ?: image?.takeIf { it.isNotBlank() }
    }
}
