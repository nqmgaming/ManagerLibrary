package com.nqmgaming.managerlibrary.ui.manager

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.nqmgaming.managerlibrary.R
import com.nqmgaming.managerlibrary.adapter.CategoryAddBookAdapter
import com.nqmgaming.managerlibrary.dao.BookDAO
import com.nqmgaming.managerlibrary.dao.CategoryBookDAO
import com.nqmgaming.managerlibrary.databinding.ActivityAddBookBinding
import com.nqmgaming.managerlibrary.dto.BookDTO
import com.nqmgaming.managerlibrary.dto.CategoryBookDTO
import com.nqmgaming.managerlibrary.ui.MainActivity

class AddBookActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddBookBinding
    private lateinit var categoryBookDAO: CategoryBookDAO
    lateinit var categoryBookDTO: CategoryBookDTO
    lateinit var bookDAO: BookDAO
    private lateinit var bookDTO: BookDTO
    lateinit var listCategoryBook: ArrayList<CategoryBookDTO>
    private lateinit var categoryLoanAdapter: CategoryAddBookAdapter
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

        binding.btnCancelAddBook.setOnClickListener {
            finish()
        }

        binding.btnAddBook.setOnClickListener {
            val name = binding.edtNameBook.text.toString()
            val rentalPrice = binding.edtRentPrice.text.toString()


            if (name.isEmpty()) {
                binding.edtNameBook.error = getString(R.string.txt_book_name_can_not_be_empty)
                return@setOnClickListener
            }
            if (binding.spinnerAddBookCategory.selectedItem == null) {
                binding.spinnerAddBookCategory.errorText =
                    getString(R.string.txt_please_choose_category_book)
                Toast.makeText(this, getString(R.string.txt_please_choose_book), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }else{
                binding.spinnerAddBookCategory.setErrorText("")
            }
            if (rentalPrice.isEmpty()) {
                binding.edtRentPrice.error = getString(R.string.txt_loan_price_can_not_be_empty)
                return@setOnClickListener
            }
            if (rentalPrice.toInt() < 0) {
                binding.edtRentPrice.error =
                    getString(R.string.txt_loan_price_can_not_small_than_zero)
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
                    com.nqmgaming.managerlibrary.databinding.DialogLoginSuccessBinding.inflate(
                        layoutInflater
                    )

                builderSuccess.setView(bindingSuccess.root)
                val dialogSuccess = builderSuccess.create()
                dialogSuccess.setCancelable(false)
                dialogSuccess.show()
                bindingSuccess.txtLoginSuccess.text = getString(R.string.txt_add_book_successfully)
                bindingSuccess.btnLoginSuccess.setOnClickListener {
                    dialogSuccess.dismiss()
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("ok", "bookOK")
                    startActivity(intent)
                    finish()
                }

            }else{
                Toast.makeText(this, getString(R.string.txt_add_book_fail), Toast.LENGTH_SHORT).show()
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