package com.example.managerlibrary.ui.manager

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.managerlibrary.R
import com.example.managerlibrary.dao.CategoryBookDAO
import com.example.managerlibrary.databinding.ActivityEditCategoryBookBinding
import com.example.managerlibrary.dto.CategoryBookDTO
import com.example.managerlibrary.ui.MainActivity

class EditCategoryBookActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditCategoryBookBinding
    private lateinit var categoryBookDAO: CategoryBookDAO
    private lateinit var categoryBookDTO: CategoryBookDTO
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditCategoryBookBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setSupportActionBar(binding.toolbarEditCategoryBook)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        //get intent
        val intent = intent
        val idCategoryBook = intent.getStringExtra("idCategory")
        if (idCategoryBook != null) {
            categoryBookDAO = CategoryBookDAO(this)
            categoryBookDTO = categoryBookDAO.getCategoryBookById(idCategoryBook.toInt())

            //set text for edit text
            binding.edtIdCategory.setText(categoryBookDTO.id.toString())
            binding.edtNameCategory.setText(categoryBookDTO.name)
        }
        binding.edtIdCategory.setOnClickListener {
            Toast.makeText(this, "Không thể thay đổi mã loại sách", Toast.LENGTH_SHORT).show()
        }

        binding.btnCancelEditCategoryBook.setOnClickListener {
            finish()
        }

        binding.btnSaveEditCategoryBook.setOnClickListener {
            val idCategory = binding.edtIdCategory.text.toString()
            val nameCategory = binding.edtNameCategory.text.toString()
            if (nameCategory.isEmpty()) {
                binding.edtNameCategory.error = "Tên loại sách không được để trống"
            } else {
                val categoryBookDTO = CategoryBookDTO(idCategory.toInt(), nameCategory)
                val result = categoryBookDAO.updateCategoryBook(categoryBookDTO)
                if (result > 0) {
                   val builderSuccess = AlertDialog.Builder(this, R.style.CustomDialog)
                    val bindingSuccess =
                        com.example.managerlibrary.databinding.DialogLoginSuccessBinding.inflate(
                            layoutInflater
                        )
                    builderSuccess.setView(bindingSuccess.root)
                    val dialogSuccess = builderSuccess.create()
                    dialogSuccess.show()
                    bindingSuccess.txtLoginSuccess.text = "Sửa loại sách thành công"
                    bindingSuccess.btnLoginSuccess.setOnClickListener {
                        dialogSuccess.dismiss()
                        val intent = Intent(this, MainActivity::class.java)
                        intent.putExtra("ok", "category")
                        startActivity(intent)
                        finish()
                    }
                }
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