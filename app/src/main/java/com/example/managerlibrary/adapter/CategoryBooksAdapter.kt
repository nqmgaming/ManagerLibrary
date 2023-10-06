package com.example.managerlibrary.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.managerlibrary.dao.BookDAO
import com.example.managerlibrary.dao.CategoryBookDAO
import com.example.managerlibrary.databinding.DialogConfirmBinding
import com.example.managerlibrary.databinding.DialogDeleteCategoryBinding
import com.example.managerlibrary.databinding.ItemCategoryBinding
import com.example.managerlibrary.dto.CategoryBookDTO
import com.example.managerlibrary.ui.manager.EditCategoryBookActivity
import com.google.firebase.firestore.FirebaseFirestore

class CategoryBooksAdapter(
    private val context: Context,
    private val listCategoryBooks: ArrayList<CategoryBookDTO>
) : RecyclerView.Adapter<CategoryBooksAdapter.CategoryBooksViewHolder>() {

    private val categoryBookDAO: CategoryBookDAO = CategoryBookDAO(context)
    private val bookDAO: BookDAO = BookDAO(context)
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    inner class CategoryBooksViewHolder(private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.cardBaseCategory.setOnClickListener {
                binding.cardImageButtonCategory.visibility =
                    if (binding.cardImageButtonCategory.visibility == android.view.View.GONE)
                        android.view.View.VISIBLE
                    else
                        android.view.View.GONE
            }

            // Inside the bind() function of CategoryBooksViewHolder
            binding.btnDeleteCategory.setOnClickListener {
                val builderConfirm = AlertDialog.Builder(context)
                val bindingConfirm = DialogConfirmBinding.inflate(LayoutInflater.from(context))
                builderConfirm.setView(bindingConfirm.root)
                val dialogConfirm = builderConfirm.create()

                bindingConfirm.btnNo.setOnClickListener {
                    dialogConfirm.dismiss()
                }
                var categoryId = ""
                 firestore.collection("category books")
                    .get()
                    .addOnSuccessListener(){
                        for(document in it){
                            if(document.data["id"] == listCategoryBooks[adapterPosition].id){
                                 categoryId = document.id
                            }
                        }
                    }

                bindingConfirm.btnYes.setOnClickListener {


                    // Check if the category book is used in any book
                    firestore.collection("books")
                        .whereEqualTo("idCategory", categoryId)
                        .get()
                        .addOnSuccessListener { querySnapshot ->
                            if (!querySnapshot.isEmpty) {
                                // The category is used in a book, show an error dialog
                                val builderError = AlertDialog.Builder(context)
                                val bindingError =
                                    DialogDeleteCategoryBinding.inflate(LayoutInflater.from(context))
                                builderError.setView(bindingError.root)
                                val dialogError = builderError.create()

                                bindingError.btnDeleteError.setOnClickListener {
                                    dialogError.dismiss()
                                }

                                dialogError.show()
                            } else {
                                // The category is not used in any book, proceed with deletion

                                // Delete the category from Firebase Firestore
                                firestore.collection("category books")
                                    .document(categoryId)
                                    .delete()
                                    .addOnSuccessListener {
                                        notifyDataSetChanged()

                                        dialogConfirm.dismiss()

                                        val builderSuccess = AlertDialog.Builder(context)
                                        val bindingSuccess =
                                            DialogConfirmBinding.inflate(LayoutInflater.from(context))
                                        builderSuccess.setView(bindingSuccess.root)
                                        val dialogSuccess = builderSuccess.create()

                                        bindingSuccess.btnYes.visibility = View.GONE
                                        bindingSuccess.btnNo.text = "OK"
                                        bindingSuccess.txtLoginSuccess.text =
                                            "Xóa loại sách thành công"

                                        bindingSuccess.btnNo.setOnClickListener {
                                            dialogSuccess.dismiss()
                                        }

                                        dialogSuccess.show()
                                    }
                                    .addOnFailureListener { exception ->
                                        // Handle failure
                                        // Show an error message or log the error
                                    }
                            }
                        }
                }

                dialogConfirm.show()
            }

            binding.btnEditCategory.setOnClickListener {
                // Intent to edit the category book
                val intent = android.content.Intent(context, EditCategoryBookActivity::class.java)
                intent.putExtra("idCategory", listCategoryBooks[adapterPosition].id)
                context.startActivity(intent)
            }
        }

        fun bind(categoryBook: CategoryBookDTO) {
            binding.txtCategoryID.text = "Mã loại sách: ${categoryBook.id}"
            binding.txtCategoryName.text = "Tên loại sách: ${categoryBook.name}"
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
        holder.bind(categoryBook)
    }
}
