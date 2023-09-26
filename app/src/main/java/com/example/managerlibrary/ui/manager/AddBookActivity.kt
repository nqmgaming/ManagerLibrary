package com.example.managerlibrary.ui.manager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import com.example.managerlibrary.R
import com.example.managerlibrary.adapter.CategoryAddBookAdapter
import com.example.managerlibrary.adapter.CategoryBooksAdapter
import com.example.managerlibrary.dao.BookDAO
import com.example.managerlibrary.dao.CategoryBookDAO
import com.example.managerlibrary.databinding.ActivityAddBookBinding
import com.example.managerlibrary.dto.BookDTO
import com.example.managerlibrary.dto.CategoryBookDTO
import com.example.managerlibrary.ui.MainActivity

class AddBookActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddBookBinding
    lateinit var categoryBookDAO: CategoryBookDAO
    lateinit var categoryBookDTO: CategoryBookDTO
    lateinit var bookDAO: BookDAO
    lateinit var bookDTO: BookDTO
    lateinit var listCategoryBook: ArrayList<CategoryBookDTO>
    lateinit var categoryLoanAdapter: CategoryAddBookAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBookBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setSupportActionBar(binding.toolbarAddBook)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        //get all category book
        categoryBookDAO = CategoryBookDAO(this)
        listCategoryBook = categoryBookDAO.getAllCategoryBooks()


        //set adapter for spinner
        categoryLoanAdapter = CategoryAddBookAdapter(this, listCategoryBook)
        binding.spinnerAddBookCategory.adapter = categoryLoanAdapter

        //set event for spinner
        binding.spinnerAddBookCategory.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    positionCategory: Int,
                    id: Long
                ) {
                    categoryBookDTO = listCategoryBook[positionCategory]
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }

        binding.btnCancelAddBook.setOnClickListener() {
            finish()
        }

        binding.btnAddBook.setOnClickListener() {
            val name = binding.edtNameBook.text.toString()
            val rentalPrice = binding.edtRentPrice.text.toString()


            if (name.isEmpty()) {
                binding.edtNameBook.error = "Tên sách không được để trống"
                return@setOnClickListener
            }
            if (binding.spinnerAddBookCategory.selectedItem == null) {
                binding.spinnerAddBookCategory.setErrorText("Please choose category book")
                Toast.makeText(this, "Please choose book", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }else{
                binding.spinnerAddBookCategory.setErrorText("")
            }
            if (rentalPrice.isEmpty()) {
                binding.edtRentPrice.error = "Giá thuê không được để trống"
                return@setOnClickListener
            }
            if (rentalPrice.toInt() < 0) {
                binding.edtRentPrice.error = "Giá thuê không được nhỏ hơn 0"
                return@setOnClickListener
            }

            val categoryBook = categoryBookDTO.id

            bookDAO = BookDAO(this)
            bookDTO = BookDTO(-1, name, rentalPrice.toInt(), categoryBook)
            val result = bookDAO.insertBook(bookDTO)
            if (result > 0) {
                val builderSuccess =
                    androidx.appcompat.app.AlertDialog.Builder(this, R.style.CustomDialog)
                val bindingSuccess =
                    com.example.managerlibrary.databinding.DialogLoginSuccessBinding.inflate(
                        layoutInflater
                    )

                builderSuccess.setView(bindingSuccess.root)
                val dialogSuccess = builderSuccess.create()
                dialogSuccess.setCancelable(false)
                dialogSuccess.show()
                bindingSuccess.txtLoginSuccess.text = "Thêm thành công sách!"
                bindingSuccess.btnLoginSuccess.setOnClickListener() {
                    dialogSuccess.dismiss()
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("ok", "bookOK")
                    startActivity(intent)
                    finish()
                }

            }else{
                Toast.makeText(this, "Thêm thất bại", Toast.LENGTH_SHORT).show()
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