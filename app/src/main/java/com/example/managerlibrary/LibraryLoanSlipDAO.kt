package com.example.managerlibrary

import android.content.Context
import com.example.managerlibrary.database.ManagerBookDataBase

class LibraryLoanSlipDAO(context: Context) {
    private val db: ManagerBookDataBase = ManagerBookDataBase(context)

    //get all loan slip return list of loan slip
    fun getAllLoanSlip(): ArrayList<LibraryLoanSlipDTO> {
        val dbReadable = db.readableDatabase
        val sql = "SELECT * FROM LibraryLoanSlip"
        val cursor = dbReadable.rawQuery(sql, null)
        val list = ArrayList<LibraryLoanSlipDTO>()
        if (cursor.count > 0) {
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                val idLoanSlip = cursor.getInt(0)
                val idLibrarian = cursor.getInt(1)
                val idMember = cursor.getInt(2)
                val idBook = cursor.getInt(3)
                val dateLoan = cursor.getString(4)
                val status = cursor.getInt(5)
                val libraryLoanSlipDTO = LibraryLoanSlipDTO(
                    idLoanSlip,
                    idBook,
                    idLibrarian,
                    idMember,
                    dateLoan,
                    status
                )
                list.add(libraryLoanSlipDTO)
                cursor.moveToNext()
            }
        }
        cursor.close()
        dbReadable.close()
        return list
    }

    //delete loan slip by id
    fun deleteLoanSlip(id: Int): Boolean {
        val dbWritable = db.writableDatabase
        val sql = "DELETE FROM LibraryLoanSlip WHERE loanSlipID = ?"
        val cursor = dbWritable.rawQuery(sql, arrayOf(id.toString()))
        if (cursor.count > 0) {
            cursor.close()
            dbWritable.close()
            return true
        }
        cursor.close()
        dbWritable.close()
        return false
    }
}