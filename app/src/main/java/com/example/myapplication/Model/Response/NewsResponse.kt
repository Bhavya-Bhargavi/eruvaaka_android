package com.example.myapplication.Model.Response

data class NewsResponse(
    val source: String,
    val last_build_date: String,
    val count: Int,
    val items: List<NewsItem>
)

data class NewsItem(
    val title: String,
    val link: String,
    val published_at: String,
    val creator: String,
    val categories: List<String>,
    val description: String,
    val content: String
)
