package com.example.myapplication.ui.slideshow

data class Product(
    val id: Int,
    val name: String,
    val slug: String,
    val description: String,
    val price: Double,
    val brandId: Int,
    val categoryId: Int,
    val category: Category,
    val brand: Brand,
    val image: String
)
data class Category(
    val id: Int,
    val name: String,
    val description: String,
    val slug: String,
    val status: Int
)
data class Brand(
    val id: Int,
    val name: String,
    val description: String,
    val slug: String,
    val status: Int
)
