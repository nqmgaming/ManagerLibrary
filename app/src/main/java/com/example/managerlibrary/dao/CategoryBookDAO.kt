package com.example.managerlibrary.dao

import android.content.Context
import com.example.managerlibrary.database.ManagerBookDataBase
import com.example.managerlibrary.dto.CategoryBookDTO

class CategoryBookDAO(context: Context) {
    private val db: ManagerBookDataBase = ManagerBookDataBase(context)

    //get all category book
    fun getAllCategoryBooks(): ArrayList<CategoryBookDTO> {
        val dbReadable = db.readableDatabase
        val sql = "SELECT * FROM CategoryBook"
        val cursor = dbReadable.rawQuery(sql, null)
        val list = ArrayList<CategoryBookDTO>()
        if (cursor.count > 0) {
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                val category: CategoryBookDTO
                val idCategory = cursor.getInt(0)
                val nameCategory = cursor.getString(1)
                category = CategoryBookDTO(idCategory, nameCategory)
                list.add(category)
                cursor.moveToNext()
            }
        }
        cursor.close()
        dbReadable.close()
        return list
    }

    //get name category book by id
    fun getNameCategoryBookById(id: Int): String {
        val dbReadable = db.readableDatabase
        val sql = "SELECT * FROM CategoryBook WHERE categoryID = $id"
        val cursor = dbReadable.rawQuery(sql, null)
        var nameCategory = ""
        if (cursor.count > 0) {
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                nameCategory = cursor.getString(1)
                cursor.moveToNext()
            }
        }
        cursor.close()
        dbReadable.close()
        return nameCategory
    }

    //get category by id
    fun getCategoryBookById(id: String): CategoryBookDTO {
        val dbReadable = db.readableDatabase
        val sql = "SELECT * FROM CategoryBook WHERE categoryID = $id"
        val cursor = dbReadable.rawQuery(sql, null)
        var category: CategoryBookDTO = CategoryBookDTO(1, "")
        if (cursor.count > 0) {
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                val idCategory = cursor.getInt(0)
                val nameCategory = cursor.getString(1)
                category = CategoryBookDTO(idCategory, nameCategory)
                cursor.moveToNext()
            }
        }
        cursor.close()
        dbReadable.close()
        return category
    }

}