package com.example.managerlibrary.ui.account

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.managerlibrary.dao.LibrarianDAO
import com.example.managerlibrary.dto.LibrarianDTO
import com.example.managerlibrary.sharepre.LoginSharePreference
import com.example.managerlibrary.databinding.ActivityEditUserBinding

class EditAccountActivity : AppCompatActivity() {
    lateinit var userSharePreference: LoginSharePreference
    lateinit var librarianDTO: LibrarianDTO
    lateinit var librarianDAO: LibrarianDAO
    private lateinit var binding: ActivityEditUserBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditUserBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        userSharePreference = LoginSharePreference(this)

        //get information of user by id
        val id = userSharePreference.getID()


        librarianDAO = LibrarianDAO(this)
        librarianDTO = id?.let { librarianDAO.getLibrarianByID(it) }!!

        binding.edtUsernameProfile.setText(librarianDTO.id)
        binding.edtFullnameProfile.setText(librarianDTO.name)

        binding.edtUsernameProfile.setOnClickListener() {
            Toast.makeText(this, "Can't edit username", Toast.LENGTH_SHORT).show()
        }

        binding.btnCancelEditUser.setOnClickListener() {
            finish()
        }

        binding.btnSaveEditUser.setOnClickListener() {
            Log.d("EditProfile", "Starting EditUserActivity")
            val username = binding.edtUsernameProfile.text.toString().trim()
            val fullname = binding.edtFullnameProfile.text.toString().trim()

            if (username.isEmpty()) {
                binding.edtUsernameProfile.error = "Username is empty"
                return@setOnClickListener
            }
            if (fullname.isEmpty()) {
                binding.edtFullnameProfile.error = "Fullname is empty"
                return@setOnClickListener
            }
            librarianDAO = LibrarianDAO(this)
            librarianDTO =
                LibrarianDTO(username, fullname, librarianDTO.password, librarianDTO.role)
            val result = librarianDAO.editLibrarian(librarianDTO)
            if (result > 0) {
                userSharePreference.saveLogin(librarianDTO)
                finish()

            } else {
                Toast.makeText(this, "Edit failed" + result, Toast.LENGTH_SHORT).show()
                binding.edtUsernameProfile.error = "Username is exist"
            }
        }
    }
}
