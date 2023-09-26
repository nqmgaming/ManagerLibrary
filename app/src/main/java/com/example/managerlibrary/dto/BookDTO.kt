package com.example.managerlibrary.dto

data class BookDTO(
    val idBook: Int,
    val name: String,
    val rentalFee: Int,
    val category: Int,
    val timeRental: Int = 0,
){
    override fun toString(): String {
        //return id + tên + phí thuê
        return "$idBook - $name - $rentalFee - $category"
    }
}