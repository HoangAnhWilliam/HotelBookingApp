package com.example.myapplication.ui.login

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Body
import retrofit2.http.POST

// Model request
data class LoginRequest(
    val email: String,
    val phoneNumber: String
)

// Model response
data class LoginResponse(
    val userId: String,
    val userName: String,
    val email: String,
    val phoneNumber: String
)

interface ApiService {
    @POST("Admin/api/login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>
}