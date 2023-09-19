package com.example.managerlibrary

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.managerlibrary.databinding.ItemBillBinding
import com.example.managerlibrary.fragment.manager.ManagerBillsFragment

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
            binding.txtNameBook.text = bookDTO.name

            val memberDTO: MemberDTO = memberDAO.getMemberDTOById(libraryLoanSlipDTO.idMember)
            binding.txtNameMember.text = memberDTO.name

            binding.txtDate.text = libraryLoanSlipDTO.dateLoan
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
                val builder = android.app.AlertDialog.Builder(binding.root.context)
                builder.setTitle("Xóa phiếu mượn")
                builder.setMessage("Bạn có chắc chắn muốn xóa phiếu mượn này không?")
                builder.setPositiveButton("Có") { dialog, which ->
                    libraryLoanSlipDAO.deleteLoanSlip(libraryLoanSlipDTO.id)
                    listBills.remove(libraryLoanSlipDTO)

                    //load lại danh sách phiếu mượn bằng cách refresh lại fragment
                    val fragment = ManagerBillsFragment()
                    val fragmentManager = (binding.root.context as MainActivity).supportFragmentManager
                    fragmentManager.beginTransaction().apply {
                        replace(R.id.nav_host_fragment, fragment)
                        commit()
                    }

                }
                builder.setNegativeButton("Không") { dialog, which ->
                    dialog.dismiss()
                }
                builder.show()
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