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

    // Thay đổi ViewHolder để sử dụng View Binding thay vì View trực tiếp
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
            binding.txtNameBook.text = "Tên sách: " + bookDTO.name

            val memberDTO: MemberDTO = memberDAO.getMemberDTOById(libraryLoanSlipDTO.idMember)
            binding.txtNameMember.text = "Người mượn: " + memberDTO.name

            binding.txtDate.text = "Ngày mượn: " + libraryLoanSlipDTO.dateLoan
            binding.txtPrice.text = "Giá thuê: " + bookDTO.rentalFee.toString() + " VND"
            if (libraryLoanSlipDTO.status == 0) {
                binding.txtStatus.text = "Chưa trả"
            } else {
                binding.txtStatus.text = "Đã trả"
            }

            //if đã trả thì đổi background màu xám
            if (libraryLoanSlipDTO.status == 1) {
                binding.cardBaseInfor.setBackgroundResource(R.color.gray)
                binding.linearImageButton.setBackgroundResource(R.color.gray_light)
            }

            //when click on item bill set visible for layout detail if it is invisible and vice versa
            binding.cardBaseInfor.setOnClickListener() {
                if (binding.cardImageButton.visibility == android.view.View.GONE) {
                    binding.cardImageButton.visibility = android.view.View.VISIBLE
                } else {
                    binding.cardImageButton.visibility = android.view.View.GONE
                }
            }

            binding.btnDelete.setOnClickListener() {
                //aleart
                val builderConfirm = AlertDialog.Builder(binding.root.context)
                val bindingCofirm =
                    DialogConfirmBinding.inflate(LayoutInflater.from(binding.root.context))
                builderConfirm.setView(bindingCofirm.root)
                val dialogConfirm = builderConfirm.create()
                bindingCofirm.txtLoginSuccess.text =
                    "Bạn có chắc chắn muốn xóa phiếu \n mượn này không?"
                bindingCofirm.btnNo.setOnClickListener() {
                    dialogConfirm.dismiss()
                }
                bindingCofirm.btnYes.setOnClickListener() {
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
                        "Xóa phiếu mượn thành công"
                    bindingSuccess.btnLoginSuccess.setOnClickListener {
                        dialogSuccess.dismiss()
                    }
                    dialogSuccess.show()

                }
                dialogConfirm.show()

            }

            binding.btnInfo.setOnClickListener() {
                //intent id to detail loan activity
                val intent = android.content.Intent(binding.root.context, DetailLoanActivity::class.java)
                intent.putExtra("idLoanSlip", libraryLoanSlipDTO.id.toString())
                binding.root.context.startActivity(intent)

            }

            binding.btnEdit.setOnClickListener(){
                //intent id to edit loan activity
                val intent = android.content.Intent(binding.root.context, EditLoanActivity::class.java)
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
        //notifyDataSetChanged() when data change

    }

}