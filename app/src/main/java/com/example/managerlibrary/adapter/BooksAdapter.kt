package com.example.managerlibrary.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.managerlibrary.R
import com.example.managerlibrary.dao.BookDAO
import com.example.managerlibrary.dao.CategoryBookDAO
import com.example.managerlibrary.dao.LibraryLoanSlipDAO
import com.example.managerlibrary.databinding.DialogConfirmBinding
import com.example.managerlibrary.databinding.DialogDeleteCategoryBinding
import com.example.managerlibrary.databinding.DialogLoginSuccessBinding
import com.example.managerlibrary.databinding.ItemBooksBinding
import com.example.managerlibrary.dto.BookDTO
import com.example.managerlibrary.fragment.manager.ManagerBooksFragment
import com.example.managerlibrary.fragment.manager.ManagerMembersFragment
import com.example.managerlibrary.ui.MainActivity
import com.example.managerlibrary.ui.manager.EditBookActivity
import com.example.managerlibrary.ui.manager.EditMemberActivity

class BooksAdapter(
    context: Context,
    private val listBooks: ArrayList<BookDTO>
) : RecyclerView.Adapter<BooksAdapter.BooksViewHolder>() {
    private val bookDAO: BookDAO = BookDAO(context)
    private val categoryBookDAO: CategoryBookDAO = CategoryBookDAO(context)
    private val libraryLoanSlipDAO = LibraryLoanSlipDAO(context)

    class BooksViewHolder(
        private val binding: ItemBooksBinding
    ) :
        RecyclerView.ViewHolder(
            binding.root
        ) {
        fun bind(
            bookDTO: BookDTO,
            bookDAO: BookDAO,
            listBooks: ArrayList<BookDTO>,
            categoryBookDAO: CategoryBookDAO,
            libraryLoanSlipDAO: LibraryLoanSlipDAO
        ) {

            binding.cardBaseBook.setOnClickListener() {
                if (binding.cardImageButtonBook.visibility == android.view.View.GONE) {
                    binding.cardImageButtonBook.visibility = android.view.View.VISIBLE
                } else {
                    binding.cardImageButtonBook.visibility = android.view.View.GONE
                }
            }

            binding.txtBookIdManager.text = "Mã sách: " + bookDTO.idBook.toString()
            binding.txtBookNameManager.text = "Tên sách: " + bookDTO.name
            binding.txtCategoryNameManager.text = "Thể loại: " +
                    categoryBookDAO.getNameCategoryBookById(bookDTO.category)
            binding.txtRentPriceManager.text = "Giá thuê: " + bookDTO.rentalFee.toString() + " VND"

            binding.btnDeleteBook.setOnClickListener(){
                val builderConfirm = AlertDialog.Builder(binding.root.context)
                val bindingCofirm =
                    DialogConfirmBinding.inflate(LayoutInflater.from(binding.root.context))
                builderConfirm.setView(bindingCofirm.root)
                val dialogConfirm = builderConfirm.create()
                bindingCofirm.txtLoginSuccess.text =
                    "Bạn có chắc chắn muốn xóa sách \n viên này không?"
                bindingCofirm.btnNo.setOnClickListener() {
                    dialogConfirm.dismiss()
                }
                bindingCofirm.btnYes.setOnClickListener() {
                    //check if id member is used in library loan slip
                    val result = libraryLoanSlipDAO.checkLoanSlipExitsByIDBook(bookDTO.idBook)
                    if (result) {
                        dialogConfirm.dismiss()
                        val builderError = AlertDialog.Builder(binding.root.context)
                        val bindingError =
                            DialogDeleteCategoryBinding.inflate(LayoutInflater.from(binding.root.context))
                        builderError.setView(bindingError.root)
                        val dialogError = builderError.create()
                        bindingError.txtDeleteError.text =
                            "Không thể xóa sách này \n vì có người mượn sách này trong \n thư viện"
                        bindingError.btnDeleteError.setOnClickListener() {
                            dialogError.dismiss()

                        }
                        dialogError.show()
                    } else {
                        val resultDelete = bookDAO.deleteBookById(bookDTO.idBook)
                        if (resultDelete > 0) {
                            listBooks.remove(bookDTO)
                            val fragment = ManagerBooksFragment()
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
                                "Xóa sách thành công"
                            bindingSuccess.btnLoginSuccess.setOnClickListener {
                                dialogSuccess.dismiss()
                            }
                            dialogSuccess.show()
                        }
                    }
                }
                dialogConfirm.show()
            }

            binding.btnEditBook.setOnClickListener(){
                val intent = android.content.Intent(
                    binding.root.context,
                    EditBookActivity::class.java
                )
                intent.putExtra("idBook", bookDTO.idBook.toString())
                binding.root.context.startActivity(intent)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BooksViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemBooksBinding.inflate(inflater, parent, false)
        return BooksViewHolder(binding)
    }


    override fun getItemCount(): Int {
        return listBooks.size
    }

    override fun onBindViewHolder(holder: BooksViewHolder, position: Int) {
        val bookDTO = listBooks[position]
        // Bind data to the view holder
        holder.bind(bookDTO, bookDAO, listBooks, categoryBookDAO, libraryLoanSlipDAO)
    }
}