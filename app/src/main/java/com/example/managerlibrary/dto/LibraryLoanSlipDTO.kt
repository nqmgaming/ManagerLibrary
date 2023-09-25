package com.example.managerlibrary.dto

data class LibraryLoanSlipDTO(
    val id: Int,
    val idBook: Int,
    val idLibrarian: String,
    val idMember: Int,
    val dateLoan: String,
    val status: Int
)