package com.example.managerlibrary.dto

data class CategoryBookDTO(
    val id: String,
    val name: String
) {
    override fun toString(): String {
        return "$id - $name"
    }
}