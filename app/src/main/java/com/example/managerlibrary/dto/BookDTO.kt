package com.example.managerlibrary.dto

data class BookDTO (
    val idBook: Int,
    val name: String,
    val rentalFee: Double,
    val category: CategoryBookDTO
)