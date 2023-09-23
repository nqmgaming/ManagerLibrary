package com.example.managerlibrary

data class BookDTO(
    val idBook: Int,
    val name: String,
    val rentalFee: Int,
    val category: String,
    val timeRental: Int = 0,
)