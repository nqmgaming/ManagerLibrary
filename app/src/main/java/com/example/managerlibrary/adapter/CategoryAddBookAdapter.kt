package com.example.managerlibrary.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.managerlibrary.databinding.ItemCategoryAddBookBinding
import com.example.managerlibrary.dto.CategoryBookDTO


class CategoryAddBookAdapter(
    context: Context,
    private val listCategory: ArrayList<CategoryBookDTO>
) : ArrayAdapter<CategoryBookDTO>(context, 0, listCategory) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    private fun initView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = ItemCategoryAddBookBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        val category = getItem(position)
        binding.txtItemCategoryBookName.text = category?.name

        return binding.root
    }
}