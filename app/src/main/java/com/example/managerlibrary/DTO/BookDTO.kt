package com.example.managerlibrary.DTO

data class BookDTO (
    val idBook: Int,
    val name: String,
    val rentalFee: Double,
    val category: CategoryBookDTO
)