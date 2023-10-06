package com.example.managerlibrary.ui.manager

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.managerlibrary.dao.CategoryBookDAO
import com.example.managerlibrary.databinding.ActivityAddCategoryBooksBinding
import com.example.managerlibrary.dto.CategoryBookDTO
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AddCategoryBooksActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddCategoryBooksBinding
    private lateinit var categoryDAO: CategoryBookDAO
    private lateinit var categoryDTO: CategoryBookDTO
    val dbCategory = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCategoryBooksBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setSupportActionBar(binding.toolbarAddCategoryBooks)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        binding.btnCancelAddCategoryBooks.setOnClickListener {
            finish()
        }

        binding.btnSaveAddCategoryBooks.setOnClickListener {
            val name = binding.edtAddCategoryBooksName.text.toString()
            if (name.isEmpty()) {
                binding.edtAddCategoryBooksName.error = "Nhập tên"
                return@setOnClickListener
            }
            val id = dbCategory.collection("category books").document().id
            val category = hashMapOf(
                "id" to id,
                "name" to name
            )

            dbCategory.collection("category books")
                .add(category)
                .addOnSuccessListener {
                    Toast.makeText(this, "Thêm loại sách thành công", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Thêm loại sách lỗi", Toast.LENGTH_SHORT).show()
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