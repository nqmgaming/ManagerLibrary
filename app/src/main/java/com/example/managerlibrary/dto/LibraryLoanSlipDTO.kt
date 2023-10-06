package com.example.managerlibrary.dto

data class LibraryLoanSlipDTO(
    val id: String,
    val idBook: Int,
    val idLibrarian: String,
    val idMember: Int,
    val dateLoan: String,
    val status: Int
)