package com.example.managerlibrary.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.managerlibrary.R
import com.example.managerlibrary.dao.LibraryLoanSlipDAO
import com.example.managerlibrary.dao.MemberDAO
import com.example.managerlibrary.databinding.DialogConfirmBinding
import com.example.managerlibrary.databinding.DialogDeleteCategoryBinding
import com.example.managerlibrary.databinding.DialogLoginSuccessBinding
import com.example.managerlibrary.databinding.ItemMemberBinding
import com.example.managerlibrary.dto.MemberDTO
import com.example.managerlibrary.fragment.manager.ManagerMembersFragment
import com.example.managerlibrary.ui.MainActivity
import com.example.managerlibrary.ui.manager.EditMemberActivity

class MemberAdapter(
    context: Context,
    private val listMembers: ArrayList<MemberDTO>
) : RecyclerView.Adapter<MemberAdapter.MemberViewHolder>() {
    private val memberDAO: MemberDAO = MemberDAO(context)
    private val libraryLoanSlipDAO: LibraryLoanSlipDAO = LibraryLoanSlipDAO(context)

    class MemberViewHolder(
        private val binding: ItemMemberBinding
    ) : RecyclerView.ViewHolder(
        binding.root
    ) {

        fun bind(
            memberDTO: MemberDTO,
            memberDAO: MemberDAO,
            listMembers: ArrayList<MemberDTO>,
            libraryLoanSlipDAO: LibraryLoanSlipDAO
        ) {
            binding.cardBaseMember.setOnClickListener {
                if (binding.cardImageButtonMember.visibility == android.view.View.GONE) {
                    binding.cardImageButtonMember.visibility = android.view.View.VISIBLE
                } else {
                    binding.cardImageButtonMember.visibility = android.view.View.GONE
                }
            }
            binding.txtIdMember.text = "Mã thành viên: " + memberDTO.id.toString()
            binding.txtNameMember.text = "Tên: " + memberDTO.name
            binding.txtBirthMember.text = "Năm sinh: " + memberDTO.birthYear

            binding.btnDeleteMember.setOnClickListener {
                val builderConfirm = AlertDialog.Builder(binding.root.context)
                val bindingCofirm =
                    DialogConfirmBinding.inflate(LayoutInflater.from(binding.root.context))
                builderConfirm.setView(bindingCofirm.root)
                val dialogConfirm = builderConfirm.create()
                bindingCofirm.txtLoginSuccess.text =
                    "Bạn có chắc chắn muốn xóa thành \n viên này không?"
                bindingCofirm.btnNo.setOnClickListener {
                    dialogConfirm.dismiss()
                }
                bindingCofirm.btnYes.setOnClickListener {
                    //check if id member is used in library loan slip
                    val result = libraryLoanSlipDAO.checkLoanSlipExitsByIDMember(memberDTO.id)
                    if (result) {
                        dialogConfirm.dismiss()
                        val builderError = AlertDialog.Builder(binding.root.context)
                        val bindingError =
                            DialogDeleteCategoryBinding.inflate(LayoutInflater.from(binding.root.context))
                        builderError.setView(bindingError.root)
                        val dialogError = builderError.create()
                        bindingError.txtDeleteError.text =
                            "Không thể xóa thành viên này \n vì có mượn sách trong thư viện"
                        bindingError.btnDeleteError.setOnClickListener {
                            dialogError.dismiss()

                        }
                        dialogError.show()
                    } else {
                        val resultDelete = memberDAO.deleteMember(memberDTO.id)
                        if (resultDelete) {
                            listMembers.remove(memberDTO)
                            val fragment = ManagerMembersFragment()
                            val fragmentManagerCategory =
                                (binding.root.context as MainActivity).supportFragmentManager
                            fragmentManagerCategory.beginTransaction().apply {
                                replace(R.id.nav_host_fragment, fragment)
                                commit()
                            }
                            dialogConfirm.dismiss()
                            val builderSuccess = AlertDialog.Builder(binding.root.context)
                            val bindingSuccess =
                                DialogLoginSuccessBinding.inflate(LayoutInflater.from(binding.root.context))
                            builderSuccess.setView(bindingSuccess.root)
                            val dialogSuccess = builderSuccess.create()
                            bindingSuccess.txtLoginSuccess.text =
                                "Xóa thành viên thành công"
                            bindingSuccess.btnLoginSuccess.setOnClickListener {
                                dialogSuccess.dismiss()
                            }
                            dialogSuccess.show()
                        }
                    }
                }
                dialogConfirm.show()

            }

            binding.btnEditMember.setOnClickListener {
                val intent = android.content.Intent(
                    binding.root.context,
                    EditMemberActivity::class.java
                )
                intent.putExtra("idMember", memberDTO.id.toString())
                binding.root.context.startActivity(intent)
            }

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
        holder.bind(memberDTO, memberDAO, listMembers, libraryLoanSlipDAO)
    }

    override fun getItemCount(): Int {
        return listMembers.size
    }
}