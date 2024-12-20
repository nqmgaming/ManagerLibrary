package com.nqmgaming.managerlibrary.dao

import android.content.Context
import android.util.Log
import com.nqmgaming.managerlibrary.database.ManagerBookDataBase
import com.nqmgaming.managerlibrary.dto.BookDTO
import com.nqmgaming.managerlibrary.dto.LibraryLoanSlipDTO

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
                val idLibrarian = cursor.getString(1)
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

    //get top 10 book from loan
    fun getTop10Book(): ArrayList<BookDTO> {
        val dbReadable = db.readableDatabase
        val sql =
            "SELECT LibraryLoanSlip.bookID," +
                    " COUNT(LibraryLoanSlip.bookID) AS count, " +
                    "Book.bookName," +
                    " Book.rentalFee," +
                    " Book.categoryID " + // Lấy trường categoryID từ bảng Book
                    "FROM LibraryLoanSlip " +
                    "INNER JOIN Book ON LibraryLoanSlip.bookID = Book.bookID" +
                    " GROUP BY LibraryLoanSlip.bookID " +
                    "ORDER BY count DESC LIMIT 10"
        val cursor = dbReadable.rawQuery(sql, null)
        val list = ArrayList<BookDTO>()
        if (cursor.count > 0) {
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                val idBook = cursor.getInt(0)
                val count = cursor.getInt(1)
                val name = cursor.getString(2)
                val rentalFee = cursor.getInt(3)
                val category = cursor.getInt(4)
                val bookDTO = BookDTO(idBook, name, rentalFee, category, count )
                list.add(bookDTO)
                cursor.moveToNext()
            }
        }
        cursor.close()
        dbReadable.close()
        return list
    }

    //thống kê doanh thu toàn bộ hóa đơn
    fun getRevenue(): Int {
        val dbReadable = db.readableDatabase
        val sql = "SELECT SUM(Book.rentalFee) FROM LibraryLoanSlip " +
                "INNER JOIN Book ON LibraryLoanSlip.bookID = Book.bookID"
        val cursor = dbReadable.rawQuery(sql, null)
        var revenue = 0
        if (cursor.count > 0) {
            cursor.moveToFirst()
            revenue = cursor.getInt(0)
        }
        cursor.close()
        dbReadable.close()
        return revenue
    }

    //thông kê doanh thu theo ngày: ngày bắt đầu và ngày kết thúc
    fun getRevenueByDate(startDate: String, endDate: String): Int {
        Log.d("getRevenueByDate", "Checking revenue from $startDate to $endDate")
        val dbReadable = db.readableDatabase
        val sql = "SELECT SUM(Book.rentalFee) FROM LibraryLoanSlip " +
                "INNER JOIN Book ON LibraryLoanSlip.bookID = Book.bookID " +
                "WHERE LibraryLoanSlip.loanDate BETWEEN ? AND ?"
        val cursor = dbReadable.rawQuery(sql, arrayOf(startDate, endDate))
        var revenue = 0
        if (cursor.count > 0) {
            cursor.moveToFirst()
            revenue = cursor.getInt(0)
        }
        cursor.close()
        dbReadable.close()

        Log.d("getRevenueByDate", "Revenue from $startDate to $endDate is: $revenue")
        return revenue
    }

    //lấy số lượng hóa đơn theo id của thủ thư
    fun getNumberOfLoanSlipByID(id: String): Int {
        val dbReadable = db.readableDatabase
        val sql = "SELECT COUNT(loanSlipID) FROM LibraryLoanSlip WHERE librarianID = ?"
        val cursor = dbReadable.rawQuery(sql, arrayOf(id))
        var count = 0
        if (cursor.count > 0) {
            cursor.moveToFirst()
            count = cursor.getInt(0)
        }
        cursor.close()
        dbReadable.close()
        return count
    }

    //get list loan slip by id(int) return loan slip dto
    fun getLoanSlipByID(id: Int): LibraryLoanSlipDTO {
        val dbReadable = db.readableDatabase
        val sql = "SELECT * FROM LibraryLoanSlip WHERE loanSlipID = ?"
        val cursor = dbReadable.rawQuery(sql, arrayOf(id.toString()))
        var libraryLoanSlipDTO = LibraryLoanSlipDTO(-1, -1, "", -1, "", -1)
        if (cursor.count > 0) {
            cursor.moveToFirst()
            val idLoanSlip = cursor.getInt(0)
            val idLibrarian = cursor.getString(1)
            val idMember = cursor.getInt(2)
            val idBook = cursor.getInt(3)
            val dateLoan = cursor.getString(4)
            val status = cursor.getInt(5)
            libraryLoanSlipDTO = LibraryLoanSlipDTO(
                idLoanSlip,
                idBook,
                idLibrarian,
                idMember,
                dateLoan,
                status
            )
        }
        cursor.close()
        dbReadable.close()
        return libraryLoanSlipDTO
    }

    //get librian by id book(int) return librarianDTO
    fun getLibrarianByIDBook(id:Int) : LibraryLoanSlipDTO {
        val dbReadable = db.readableDatabase
        val sql = "SELECT * FROM LibraryLoanSlip WHERE bookID = ?"
        val cursor = dbReadable.rawQuery(sql, arrayOf(id.toString()))
        var librarianDTO = LibraryLoanSlipDTO(-1, -1, "", -1, "", -1)
        if (cursor.count > 0) {
            cursor.moveToFirst()
            val idLoanSlip = cursor.getInt(0)
            val idLibrarian = cursor.getString(1)
            val idMember = cursor.getInt(2)
            val idBook = cursor.getInt(3)
            val dateLoan = cursor.getString(4)
            val status = cursor.getInt(5)
            librarianDTO = LibraryLoanSlipDTO(
                idLoanSlip,
                idBook,
                idLibrarian,
                idMember,
                dateLoan,
                status
            )
        }
        cursor.close()
        dbReadable.close()
        return librarianDTO
    }

    //insert loan slip
    fun insertLoanSlip(libraryLoanSlipDTO: LibraryLoanSlipDTO): Boolean {
        val dbWritable = db.writableDatabase
        val sql =
            "INSERT INTO LibraryLoanSlip(librarianID, memberID, bookID, loanDate, status) VALUES(?,?,?,?,?)"
        try {
            dbWritable.execSQL(
                sql,
                arrayOf(
                    libraryLoanSlipDTO.idLibrarian,
                    libraryLoanSlipDTO.idMember.toString(),
                    libraryLoanSlipDTO.idBook.toString(),
                    libraryLoanSlipDTO.dateLoan,
                    libraryLoanSlipDTO.status.toString()
                )
            )
            dbWritable.close()
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            dbWritable.close()
            return false
        }
    }

    //up date loan slip
    fun updateLoanSlip(libraryLoanSlipDTO: LibraryLoanSlipDTO): Boolean {
        val dbWritable = db.writableDatabase
        val sql =
            "UPDATE LibraryLoanSlip SET librarianID = ?, memberID = ?, bookID = ?, loanDate = ?, status = ? WHERE loanSlipID = ?"
        try {
            dbWritable.execSQL(
                sql,
                arrayOf(
                    libraryLoanSlipDTO.idLibrarian,
                    libraryLoanSlipDTO.idMember.toString(),
                    libraryLoanSlipDTO.idBook.toString(),
                    libraryLoanSlipDTO.dateLoan,
                    libraryLoanSlipDTO.status.toString(),
                    libraryLoanSlipDTO.id.toString()
                )
            )
            dbWritable.close()
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            dbWritable.close()
            return false
        }
    }

    //check loan slip exits by id member
    fun checkLoanSlipExitsByIDMember(id: Int): Boolean {
        val dbReadable = db.readableDatabase
        val sql = "SELECT * FROM LibraryLoanSlip WHERE memberID = ?"
        val cursor = dbReadable.rawQuery(sql, arrayOf(id.toString()))
        if (cursor.count > 0) {
            cursor.close()
            dbReadable.close()
            return true
        }
        cursor.close()
        dbReadable.close()
        return false
    }

    //check loan slip exits by id book
    fun checkLoanSlipExitsByIDBook(id: Int): Boolean {
        val dbReadable = db.readableDatabase
        val sql = "SELECT * FROM LibraryLoanSlip WHERE bookID = ?"
        val cursor = dbReadable.rawQuery(sql, arrayOf(id.toString()))
        if (cursor.count > 0) {
            cursor.close()
            dbReadable.close()
            return true
        }
        cursor.close()
        dbReadable.close()
        return false
    }

}