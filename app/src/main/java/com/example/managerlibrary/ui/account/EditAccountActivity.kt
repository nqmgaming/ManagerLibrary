package com.example.managerlibrary.ui.account

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
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

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        userSharePreference = LoginSharePreference(this)

        //get information of user by id
        val id = userSharePreference.getID()


        librarianDAO = LibrarianDAO(this)
        librarianDTO = id?.let { librarianDAO.getLibrarianByID(it) }!!

        binding.edtUsernameProfile.setText(librarianDTO.id)
        binding.edtFullnameProfile.setText(librarianDTO.name)

        binding.edtUsernameProfile.setOnClickListener() {
            Toast.makeText(this, "Không thể sửa", Toast.LENGTH_SHORT).show()
        }

        binding.btnCancelEditUser.setOnClickListener() {
            finish()
        }

        binding.btnSaveEditUser.setOnClickListener() {
            val username = binding.edtUsernameProfile.text.toString().trim()
            val fullname = binding.edtFullnameProfile.text.toString().trim()

            if (username.isEmpty()) {
                binding.edtUsernameProfile.error = "Tên người dùng trống"
                return@setOnClickListener
            }
            if (fullname.isEmpty()) {
                binding.edtFullnameProfile.error = "Tên đầy đủ trống"
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
            }
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
