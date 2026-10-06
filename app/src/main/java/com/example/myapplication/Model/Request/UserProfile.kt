package com.example.myapplication.Model.Request


data class UserProfileResponse(
    val user: UserProfile
)

data class UpdateProfileResponse(val message: String, val user: UserProfile)

data class UserProfile(
    val id: Int = 0,
    val first_name: String = "",
    val last_name: String = "",
    val phone: String = "",
    val email: String = "",
    val state: String = "",
    val district: String = "",
    val mandal: String = "",
    val pincode: String = "",
    val role: String = "",
    val crop_interests: List<String> = emptyList()
)

/*data class UserProfile(val firstName: String = "",
                       val lastName: String = "",
                       val phone: String = "",
                       val email: String = "",
                       val state: String = "",
                       val district: String = "",
                       val mandal: String = "",
                       val pincode: String = "",
                       val role: String = ""
)*/
