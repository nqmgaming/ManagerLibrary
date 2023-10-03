package com.example.managerlibrary.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.managerlibrary.dao.BookDAO
import com.example.managerlibrary.dto.BookDTO
import com.example.managerlibrary.ui.manager.DetailLoanActivity
import com.example.managerlibrary.dao.LibraryLoanSlipDAO
import com.example.managerlibrary.dto.LibraryLoanSlipDTO
import com.example.managerlibrary.ui.MainActivity
import com.example.managerlibrary.dao.MemberDAO
import com.example.managerlibrary.dto.MemberDTO
import com.example.managerlibrary.R
import com.example.managerlibrary.databinding.DialogConfirmBinding
import com.example.managerlibrary.databinding.DialogLoginSuccessBinding
import com.example.managerlibrary.databinding.ItemBillBinding
import com.example.managerlibrary.fragment.manager.ManagerBillsFragment
import com.example.managerlibrary.ui.manager.EditLoanActivity

class BillsAdapter(
    context: Context,
    private val listBills: ArrayList<LibraryLoanSlipDTO>
) : RecyclerView.Adapter<BillsAdapter.BillsViewHolder>() {

    private val bookDAO: BookDAO = BookDAO(context)
    private val memberDAO: MemberDAO = MemberDAO(context)
    private val libraryLoanSlipDAO: LibraryLoanSlipDAO = LibraryLoanSlipDAO(context)

    class BillsViewHolder(private val binding: ItemBillBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            libraryLoanSlipDTO: LibraryLoanSlipDTO,
            bookDAO: BookDAO,
            memberDAO: MemberDAO,
            libraryLoanSlipDAO: LibraryLoanSlipDAO,
            listBills: ArrayList<LibraryLoanSlipDTO>
        ) {
            //get name of book by id
            val bookDTO: BookDTO = bookDAO.getBookByID(libraryLoanSlipDTO.idBook)
            binding.txtNameBook.text =
                binding.root.context.getString(R.string.book_name) + bookDTO.name
            val memberDTO: MemberDTO = binding.root.context.let {
                memberDAO.getMemberDTOById(
                    libraryLoanSlipDTO.idMember
                )
            }
            binding.txtNameMember.text =
                binding.root.context.getString(R.string.username) + memberDTO.name

            binding.txtDate.text =
                binding.root.context.getString(R.string.date_loan) + libraryLoanSlipDTO.dateLoan
            binding.txtPrice.text =
                binding.root.context.getString(R.string.rental_fee) + bookDTO.rentalFee.toString()
            if (libraryLoanSlipDTO.status == 0) {
                binding.txtStatus.text = binding.root.context.getString(R.string.not_returned)
            } else {
                binding.txtStatus.text = binding.root.context.getString(R.string.returned)
            }

            //if đã trả thì đổi background màu xám
            if (libraryLoanSlipDTO.status == 1) {
                binding.cardBaseInfor.setBackgroundResource(R.color.gray)
                binding.linearImageButton.setBackgroundResource(R.color.gray_light)
            }

            //when click on item bill set visible for layout detail if it is invisible and vice versa
            binding.cardBaseInfor.setOnClickListener {
                if (binding.cardImageButton.visibility == android.view.View.GONE) {
                    binding.cardImageButton.visibility = android.view.View.VISIBLE
                } else {
                    binding.cardImageButton.visibility = android.view.View.GONE
                }
            }

            binding.btnDelete.setOnClickListener {
                //alert
                val builderConfirm = AlertDialog.Builder(binding.root.context)
                val bindingConfirm =
                    DialogConfirmBinding.inflate(LayoutInflater.from(binding.root.context))
                builderConfirm.setView(bindingConfirm.root)
                val dialogConfirm = builderConfirm.create()
                bindingConfirm.txtLoginSuccess.text =
                    binding.root.context.getString(R.string.confirm_delete)
                bindingConfirm.btnNo.setOnClickListener {
                    dialogConfirm.dismiss()
                }
                bindingConfirm.btnYes.setOnClickListener {
                    libraryLoanSlipDAO.deleteLoanSlip(libraryLoanSlipDTO.id)
                    listBills.remove(libraryLoanSlipDTO)

                    //load lại danh sách phiếu mượn bằng cách refresh lại fragment
                    val fragment = ManagerBillsFragment()
                    val fragmentManager =
                        (binding.root.context as MainActivity).supportFragmentManager
                    fragmentManager.beginTransaction().apply {
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
                        binding.root.context.getString(R.string.delete_success)
                    bindingSuccess.btnLoginSuccess.setOnClickListener {
                        dialogSuccess.dismiss()
                    }
                    dialogSuccess.show()

                }
                dialogConfirm.show()

            }

            binding.btnInfo.setOnClickListener {
                //intent id to detail loan activity
                val intent =
                    android.content.Intent(binding.root.context, DetailLoanActivity::class.java)
                intent.putExtra("idLoanSlip", libraryLoanSlipDTO.id.toString())
                binding.root.context.startActivity(intent)

            }

            binding.btnEdit.setOnClickListener {
                //intent id to edit loan activity
                val intent =
                    android.content.Intent(binding.root.context, EditLoanActivity::class.java)
                intent.putExtra("idLoanSlip", libraryLoanSlipDTO.id.toString())
                binding.root.context.startActivity(intent)
            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillsViewHolder {
        // Sử dụng View Binding để inflate layout
        val binding = ItemBillBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BillsViewHolder(binding)

    }

    override fun getItemCount(): Int {
        return listBills.size

    }

    override fun onBindViewHolder(holder: BillsViewHolder, position: Int) {
        // Sử dụng hàm bind đã được định nghĩa ở trên
        holder.bind(listBills[position], bookDAO, memberDAO, libraryLoanSlipDAO, listBills)

    }

}

