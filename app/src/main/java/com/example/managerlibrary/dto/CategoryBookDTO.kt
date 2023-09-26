package com.example.managerlibrary.dto

data class CategoryBookDTO(
    val id: Int,
    val name: String
) {
    override fun toString(): String {
        return "$id - $name"
    }
}