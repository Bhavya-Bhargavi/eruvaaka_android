package com.example.myapplication.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.Model.Request.CreateOrderRequest
import com.example.myapplication.Model.Response.BookResponse
import com.example.myapplication.Network.RetrofitClient
import com.example.myapplication.Network.RetrofitClient.pApiInterface
import com.example.myapplication.Repository.BookRepository
import com.google.gson.JsonElement
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BookViewModel : ViewModel() {

    private val repository = BookRepository(RetrofitClient.pApiInterface)

    private val _booksState = MutableStateFlow<Result<BookResponse>?>(null)
    val booksState: StateFlow<Result<BookResponse>?> = _booksState

    private val _bookDetailsState = MutableStateFlow<Result<JsonElement>?>(null)
    val bookDetailsState: StateFlow<Result<JsonElement>?> = _bookDetailsState

    fun fetchBooks() {
        viewModelScope.launch {
            _booksState.value = repository.getBooks()
        }
    }

    fun fetchBookDetails(bookId: Int) {
        viewModelScope.launch {
            _bookDetailsState.value = repository.getBookDetails(bookId)
        }
    }

    /*fun createBookOrder(bookId: Int, token: String) {
        viewModelScope.launch {


            try {
                val request = CreateOrderRequest(
                    book_id = bookId
                )

                // Use your existing create-order API
                val response = pApiInterface.createOrder("Bearer $token",request)

                if (response.isSuccessful) {
                    val result = response.body()

                    // Extract the same order_id, payment_key,
                    // amount and currency used by subscription checkout.

                    // Launch your existing Razorpay checkout.
                } else {
                    // Handle API error
                }

            } catch (e: Exception) {
                // Handle exception
            }
        }
    }*/
}
