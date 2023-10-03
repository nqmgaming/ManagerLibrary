package com.example.managerlibrary.ui.manager

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.managerlibrary.dao.CategoryBookDAO
import com.example.managerlibrary.databinding.ActivityAddCategoryBooksBinding
import com.example.managerlibrary.dto.CategoryBookDTO
import com.example.managerlibrary.ui.MainActivity

class AddCategoryBooksActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddCategoryBooksBinding
    private lateinit var categoryDAO: CategoryBookDAO
    private lateinit var categoryDTO: CategoryBookDTO
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
            categoryDAO = CategoryBookDAO(this)
            categoryDTO = CategoryBookDTO(-1, name)
            val result = categoryDAO.insertCategoryBook(categoryDTO)
            if (result > 0) {
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("ok", "category")
                startActivity(intent)
                finish()
            }else{
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