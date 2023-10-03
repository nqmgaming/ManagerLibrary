package com.example.managerlibrary.dao

import android.content.ContentValues
import android.content.Context
import com.example.managerlibrary.database.ManagerBookDataBase
import com.example.managerlibrary.dto.LibrarianDTO
import java.sql.SQLDataException

class LibrarianDAO(context: Context) {
    private val db: ManagerBookDataBase = ManagerBookDataBase(context)

    //get librarian username(id) by id return long  if not found return -1
    fun getLibrarianUsernameByID(id: String): Int {
        var result: Int = -1
        val dbReadable = db.readableDatabase
        val sql = "SELECT * FROM Librarian WHERE librarianID = ?"
        val cursor = dbReadable.rawQuery(sql, arrayOf(id))
        if (cursor.count > 0) {
            cursor.moveToFirst()
            result = cursor.getShort(0).toByte().toInt()
        }
        cursor.close()
        dbReadable.close()
        return result
    }

    //check password librarian return true if correct else return false
    fun checkPasswordLibrarian(id: String, password: String): Int {
        var result: Int = -1
        val dbReadable = db.readableDatabase
        val sql = "SELECT * FROM Librarian WHERE librarianID = ? AND password = ?"
        val cursor = dbReadable.rawQuery(sql, arrayOf(id, password))
        if (cursor.count > 0) {
            cursor.moveToFirst()
            result = 1
        }
        cursor.close()
        dbReadable.close()
        return result
    }

    //get librarian by id return LibrarianDTO
    fun getLibrarianByID(id: String): LibrarianDTO {
        val dbReadable = db.readableDatabase
        val sql = "SELECT * FROM Librarian WHERE librarianID = ?"
        val cursor = dbReadable.rawQuery(sql, arrayOf(id))
        val librarianDTO = LibrarianDTO()
        if (cursor.count > 0) {
            cursor.moveToFirst()
            librarianDTO.id = cursor.getString(0)
            librarianDTO.name = cursor.getString(1)
            librarianDTO.password = cursor.getString(2)
            librarianDTO.role = cursor.getString(3)
        }
        cursor.close()
        dbReadable.close()
        return librarianDTO
    }

    //edit librarian truyền vào là librarianDTO
    fun editLibrarian(librarianDTO: LibrarianDTO): Long {
        val values = ContentValues()
        values.put("librarianID", librarianDTO.id)
        values.put("librarianName", librarianDTO.name)
        values.put("password", librarianDTO.password)
        values.put("role", librarianDTO.role)
        var result: Long = -1L
        val dbWritable = db.writableDatabase
        try {
            result = dbWritable.update("Librarian", values, "librarianID = ?", arrayOf(librarianDTO.id)).toLong()
        } catch (e: SQLDataException) {
            e.printStackTrace()
        } finally {
            dbWritable.close()
        }
        return result
    }

    //check if username is exist return true if exist else return false
    fun checkUsernameExist(id: String): Boolean {
        val dbReadable = db.readableDatabase
        val sql = "SELECT * FROM Librarian WHERE librarianID = ?"
        val cursor = dbReadable.rawQuery(sql, arrayOf(id))
        if (cursor.count > 0) {
            cursor.close()
            dbReadable.close()
            return true
        }
        cursor.close()
        dbReadable.close()
        return false
    }

    //insert librarian
    fun insertLibrarian(librarianDTO: LibrarianDTO): Long {
        val values = ContentValues()
        values.put("librarianID", librarianDTO.id)
        values.put("librarianName", librarianDTO.name)
        values.put("password", librarianDTO.password)
        values.put("role", librarianDTO.role)
        var result: Long = -1L
        val dbWritable = db.writableDatabase
        try {
            result = dbWritable.insert("Librarian", null, values)
        } catch (e: SQLDataException) {
            e.printStackTrace()
        } finally {
            dbWritable.close()
        }
        return result
    }

    //get all librarian
    fun getAllLibrarian(): ArrayList<LibrarianDTO> {
        val listLibrarian = ArrayList<LibrarianDTO>()
        val dbReadable = db.readableDatabase
        val sql = "SELECT * FROM Librarian"
        val cursor = dbReadable.rawQuery(sql, null)
        if (cursor.count > 0) {
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                val librarianDTO = LibrarianDTO()
                librarianDTO.id = cursor.getString(0)
                librarianDTO.name = cursor.getString(1)
                librarianDTO.password = cursor.getString(2)
                librarianDTO.role = cursor.getString(3)
                listLibrarian.add(librarianDTO)
                cursor.moveToNext()
            }
        }
        cursor.close()
        dbReadable.close()
        return listLibrarian
    }
}