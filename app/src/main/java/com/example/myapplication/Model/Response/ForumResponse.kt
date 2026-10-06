package com.example.myapplication.Model.Response

data class ForumResponse(
    val data: List<ForumItem>? = emptyList()
)

data class ForumItem(
    val id: Int? = 0,
    val slug: String? = "",
    val title: String? = "",
    val body: String? = "",
    val is_published: Int? = 0,
    val created_at: String? = "",
    val updated_at: String? = "",
    val comments_count: Int? = 0
)
