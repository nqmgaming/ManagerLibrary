package com.example.managerlibrary.ui.account

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.managerlibrary.R
import com.example.managerlibrary.dao.LibrarianDAO
import com.example.managerlibrary.databinding.ActivityLoginBinding
import com.example.managerlibrary.databinding.DialogLoginSuccessBinding
import com.example.managerlibrary.dto.LibrarianDTO
import com.example.managerlibrary.sharepre.LoginSharePreference
import com.example.managerlibrary.ui.MainActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private var username: String = ""
    private var password: String = ""

    private lateinit var librarianDAO: LibrarianDAO
    private lateinit var loginSharePreference: LoginSharePreference

    //firebase storage
    val firestore = Firebase.firestore
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


            if (password.isEmpty()) {
                binding.edtPassword.error = "Nhập mật khẩu"
                return@setOnClickListener
            }
            if (binding.cbRememberMe.isChecked) {
                loginSharePreference.isRemember(true)
            } else {
                loginSharePreference.isRemember(false)
            }

            firestore.collection("users")
                .get()
                .addOnSuccessListener {
                    var foundUser = false
                    for (document in it) {
                        if (document.id == (username)) {
                            foundUser = true
                            if (document.data["password"].toString() == password) {
                                //save full information of librarian
                                val librarianDTO = LibrarianDTO()
                                librarianDTO.id = document.id
                                librarianDTO.name = document.data["name"].toString()
                                librarianDTO.password = document.data["password"].toString()
                                librarianDTO.role = document.data["role"].toString()

                                //save information of librarian
                                val userSharePreference = LoginSharePreference(this)
                                userSharePreference.saveLogin(librarianDTO)
                                //intent to main activity
                                val builder = AlertDialog.Builder(this, R.style.CustomDialog)
                                val inflater = layoutInflater
                                builder.setView(inflater.inflate(R.layout.dialog_proccessing, null))
                                builder.setCancelable(false) // if you want the user to wait until the process finishes
                                val dialog = builder.create()

                                dialog.show()

                                Handler().postDelayed({
                                    dialog.dismiss()
                                    val builderDialog =
                                        AlertDialog.Builder(this, R.style.CustomDialog)
                                    val bindingDialog =
                                        DialogLoginSuccessBinding.inflate(layoutInflater)
                                    builderDialog.setView(bindingDialog.root)
                                    val dialogLogin = builderDialog.create()
                                    bindingDialog.btnLoginSuccess.setOnClickListener {
                                        dialogLogin.dismiss()
                                        Intent(this, MainActivity::class.java).also {
                                            startActivity(it)
                                            finish()
                                        }
                                    }
                                    dialogLogin.show()
                                }, 1500)
                            } else {
                                binding.edtPassword.error = "Mật khẩu không đúng"
                                Toast.makeText(this,document.data["password"].toString() + document.data["id"].toString(),Toast.LENGTH_SHORT).show()
                                return@addOnSuccessListener
                            }
                        }
                    }
                    if (!foundUser) {
                        binding.edtUsername.error = "Username không đúng"
                        return@addOnSuccessListener
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Lỗi kết nối", Toast.LENGTH_SHORT).show()
                }
        }

    }
}