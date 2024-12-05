package com.nqmgaming.managerlibrary.ui.manager

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import com.nqmgaming.managerlibrary.R
import com.nqmgaming.managerlibrary.adapter.CategoryAddBookAdapter
import com.nqmgaming.managerlibrary.dao.BookDAO
import com.nqmgaming.managerlibrary.dao.CategoryBookDAO
import com.nqmgaming.managerlibrary.databinding.ActivityEditBookBinding
import com.nqmgaming.managerlibrary.dto.BookDTO
import com.nqmgaming.managerlibrary.dto.CategoryBookDTO
import com.nqmgaming.managerlibrary.ui.MainActivity

class EditBookActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditBookBinding
    private lateinit var categoryBookDAO: CategoryBookDAO
    lateinit var categoryBookDTO: CategoryBookDTO
    private lateinit var bookDTO: BookDTO
    lateinit var bookDAO: BookDAO
    lateinit var listCategoryBook: ArrayList<CategoryBookDTO>
    lateinit var adapter: CategoryAddBookAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBookBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setSupportActionBar(binding.toolbarEditBook)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        //get all category book
        categoryBookDAO = CategoryBookDAO(this)
        listCategoryBook = categoryBookDAO.getAllCategoryBooks()

        //set value to spinner
        adapter = CategoryAddBookAdapter(this, listCategoryBook)
        binding.spinnerEditBookCategory.adapter = adapter

        //set value to spinner
        binding.spinnerEditBookCategory.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    p1: View?,
                    position: Int,
                    p3: Long
                ) {
                    categoryBookDTO = listCategoryBook[position]
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }
            }


        //get intent
        val intent = intent
        val idCategoryBook = intent.getStringExtra("idBook")
        if (idCategoryBook != null) {
            bookDAO = BookDAO(this)
            bookDTO = bookDAO.getBookByID(idCategoryBook.toInt())

            binding.edtIdBookEdit.setText(bookDTO.idBook.toString())
            binding.edtNameBookEdit.setText(bookDTO.name)
            binding.edtRentPriceEdit.setText(bookDTO.rentalFee.toString())

            //get category book by id
            categoryBookDAO = CategoryBookDAO(this)
            categoryBookDTO = categoryBookDAO.getCategoryBookById(bookDTO.category)


            //set value to spinner
            val selectedBookPosition = listCategoryBook.indexOf(categoryBookDTO)
            binding.spinnerEditBookCategory.setSelection(selectedBookPosition)
        }
        binding.edtIdBookEdit.setOnClickListener {
            binding.edtIdBookEdit.error = "Không thể thay đổi mã sách"
        }
        binding.btnCancelEditBook.setOnClickListener {
            finish()
        }
        binding.btnEditBook.setOnClickListener {
            val idBook = binding.edtIdBookEdit.text.toString()
            val nameBook = binding.edtNameBookEdit.text.toString()
            val rentalFee = binding.edtRentPriceEdit.text.toString()

            if (nameBook.isEmpty()) {
                binding.edtNameBookEdit.error = "Tên sách không được để trống"
                return@setOnClickListener
            }

            if (binding.spinnerEditBookCategory.selectedItem == null) {
                binding.spinnerEditBookCategory.errorText = "Loại sách không được để trống"
                return@setOnClickListener
            }

            if (rentalFee.isEmpty()) {
                binding.edtRentPriceEdit.error = "Giá thuê không được để trống"
                return@setOnClickListener
            }

            if (rentalFee.toInt() < 0) {
                binding.edtRentPriceEdit.error = "Giá thuê không được nhỏ hơn 0"
                return@setOnClickListener
            }

            bookDAO = BookDAO(this)
            val bookDTO =
                BookDTO(idBook.toInt(), nameBook, rentalFee.toInt(), categoryBookDTO.id, -1)
            val result = bookDAO.updateBook(bookDTO)
            if (result > 0) {
                val builderSuccess = androidx.appcompat.app.AlertDialog.Builder(this, R.style.CustomDialog)
                val bindingSuccess =
                    com.nqmgaming.managerlibrary.databinding.DialogLoginSuccessBinding.inflate(
                        layoutInflater
                    )
                builderSuccess.setView(bindingSuccess.root)
                val dialogSuccess = builderSuccess.create()
                dialogSuccess.show()
                bindingSuccess.txtLoginSuccess.text = "Sửa sách thành công"
                bindingSuccess.btnLoginSuccess.setOnClickListener {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("ok", "bookOK")
                    startActivity(intent)
                    dialogSuccess.dismiss()
                    finish()
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