package com.example.managerlibrary

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.managerlibrary.dao.CategoryBookDAO
import com.example.managerlibrary.databinding.ItemBooksBinding
import com.example.managerlibrary.databinding.ItemCategoryBinding

class BooksAdapter(
    context: Context,
    private val listBooks: ArrayList<BookDTO>
) : RecyclerView.Adapter<BooksAdapter.BooksViewHolder>() {
    private val bookDAO: BookDAO = BookDAO(context)
    private val categoryBookDAO: CategoryBookDAO = CategoryBookDAO(context)

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
            categoryBookDAO: CategoryBookDAO
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
                    categoryBookDAO.getNameCategoryBookById(bookDTO.idBook)
            binding.txtRentPriceManager.text = "Giá thuê: " + bookDTO.rentalFee.toString() + " VND"

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
        holder.bind(bookDTO, bookDAO, listBooks, categoryBookDAO)
    }
}