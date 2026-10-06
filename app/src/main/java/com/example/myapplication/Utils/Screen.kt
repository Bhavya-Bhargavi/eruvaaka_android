package com.example.myapplication.Utils

sealed class Screen(val route: String) {

    data object Magazine : Screen("magazine")

    data object Subscription : Screen("subscription")

    data object PdfViewer : Screen("pdf_viewer")
}