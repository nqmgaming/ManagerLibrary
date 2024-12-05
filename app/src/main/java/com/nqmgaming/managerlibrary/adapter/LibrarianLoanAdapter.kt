package com.nqmgaming.managerlibrary.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.nqmgaming.managerlibrary.databinding.ItemLibrianLoanBinding
import com.nqmgaming.managerlibrary.dto.LibrarianDTO

class LibrarianLoanAdapter(
    context: Context,
    listLibrarian: ArrayList<LibrarianDTO>
) : ArrayAdapter<LibrarianDTO>(context, 0, listLibrarian) {

    private var _binding: ItemLibrianLoanBinding? = null
    private val binding get() = _binding!!

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, parent)
    }

    private fun initView(position: Int, parent: ViewGroup): View {
        _binding = ItemLibrianLoanBinding.inflate(LayoutInflater.from(context), parent, false)

        val librarian = getItem(position)
        binding.txtItemLibrarianName.text = librarian?.name

        return binding.root
    }
}
