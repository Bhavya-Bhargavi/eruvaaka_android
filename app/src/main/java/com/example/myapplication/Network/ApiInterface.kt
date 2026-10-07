package com.example.myapplication.Network

import com.example.myapplication.Model.Request.CommentRequest
import com.example.myapplication.Model.Request.ContactRequest
import com.example.myapplication.Model.Request.CreateOrderRequest
import com.example.myapplication.Model.Request.LoginRequest
import com.example.myapplication.Model.Request.RegistrationRequest
import com.example.myapplication.Model.Request.UpdateProfileRequest
import com.example.myapplication.Model.Request.UpdateProfileResponse
import com.example.myapplication.Model.Request.UserProfileResponse
import com.example.myapplication.Model.Request.VerifyOtpRequest
import com.example.myapplication.Model.Response.VerifyOtpResponse
import com.example.myapplication.Model.Request.VerifyPaymentRequest
import com.example.myapplication.Model.Response.BookResponse
import com.example.myapplication.Model.Response.ContactResponse
import com.example.myapplication.Model.Response.CreateOrderResponse
import com.example.myapplication.Model.Response.ForumResponse
import com.example.myapplication.Model.Response.LoginResponse
import com.example.myapplication.Model.Response.NewsResponse
import com.example.myapplication.Model.Response.RegistrationResponse
import com.example.myapplication.Model.Response.VerifyPaymentResponse
import com.google.gson.JsonElement
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiInterface {

    @POST("api/auth/register")
    suspend fun registerUser(@Header("Authorization") contentType: String,
        @Body request: RegistrationRequest
    ): Response<RegistrationResponse>

    @POST("api/auth/login")
    suspend fun loginUser(
        @Body request: LoginRequest): Response<LoginResponse>

    @Headers("Accept: application/json")
    @GET("api/users/profile")
    suspend fun getUserProfile(@Header("Authorization") contentType: String): UserProfileResponse

    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST("api/users/updateProfile")
    suspend fun updateProfilePost(
        @Header("Authorization") token: String,
        @Body request: UpdateProfileRequest
    ): Response<UpdateProfileResponse>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST("api/users/profile")
    suspend fun updateProfileUserProfilePost(
        @Header("Authorization") token: String,
        @Body request: UpdateProfileRequest
    ): Response<UpdateProfileResponse>

    @POST("api/create-order")
    suspend fun createOrder(@Header("Authorization") contentType: String,
        @Body request: CreateOrderRequest
    ): CreateOrderResponse

    @POST("api/verify-payment")
    suspend fun verifyPayment(
        @Body verifyPaymentRequest: VerifyPaymentRequest
    ): VerifyPaymentResponse

    @POST("api/auth/verify-otp")
    suspend fun verifyOtp(
        @Body request: VerifyOtpRequest): Response<VerifyOtpResponse>

    @POST("api/payments/order")
    suspend fun createPaymentOrder(@Header("Authorization") contentType: String,
        @Body request: CreateOrderRequest
    ): CreateOrderResponse

    @POST("api/payments/verify")
    suspend fun verifyPaymentEndpoint(@Header("Authorization") contentType: String,
        @Body request: VerifyPaymentRequest
    ): VerifyPaymentResponse

    @GET("api/subscription-status/{userId}")
    suspend fun getSubscriptionStatus(@Header("Authorization") token: String,
        @Path("userId") userId: Int
    ): Response<JsonElement>

    @POST("api/auth/resend-otp")
    suspend fun resendOtp(@Body request: LoginRequest): Response<JsonElement>

    @POST("api/auth/logout")
    suspend fun logout(@Header("Authorization") token: String): Response<JsonElement>

    @POST("api/contact")
    suspend fun submitContactPost(@Body request: ContactRequest): Response<ContactResponse>

    @POST
    suspend fun submitContact(
        @Url url: String,
        @Body request: ContactRequest
    ): Response<ContactResponse>



    @GET("api/news")
    suspend fun getNews(
        @Query("limit") limit: Int? = null
    ): Response<NewsResponse>

    @GET("api/epapers")
    suspend fun getEPapers(): Response<JsonElement>

    @GET
    suspend fun downloadFileUrl(
        @Header("Authorization") token: String? = null,
        @Url url: String
    ): Response<ResponseBody>

    @GET("api/epapers/{publicationid}/download")
    suspend fun downloadEPaperApi(
        @Header("Authorization") token: String,
        @Path("publicationid") publicationId: Int
    ): Response<ResponseBody>

    @GET("/android_api/epapers/{publicationid}/download")
    suspend fun downloadEPaper(
        @Header("Authorization") token: String,
        @Path("publicationid") publicationId: Int
    ): Response<ResponseBody>

    @GET("api/emagazines")
    suspend fun getEMagazines(): Response<JsonElement>

    @GET("api/emagazines/{publicationid}/download")
    suspend fun downloadEMagazineApi(
        @Header("Authorization") token: String,
        @Path("publicationid") publicationId: Int
    ): Response<ResponseBody>

    @GET("/android_api/emagazines/{publicationid}/download")
    suspend fun downloadEMagazine(
        @Header("Authorization") token: String,
        @Path("publicationid") publicationId: Int
    ): Response<ResponseBody>

    @GET("api/books")
    suspend fun getBooks(): Response<BookResponse>

    @GET("api/books/{bookId}")
    suspend fun getBookDetails(@Path("bookId") bookId: Int): Response<JsonElement>

    @GET("api/forums")
    suspend fun getForums(): Response<ForumResponse>

    @GET("api/forums/{forumId}")
    suspend fun getForumDetails(@Path("forumId") forumId: Int): Response<JsonElement>

    @GET("api/forums/{forumId}/comments")
    suspend fun getForumComments(@Path("forumId") forumId: Int): Response<JsonElement>

    @POST("api/forums/{forumId}/comments")
    suspend fun postForumComment(
        @Header("Authorization") token: String,
        @Path("forumId") forumId: Int,
        @Body request: CommentRequest
    ): Response<JsonElement>
}
