package com.example.managerlibrary.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.managerlibrary.ui.manager.EditCategoryBookActivity
import com.example.managerlibrary.R
import com.example.managerlibrary.dao.BookDAO
import com.example.managerlibrary.dao.CategoryBookDAO
import com.example.managerlibrary.databinding.DialogConfirmBinding
import com.example.managerlibrary.databinding.DialogDeleteCategoryBinding
import com.example.managerlibrary.databinding.DialogLoginSuccessBinding
import com.example.managerlibrary.databinding.ItemCategoryBinding
import com.example.managerlibrary.dto.CategoryBookDTO
import com.example.managerlibrary.fragment.manager.ManagerCategoryBooksFragment
import com.example.managerlibrary.ui.MainActivity

class CategoryBooksAdapter(
    context: Context,
    private val listCategoryBooks: ArrayList<CategoryBookDTO>
) : RecyclerView.Adapter<CategoryBooksAdapter.CategoryBooksViewHolder>() {
    private val categoryBookDAO: CategoryBookDAO = CategoryBookDAO(context)
    private val bookDAO: BookDAO = BookDAO(context)

    class CategoryBooksViewHolder(private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        // Add any additional logic or data binding code here
        fun bind(
            categoryBook: CategoryBookDTO,
            categoryBookDAO: CategoryBookDAO,
            listCategoryBooks: ArrayList<CategoryBookDTO>,
            bookDAO: BookDAO
        ) {
            binding.cardBaseCategory.setOnClickListener() {
                if (binding.cardImageButtonCategory.visibility == android.view.View.GONE) {
                    binding.cardImageButtonCategory.visibility = android.view.View.VISIBLE
                } else {
                    binding.cardImageButtonCategory.visibility = android.view.View.GONE
                }
            }
            binding.txtCategoryID.text = "Mã loại sách: " + categoryBook.id.toString()
            binding.txtCategoryName.text = "Tên loại sách: " + categoryBook.name

            binding.btnDeleteCategory.setOnClickListener() {
                val builderConfirm = AlertDialog.Builder(binding.root.context)
                val bindingConfirm =
                    DialogConfirmBinding.inflate(LayoutInflater.from(binding.root.context))
                builderConfirm.setView(bindingConfirm.root)
                val dialogConfirm = builderConfirm.create()

                bindingConfirm.btnNo.setOnClickListener() {
                    dialogConfirm.dismiss()
                }
                bindingConfirm.btnYes.setOnClickListener() {
                    //check if id category book is used in book
                    val result = bookDAO.checkBookExistByIdCategory(categoryBook.id)
                    if (result) {
                        dialogConfirm.dismiss()
                        val builderError = AlertDialog.Builder(binding.root.context)
                        val bindingError =
                            DialogDeleteCategoryBinding.inflate(LayoutInflater.from(binding.root.context))
                        builderError.setView(bindingError.root)
                        val dialogError = builderError.create()
                        bindingError.btnDeleteError.setOnClickListener() {
                            dialogError.dismiss()

                        }
                        dialogError.show()
                    } else {
                        val builderLoading = AlertDialog.Builder(binding.root.context)
                        val inflaterLoading = LayoutInflater.from(binding.root.context)
                        builderLoading.setView(
                            inflaterLoading.inflate(
                                com.example.managerlibrary.R.layout.dialog_proccessing,
                                null
                            )
                        )
                        builderLoading.setCancelable(false) // if you want the user to wait until the process finishes
                        val dialogLoading = builderLoading.create()
                        dialogLoading.show()

                        var result = categoryBookDAO.deleteCategoryBook(categoryBook.id)
                        if (result > 0) {
                            listCategoryBooks.remove(categoryBook)
                            //load lại danh sách phiếu mượn bằng cách refresh lại fragment
                            val fragment = ManagerCategoryBooksFragment()
                            val fragmentManagerCategory =
                                (binding.root.context as MainActivity).supportFragmentManager
                            fragmentManagerCategory.beginTransaction().apply {
                                replace(R.id.nav_host_fragment, fragment)
                                commit()
                            }
                            dialogConfirm.dismiss()
                            dialogLoading.dismiss()

                            val builderSuccess = AlertDialog.Builder(binding.root.context)
                            val bindingSuccess =
                                DialogLoginSuccessBinding.inflate(LayoutInflater.from(binding.root.context))
                            builderSuccess.setView(bindingSuccess.root)
                            val dialogSuccess = builderSuccess.create()
                            bindingSuccess.txtLoginSuccess.text =
                                "Xóa loại sách thành công"
                            bindingSuccess.btnLoginSuccess.setOnClickListener {
                                dialogSuccess.dismiss()
                            }
                            dialogSuccess.show()
                        }

                    }
                }

                dialogConfirm.show()

            }

            binding.btnEditCategory.setOnClickListener(){
                //intent id category book to edit
                val intent = android.content.Intent(binding.root.context, EditCategoryBookActivity::class.java)
                intent.putExtra("idCategory", categoryBook.id.toString())
                binding.root.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryBooksViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCategoryBinding.inflate(inflater, parent, false)
        return CategoryBooksViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listCategoryBooks.size
    }

    override fun onBindViewHolder(holder: CategoryBooksViewHolder, position: Int) {
        val categoryBook = listCategoryBooks[position]
        // Bind data to the view holder
        holder.bind(categoryBook, categoryBookDAO, listCategoryBooks, bookDAO)
    }
}