package com.example.managerlibrary.dto


data class LibrarianDTO(
    var id: String = "",
    var name: String = "",
    var password: String = "",
    var role : String = ""
){
    override fun toString(): String {
        //return id + name
        return "$name - $id"
    }

    override fun hashCode(): Int {
        return id.hashCode() + name.hashCode() + password.hashCode() + role.hashCode()
    }
}
