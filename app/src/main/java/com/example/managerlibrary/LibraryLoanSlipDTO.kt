package com.example.managerlibrary

data class LibraryLoanSlipDTO(
    val id: Int,
    val idBook: Int,
    val idLibrarian: Int,
    val idMember: Int,
    val dateLoan: String,
    val status: Int
)