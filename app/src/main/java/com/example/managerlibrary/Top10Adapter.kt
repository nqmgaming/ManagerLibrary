package com.example.managerlibrary

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.managerlibrary.databinding.ItemTop10Binding

class Top10Adapter(
    private val context: Context,
    private val listTop10: ArrayList<BookDTO>
) : RecyclerView.Adapter<Top10Adapter.Top10ViewHolder>() {
    private val bookDAO: BookDAO = BookDAO(context)

    class Top10ViewHolder(
        private val binding: ItemTop10Binding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            book: BookDTO,
            bookDAO: BookDAO,
            listTop10: ArrayList<BookDTO>
        ) {
            // Assuming BookDTO has 'name' and 'count' fields
            binding.txtBookIdManagerTop10.text = "Mã sách: " + book.idBook.toString()
            binding.txtBookNameManagerTop10.text = "Tên sách: " + book.name
            binding.txtRentPriceManager.text = "Giá thuê: " + book.rentalFee.toString() + " VND"
            binding.txtTimesRented.text = "Số lần mượn: " + book.timeRental.toString()
            binding.txtCategoryNameManagerTop10.text = "Thể loại: " + book.category
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Top10ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTop10Binding.inflate(inflater, parent, false)
        return Top10ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: Top10ViewHolder, position: Int) {
        val book = listTop10[position]
        holder.bind(book, bookDAO, listTop10)
    }

    override fun getItemCount(): Int {
        return listTop10.size
    }
}
