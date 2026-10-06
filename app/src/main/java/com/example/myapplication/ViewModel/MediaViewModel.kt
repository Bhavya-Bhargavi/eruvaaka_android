package com.example.myapplication.ViewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.Model.Response.NewsResponse
import com.example.myapplication.Network.RetrofitClient
import com.example.myapplication.Repository.MediaRepository
import com.example.myapplication.Utils.MagazineStorage
import com.google.gson.JsonElement
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import java.io.FileOutputStream

class MediaViewModel : ViewModel() {

    private val repository = MediaRepository(RetrofitClient.pApiInterface)

    private val _newsState = MutableStateFlow<Result<NewsResponse>?>(null)
    val newsState: StateFlow<Result<NewsResponse>?> = _newsState

    private val _ePapersState = MutableStateFlow<Result<JsonElement>?>(null)
    val ePapersState: StateFlow<Result<JsonElement>?> = _ePapersState

    private val _eMagazinesState = MutableStateFlow<Result<JsonElement>?>(null)
    val eMagazinesState: StateFlow<Result<JsonElement>?> = _eMagazinesState

    private val _downloadState = MutableStateFlow<Result<ResponseBody>?>(null)
    val downloadState: StateFlow<Result<ResponseBody>?> = _downloadState

    fun fetchNews() {
        viewModelScope.launch {
            _newsState.value = repository.getNews(null)
        }
    }

    fun fetchEPapers() {
        viewModelScope.launch {
            _ePapersState.value = repository.getEPapers()
        }
    }

    fun fetchEMagazines() {
        viewModelScope.launch {
            _eMagazinesState.value = repository.getEMagazines()
        }
    }

    fun downloadEPaper(token: String, publicationId: Int = 1, context: Context) {
        viewModelScope.launch {
            var pubId = publicationId
            if (pubId <= 1) {
                try {
                    val epaperResult = repository.getEPapers()
                    if (epaperResult.isSuccess) {
                        val element = epaperResult.getOrNull()
                        if (element != null && element.isJsonArray) {
                            val arr = element.asJsonArray
                            if (arr.size() > 0) {
                                val firstObj = arr[0].asJsonObject
                                pubId = when {
                                    firstObj.has("id") -> firstObj.get("id").asInt
                                    firstObj.has("publication_id") -> firstObj.get("publication_id").asInt
                                    else -> 1
                                }
                            }
                        } else if (element != null && element.isJsonObject) {
                            val obj = element.asJsonObject
                            val dataArr = obj.getAsJsonArray("data") ?: obj.getAsJsonArray("items")
                            if (dataArr != null && dataArr.size() > 0) {
                                val firstObj = dataArr[0].asJsonObject
                                pubId = when {
                                    firstObj.has("id") -> firstObj.get("id").asInt
                                    firstObj.has("publication_id") -> firstObj.get("publication_id").asInt
                                    else -> 1
                                }
                            }
                        }
                    }
                } catch (e: Exception) {
                    // fallback
                }
            }

            val result = repository.downloadEPaper(token, pubId)
            if (result.isSuccess) {
                try {
                    val file = MagazineStorage(context).getMagazineFile("epaper.pdf")
                    val inputStream = result.getOrNull()?.byteStream()
                    val outputStream = FileOutputStream(file)
                    inputStream?.copyTo(outputStream)
                    inputStream?.close()
                    outputStream.close()
                    _downloadState.value = result // Indicates success
                } catch (e: Exception) {
                    _downloadState.value = Result.failure(e)
                }
            } else {
                _downloadState.value = result
            }
        }
    }

    fun downloadEMagazine(token: String, publicationId: Int = 1, context: Context) {
        viewModelScope.launch {
            var pubId = publicationId
            if (pubId <= 1) {
                try {
                    val emagResult = repository.getEMagazines()
                    if (emagResult.isSuccess) {
                        val element = emagResult.getOrNull()
                        if (element != null && element.isJsonArray) {
                            val arr = element.asJsonArray
                            if (arr.size() > 0) {
                                val firstObj = arr[0].asJsonObject
                                pubId = when {
                                    firstObj.has("id") -> firstObj.get("id").asInt
                                    firstObj.has("publication_id") -> firstObj.get("publication_id").asInt
                                    else -> 1
                                }
                            }
                        } else if (element != null && element.isJsonObject) {
                            val obj = element.asJsonObject
                            val dataArr = obj.getAsJsonArray("data") ?: obj.getAsJsonArray("items")
                            if (dataArr != null && dataArr.size() > 0) {
                                val firstObj = dataArr[0].asJsonObject
                                pubId = when {
                                    firstObj.has("id") -> firstObj.get("id").asInt
                                    firstObj.has("publication_id") -> firstObj.get("publication_id").asInt
                                    else -> 1
                                }
                            }
                        }
                    }
                } catch (e: Exception) {
                    // fallback
                }
            }

            val result = repository.downloadEMagazine(token, pubId)
            if (result.isSuccess) {
                try {
                    val file = MagazineStorage(context).getMagazineFile("eru_vaaka_latest.pdf")
                    val inputStream = result.getOrNull()?.byteStream()
                    val outputStream = FileOutputStream(file)
                    inputStream?.copyTo(outputStream)
                    inputStream?.close()
                    outputStream.close()
                    _downloadState.value = result // Indicates success
                } catch (e: Exception) {
                    _downloadState.value = Result.failure(e)
                }
            } else {
                _downloadState.value = result
            }
        }
    }
}
