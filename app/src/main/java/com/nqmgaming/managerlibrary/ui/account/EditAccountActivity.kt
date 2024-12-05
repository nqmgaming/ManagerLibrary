package com.nqmgaming.managerlibrary.ui.account

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.nqmgaming.managerlibrary.R
import com.nqmgaming.managerlibrary.dao.LibrarianDAO
import com.nqmgaming.managerlibrary.databinding.ActivityEditUserBinding
import com.nqmgaming.managerlibrary.dto.LibrarianDTO
import com.nqmgaming.managerlibrary.sharepre.LoginSharePreference

class EditAccountActivity : AppCompatActivity() {

    //shared preference
    private lateinit var userSharePreference: LoginSharePreference

    //database
    private lateinit var librarianDTO: LibrarianDTO
    private lateinit var librarianDAO: LibrarianDAO

    //binding
    private lateinit var binding: ActivityEditUserBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //set binding
        binding = ActivityEditUserBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //set toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        //get information of user by id
        userSharePreference = LoginSharePreference(this)
        val id = userSharePreference.getID()

        //set information of user
        librarianDAO = LibrarianDAO(this)
        librarianDTO = id?.let { librarianDAO.getLibrarianByID(it) }!!

        binding.edtUsernameProfile.setText(librarianDTO.id)
        binding.edtFullnameProfile.setText(librarianDTO.name)

        binding.edtUsernameProfile.setOnClickListener {
            Toast.makeText(this, getString(R.string.txt_can_not_edit), Toast.LENGTH_SHORT).show()
        }

        binding.btnCancelEditUser.setOnClickListener {
            finish()
        }

        binding.btnSaveEditUser.setOnClickListener {
            val username = binding.edtUsernameProfile.text.toString().trim()
            val fullname = binding.edtFullnameProfile.text.toString().trim()

            if (username.isEmpty()) {
                binding.edtUsernameProfile.error = getString(R.string.txt_username_is_empty)
                return@setOnClickListener
            }
            if (fullname.isEmpty()) {
                binding.edtFullnameProfile.error = getString(R.string.txt_fullname_is_empty)
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
                Toast.makeText(this, getString(R.string.txt_edit_failed), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    //set event for toolbar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}
