package com.example.myapplication.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.Model.Response.OfferResponse
import com.example.myapplication.Model.Response.ProductResponse
import com.example.myapplication.Network.RetrofitClient
import com.example.myapplication.Repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ProductRepository(RetrofitClient.pApiInterface)

    private val _productsState = MutableStateFlow<Result<ProductResponse>?>(null)
    val productsState: StateFlow<Result<ProductResponse>?> = _productsState.asStateFlow()

    private val _offersState = MutableStateFlow<Result<OfferResponse>?>(null)
    val offersState: StateFlow<Result<OfferResponse>?> = _offersState.asStateFlow()

    fun fetchProducts() {
        viewModelScope.launch {
            _productsState.value = repository.getProducts()
        }
    }

    fun fetchActiveOffers() {
        viewModelScope.launch {
            _offersState.value = repository.getActiveOffers()
        }
    }
}
