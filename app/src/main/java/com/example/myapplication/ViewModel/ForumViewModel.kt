package com.example.myapplication.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.Model.Response.ForumResponse
import com.example.myapplication.Network.RetrofitClient
import com.example.myapplication.Repository.ForumRepository
import com.google.gson.JsonElement
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ForumViewModel : ViewModel() {

    private val repository = ForumRepository(RetrofitClient.pApiInterface)

    private val _forumsState = MutableStateFlow<Result<ForumResponse>?>(null)
    val forumsState: StateFlow<Result<ForumResponse>?> = _forumsState

    private val _forumDetailsState = MutableStateFlow<Result<JsonElement>?>(null)
    val forumDetailsState: StateFlow<Result<JsonElement>?> = _forumDetailsState

    private val _forumCommentsState = MutableStateFlow<Result<JsonElement>?>(null)
    val forumCommentsState: StateFlow<Result<JsonElement>?> = _forumCommentsState

    private val _postCommentState = MutableStateFlow<Result<JsonElement>?>(null)
    val postCommentState: StateFlow<Result<JsonElement>?> = _postCommentState

    fun fetchForums() {
        viewModelScope.launch {
            _forumsState.value = repository.getForums()
        }
    }

    fun fetchForumDetails(forumId: Int) {
        viewModelScope.launch {
            _forumDetailsState.value = repository.getForumDetails(forumId)
        }
    }

    fun fetchForumComments(forumId: Int) {
        viewModelScope.launch {
            _forumCommentsState.value = repository.getForumComments(forumId)
        }
    }

    fun postForumComment(token: String, forumId: Int, body: String) {
        viewModelScope.launch {
            _postCommentState.value = repository.postForumComment(token, forumId, body)
        }
    }
}
