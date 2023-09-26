package com.example.managerlibrary.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.managerlibrary.databinding.ItemBooksLoanBinding
import com.example.managerlibrary.dto.BookDTO

class BookLoanAdapter(
    context: Context,
    private val listBooks: ArrayList<BookDTO>
) : ArrayAdapter<BookDTO>(context, 0, listBooks) {

    private var _binding: ItemBooksLoanBinding? = null
    private val binding get() = _binding!!

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    private fun initView(position: Int, convertView: View?, parent: ViewGroup): View {
        _binding = ItemBooksLoanBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        val book = getItem(position)
        binding.txtItemBookName.text = book?.name

        return binding.root
    }
}