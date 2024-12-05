package com.nqmgaming.managerlibrary.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.nqmgaming.managerlibrary.databinding.ItemCategoryAddBookBinding
import com.nqmgaming.managerlibrary.dto.CategoryBookDTO


class CategoryAddBookAdapter(
    context: Context,
    listCategory: ArrayList<CategoryBookDTO>
) : ArrayAdapter<CategoryBookDTO>(context, 0, listCategory) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, parent)
    }

    private fun initView(position: Int, parent: ViewGroup): View {
        val binding = ItemCategoryAddBookBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        val category = getItem(position)
        binding.txtItemCategoryBookName.text = category?.name

        return binding.root
    }
}