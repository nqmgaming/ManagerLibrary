package com.example.managerlibrary.sharepre

import android.content.Context

class LoginSharePreference(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("Login", Context.MODE_PRIVATE)

    fun saveLogin(id: String, password: String) {
        val editor = sharedPreferences.edit()
        editor.putString("id", id)
        editor.putString("password", password)
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

    fun clearLogin() {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

}