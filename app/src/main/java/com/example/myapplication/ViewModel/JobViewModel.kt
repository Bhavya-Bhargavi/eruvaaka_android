package com.example.myapplication.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.Model.Response.JobItem
import com.example.myapplication.Model.Response.JobResponse
import com.example.myapplication.Network.RetrofitClient
import com.example.myapplication.Repository.JobRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class JobViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = JobRepository(RetrofitClient.pApiInterface)

    private val _jobsState = MutableStateFlow<Result<JobResponse>?>(null)
    val jobsState: StateFlow<Result<JobResponse>?> = _jobsState.asStateFlow()

    private val _selectedJob = MutableStateFlow<JobItem?>(null)
    val selectedJob: StateFlow<JobItem?> = _selectedJob.asStateFlow()

    fun fetchJobs() {
        viewModelScope.launch {
            _jobsState.value = repository.getJobs()
        }
    }

    fun selectJob(job: JobItem) {
        _selectedJob.value = job
    }

    fun clearSelectedJob() {
        _selectedJob.value = null
    }
}
