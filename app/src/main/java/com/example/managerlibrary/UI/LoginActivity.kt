package com.example.managerlibrary.UI

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.managerlibrary.DAO.LibrarianDAO
import com.example.managerlibrary.DTO.LibrarianDTO
import com.example.managerlibrary.SharePre.LoginSharePreference
import com.example.managerlibrary.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private var username: String = ""
    private var password: String = ""

    private lateinit var librarianDAO: LibrarianDAO
    private lateinit var loginSharePreference: LoginSharePreference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loginSharePreference = LoginSharePreference(this)
        if (loginSharePreference.getRemember()) {
            Intent(this, MainActivity::class.java).also {
                startActivity(it)
                finish()
            }
        }

        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.btnLoginClick.setOnClickListener() {
            if (binding.constraintLayout.visibility == android.view.View.VISIBLE) {
                binding.constraintLayout.visibility = android.view.View.GONE
            } else {
                binding.constraintLayout.visibility = android.view.View.VISIBLE
                binding.btnLoginClick.visibility = android.view.View.GONE
            }
        }

        binding.btnLogin.setOnClickListener() {
            username = binding.edtUsername.text.toString().trim()
            password = binding.edtPassword.text.toString().trim()
            if (username.isEmpty()) {
                binding.edtUsername.error = "Nhập username"
                return@setOnClickListener
            }
            librarianDAO = LibrarianDAO(this)
            var result = librarianDAO.getLibrarianUsernameByID(username)
            if (result == -1) {
                binding.edtUsername.error = "Username sai"
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                binding.edtPassword.error = "Nhập mật khẩu"
                return@setOnClickListener
            }
            result = librarianDAO.checkPasswordLibrarian(username, password)
            if (result == -1) {
                binding.edtPassword.error = "Mật khẩu sai"
                return@setOnClickListener
            }

            if (binding.cbRememberMe.isChecked) {
                loginSharePreference.saveLogin(username, password)
                loginSharePreference.isRemember(true)
            } else {
                loginSharePreference.isRemember(false)
            }
            //intent to main activity
            Intent(this, MainActivity::class.java).also {
                startActivity(it)
                finish()
            }
        }

    }
}