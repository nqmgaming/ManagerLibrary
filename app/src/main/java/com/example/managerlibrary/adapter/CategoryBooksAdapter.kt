package com.example.managerlibrary.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.managerlibrary.dao.CategoryBookDAO
import com.example.managerlibrary.databinding.ItemCategoryBinding
import com.example.managerlibrary.dto.CategoryBookDTO

class CategoryBooksAdapter(
    context: Context,
    private val listCategoryBooks: ArrayList<CategoryBookDTO>
) : RecyclerView.Adapter<CategoryBooksAdapter.CategoryBooksViewHolder>() {
    private val categoryBookDAO: CategoryBookDAO = CategoryBookDAO(context)

    class CategoryBooksViewHolder(private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        // Add any additional logic or data binding code here
        fun bind(
            categoryBook: CategoryBookDTO,
            categoryBookDAO: CategoryBookDAO,
            listCategoryBooks: ArrayList<CategoryBookDTO>
        ) {
            binding.cardBaseCategory.setOnClickListener() {
                if (binding.cardImageButtonCategory.visibility == android.view.View.GONE) {
                    binding.cardImageButtonCategory.visibility = android.view.View.VISIBLE
                } else {
                    binding.cardImageButtonCategory.visibility = android.view.View.GONE
                }
            }
            binding.txtCategoryID.text = "Mã loại sách: " + categoryBook.id.toString()
            binding.txtCategoryName.text = "Tên loại sách: " + categoryBook.name


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryBooksViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCategoryBinding.inflate(inflater, parent, false)
        return CategoryBooksViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listCategoryBooks.size
    }

    override fun onBindViewHolder(holder: CategoryBooksViewHolder, position: Int) {
        val categoryBook = listCategoryBooks[position]
        // Bind data to the view holder
        holder.bind(categoryBook, categoryBookDAO, listCategoryBooks)
    }
}