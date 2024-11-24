package com.example.myapplication.ui.login

data class UserWithRole(
    val userId: String,
    val userName: String,
    val email: String,
    val phone: String,
    val roleName: String
)