package com.example.managerlibrary

import android.content.Context
import com.example.managerlibrary.database.ManagerBookDataBase

class BookDAO (context: Context){
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
            val rentalFee = cursor.getDouble(2)
            val category = cursor.getString(3)
            val bookDTO = BookDTO(idBook, name, rentalFee, category)
            cursor.close()
            dbReadable.close()
            return bookDTO
        }
        cursor.close()
        dbReadable.close()
        return BookDTO(-1, "", -1.0, "")
    }
}