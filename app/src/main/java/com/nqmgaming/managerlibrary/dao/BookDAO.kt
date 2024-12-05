package com.nqmgaming.managerlibrary.dao

import android.content.ContentValues
import android.content.Context
import com.nqmgaming.managerlibrary.dto.BookDTO
import com.nqmgaming.managerlibrary.database.ManagerBookDataBase

class BookDAO(context: Context) {
    private val db: ManagerBookDataBase = ManagerBookDataBase(context)

    //return bookDTO by id
    fun getBookByID(id: Int): BookDTO {
        val dbReadable = db.readableDatabase
        val sql = "SELECT * FROM Book WHERE bookID = ?"
        val cursor = dbReadable.rawQuery(sql, arrayOf(id.toString()))
        if (cursor.count > 0) {
            cursor.moveToFirst()
            val idBook = cursor.getInt(0)
            val name = cursor.getString(1)
            val rentalFee = cursor.getInt(2)
            val category = cursor.getInt(3)
            val bookDTO = BookDTO(idBook, name, rentalFee, category, 0)
            cursor.close()
            dbReadable.close()
            return bookDTO
        }
        cursor.close()
        dbReadable.close()
        return BookDTO(-1, "", -1, -1, 0)
    }

    //get all book
    fun getAllBook(): ArrayList<BookDTO> {
        val dbReadable = db.readableDatabase
        val sql = "SELECT * FROM Book"
        val cursor = dbReadable.rawQuery(sql, null)
        val listBook = ArrayList<BookDTO>()
        if (cursor.count > 0) {
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                val idBook = cursor.getInt(0)
                val name = cursor.getString(1)
                val rentalFee = cursor.getInt(2)
                val category = cursor.getInt(3)
                val bookDTO = BookDTO(idBook, name, rentalFee, category)
                listBook.add(bookDTO)
                cursor.moveToNext()
            }
        }
        cursor.close()
        dbReadable.close()
        return listBook
    }

    //check book exist by id category
    fun checkBookExistByIdCategory(idCategory: Int): Boolean {
        val dbReadable = db.readableDatabase
        val sql = "SELECT * FROM Book WHERE categoryID = ?"
        val cursor = dbReadable.rawQuery(sql, arrayOf(idCategory.toString()))
        if (cursor.count > 0) {
            cursor.close()
            dbReadable.close()
            return true
        }
        cursor.close()
        dbReadable.close()
        return false
    }
    //insert book
    fun insertBook(bookDTO: BookDTO): Long {
        val dbWritable = db.writableDatabase
        val values = ContentValues()
        values.put("bookName", bookDTO.name)
        values.put("rentalFee", bookDTO.rentalFee)
        values.put("categoryID", bookDTO.category)
        val result = dbWritable.insert("Book", null, values)
        dbWritable.close()
        return result
    }

    //delete book by id
    fun deleteBookById(id: Int): Int {
        val dbWritable = db.writableDatabase
        val result = dbWritable.delete("Book", "bookID = ?", arrayOf(id.toString()))
        dbWritable.close()
        return result
    }

    //update book
    fun updateBook(bookDTO: BookDTO): Int {
        val dbWritable = db.writableDatabase
        val values = ContentValues()
        values.put("bookName", bookDTO.name)
        values.put("rentalFee", bookDTO.rentalFee)
        values.put("categoryID", bookDTO.category)
        val result = dbWritable.update("Book", values, "bookID = ?", arrayOf(bookDTO.idBook.toString()))
        dbWritable.close()
        return result
    }
}
