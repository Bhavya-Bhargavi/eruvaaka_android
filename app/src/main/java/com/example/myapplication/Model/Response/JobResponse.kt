package com.example.myapplication.Model.Response

import com.google.gson.annotations.SerializedName

data class JobResponse(
    @SerializedName("status") val status: Boolean? = true,
    @SerializedName("message") val message: String? = null,
    @SerializedName("data") val data: List<JobItem>? = emptyList(),
    @SerializedName("jobs") val jobs: List<JobItem>? = emptyList()
) {
    fun getJobList(): List<JobItem> {
        return data?.ifEmpty { jobs } ?: jobs ?: emptyList()
    }
}

data class JobItem(
    @SerializedName("id") val id: Int? = 0,
    @SerializedName("title") val title: String? = null,
    @SerializedName("company") val company: String? = null,
    @SerializedName("location") val location: String? = null,
    @SerializedName("salary") val salary: String? = null,
    @SerializedName("job_type") val job_type: String? = null,
    @SerializedName("description") val description: String? = null,
    @SerializedName("qualifications") val qualifications: String? = null,
    @SerializedName("posted_at") val posted_at: String? = null,
    @SerializedName("apply_link") val apply_link: String? = null
)
