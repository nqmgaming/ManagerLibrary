package com.example.managerlibrary.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.managerlibrary.databinding.ItemMembersLoanBinding
import com.example.managerlibrary.dto.MemberDTO

class MemberLoanAdapter(
    context: Context,
    listMembers: ArrayList<MemberDTO>
) : ArrayAdapter<MemberDTO>(context, 0, listMembers) {

    private var _binding: ItemMembersLoanBinding? = null
    private val binding get() = _binding!!

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, parent)
    }

    private fun initView(position: Int, parent: ViewGroup): View {
        _binding =
            ItemMembersLoanBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        val member = getItem(position)
        binding.txtItemMemberName.text = member?.name

        return binding.root
    }
}