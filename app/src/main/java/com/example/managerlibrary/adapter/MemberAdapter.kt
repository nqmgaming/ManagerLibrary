package com.example.managerlibrary.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.managerlibrary.dao.MemberDAO
import com.example.managerlibrary.dto.MemberDTO
import com.example.managerlibrary.databinding.ItemMemberBinding

class MemberAdapter(
    context: Context,
    private val listMembers: ArrayList<MemberDTO>
) : RecyclerView.Adapter<MemberAdapter.MemberViewHolder>() {
    private val memberDAO: MemberDAO = MemberDAO(context)

    class MemberViewHolder(
        private val binding: ItemMemberBinding
    ) : RecyclerView.ViewHolder(
        binding.root
    ) {

        fun bind(
            memberDTO: MemberDTO,
            memberDAO: MemberDAO,
            listMembers: ArrayList<MemberDTO>
        ) {
            binding.cardBaseMember.setOnClickListener() {
                if (binding.cardImageButtonMember.visibility == android.view.View.GONE) {
                    binding.cardImageButtonMember.visibility = android.view.View.VISIBLE
                } else {
                    binding.cardImageButtonMember.visibility = android.view.View.GONE
                }
            }
            binding.txtIdMember.text = "Mã thành viên: " +  memberDTO.id.toString()
            binding.txtNameMember.text = "Tên: " +  memberDTO.name
            binding.txtBirthMember.text = "Năm sinh: " +  memberDTO.birthYear
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMemberBinding.inflate(inflater, parent, false)
        return MemberViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MemberViewHolder, position: Int) {
        val memberDTO = listMembers[position]
        // Bind data to the view holder
        holder.bind(memberDTO, memberDAO, listMembers)
    }

    override fun getItemCount(): Int {
        return listMembers.size
    }
}