package com.example.managerlibrary.ui.manager

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.managerlibrary.R
import com.example.managerlibrary.dao.CategoryBookDAO
import com.example.managerlibrary.databinding.ActivityEditCategoryBookBinding
import com.example.managerlibrary.dto.CategoryBookDTO
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class EditCategoryBookActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditCategoryBookBinding
    private lateinit var categoryBookDAO: CategoryBookDAO
    private lateinit var categoryBookDTO: CategoryBookDTO

    private val dbCategoryBook = Firebase.firestore
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
        var id:String = ""
        if (idCategoryBook != null) {

            dbCategoryBook.collection("category books")
                .get()
                .addOnSuccessListener {
                    for (document in it) {
                        if (document.data["id"] == idCategoryBook) {
                            categoryBookDTO = CategoryBookDTO(
                                document.data["id"].toString(),
                                document.data["name"].toString()
                            )
                            binding.edtIdCategory.setText(categoryBookDTO.id)
                            binding.edtNameCategory.setText(categoryBookDTO.name)
                        }
                    }
                }
                .addOnFailureListener(){
                    Toast.makeText(this, "Lỗi", Toast.LENGTH_SHORT).show()
                }

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

                dbCategoryBook.collection("category books")
                    .get()
                    .addOnSuccessListener {
                        for (document in it) {
                            if (document.data["id"] == idCategoryBook) {
                                id = document.id
                            }
                        }
                        val category = hashMapOf(
                            "id" to idCategory,
                            "name" to nameCategory
                        )
                        dbCategoryBook.collection("category books")
                            .document(id)
                            .set(category)
                            .addOnSuccessListener {
                                Toast.makeText(
                                    this,
                                    "Sửa loại sách thành công",
                                    Toast.LENGTH_SHORT
                                ).show()
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
                                    finish()

                                }
                            }
                            .addOnFailureListener {
                                Toast.makeText(this, "Sửa loại sách lỗi", Toast.LENGTH_SHORT).show()
                            }
                    }
                    .addOnFailureListener(){
                        Toast.makeText(this, "Lỗi", Toast.LENGTH_SHORT).show()
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