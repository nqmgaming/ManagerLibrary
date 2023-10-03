package com.example.managerlibrary.ui.account

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.managerlibrary.R
import com.example.managerlibrary.dao.LibrarianDAO
import com.example.managerlibrary.databinding.ActivityLoginBinding
import com.example.managerlibrary.databinding.DialogLoginSuccessBinding
import com.example.managerlibrary.sharepre.LoginSharePreference
import com.example.managerlibrary.ui.MainActivity

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

        binding.btnLoginClick.setOnClickListener {
            if (binding.constraintLayout.visibility == android.view.View.VISIBLE) {
                binding.constraintLayout.visibility = android.view.View.GONE
            } else {
                binding.constraintLayout.visibility = android.view.View.VISIBLE
                binding.btnLoginClick.visibility = android.view.View.GONE
            }
        }

        binding.btnLogin.setOnClickListener {
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

            //save full informatio of librarian
            librarianDAO = LibrarianDAO(this)
            val librarian = librarianDAO.getLibrarianByID(username)
            loginSharePreference.saveLogin(librarian)


            if (binding.cbRememberMe.isChecked) {
                loginSharePreference.isRemember(true)
            } else {
                loginSharePreference.isRemember(false)
            }
            val builder = AlertDialog.Builder(this, R.style.CustomDialog)
            val inflater = layoutInflater
            builder.setView(inflater.inflate(R.layout.dialog_proccessing, null))
            builder.setCancelable(false) // if you want the user to wait until the process finishes
            val dialog = builder.create()

            dialog.show()

            Handler().postDelayed({
                dialog.dismiss()
                val builderDialog = AlertDialog.Builder(this, R.style.CustomDialog)
                val bindingDialog = DialogLoginSuccessBinding.inflate(layoutInflater)
                builderDialog.setView(bindingDialog.root)
                val dialogLogin = builderDialog.create()
                bindingDialog.btnLoginSuccess.setOnClickListener {
                    dialogLogin.dismiss()
                    //intent to main activity
                    Intent(this, MainActivity::class.java).also {
                        startActivity(it)
                        finish()
                    }
                }
                dialogLogin.show()
            }, 1500)

        }

    }
}