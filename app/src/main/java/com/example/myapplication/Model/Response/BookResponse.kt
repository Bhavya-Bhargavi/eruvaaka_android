package com.example.myapplication.Model.Response

data class BookResponse(
    val data: List<BookItem>? = emptyList()
)

data class BookItem(
    val id: Int? = 0,
    val title: String? = "",
    val author: String? = "",
    val description: String? = "",
    val cover_image: String? = "",
    val download_url: String? = ""
)
