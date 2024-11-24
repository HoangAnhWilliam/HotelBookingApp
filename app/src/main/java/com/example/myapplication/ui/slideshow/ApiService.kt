package com.example.myapplication.ui.slideshow
import retrofit2.Call
import retrofit2.http.GET
interface ApiService {
    @GET("Admin/api/products")
    fun getProducts(): Call<List<Product>>

    @GET("Admin/api/brands")
    fun getBrands(): Call<List<Brand>>
}