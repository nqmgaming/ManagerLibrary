package com.nqmgaming.managerlibrary.dto

data class MemberDTO (
    val id: Int,
    val name: String,
    val birthYear: String
){
    override fun toString(): String {
        //return id + tên
        return "$id - $name - $birthYear"
    }
}