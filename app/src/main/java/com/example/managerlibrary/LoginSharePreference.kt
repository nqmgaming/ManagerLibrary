package com.example.managerlibrary

import android.content.Context

class LoginSharePreference(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("Login", Context.MODE_PRIVATE)

    fun saveLogin(librarianDTO: LibrarianDTO) {
        val editor = sharedPreferences.edit()
        editor.putString("id", librarianDTO.id)
        editor.putString("password", librarianDTO.password)
        editor.putString("name", librarianDTO.name)
        editor.putString("role", librarianDTO.role)
        editor.apply()
    }

    fun isRemember(login:Boolean){
        val editor = sharedPreferences.edit()
        editor.putBoolean("login",login)
        editor.apply()
    }

    fun getRemember():Boolean{
        return sharedPreferences.getBoolean("login",false)
    }

    fun getID(): String? {
        return sharedPreferences.getString("id", "")
    }

    fun getPassword(): String? {
        return sharedPreferences.getString("password", "")
    }

    fun getName(): String? {
        return sharedPreferences.getString("name", "")
    }

    fun clearLogin() {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

    fun getRole(): String {
        return sharedPreferences.getString("role", "").toString()
    }

}