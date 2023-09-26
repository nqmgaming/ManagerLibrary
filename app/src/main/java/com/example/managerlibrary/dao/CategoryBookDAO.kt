package com.example.managerlibrary.dao

import android.content.ContentValues
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
    fun getCategoryBookById(id: Int): CategoryBookDTO {
        val dbReadable = db.readableDatabase
        val sql = "SELECT * FROM CategoryBook WHERE categoryID = $id"
        val cursor = dbReadable.rawQuery(sql, null)
        var category: CategoryBookDTO
        if (cursor.count > 0) {
            cursor.moveToFirst()
            val idCategory = cursor.getInt(0)
            val nameCategory = cursor.getString(1)
            category = CategoryBookDTO(idCategory, nameCategory)
            cursor.close()
            dbReadable.close()
            return category
        }
        cursor.close()
        dbReadable.close()
        return CategoryBookDTO(-1, "")
    }

    //insert category book
    fun insertCategoryBook(categoryBookDTO: CategoryBookDTO): Long {
        var result = -1L
        val dbWritable = db.writableDatabase
        val values = ContentValues()
        values.put("categoryName", categoryBookDTO.name)
        try {
            result = dbWritable.insert("CategoryBook", null, values)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }

    //delete category book
    fun deleteCategoryBook(id: Int): Int {
        var result = -1
        val dbWritable = db.writableDatabase
        try {
            result = dbWritable.delete("CategoryBook", "categoryID = ?", arrayOf(id.toString()))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }

    //update category book
    fun updateCategoryBook(categoryBookDTO: CategoryBookDTO): Int {
        var result = -1
        val dbWritable = db.writableDatabase
        val values = ContentValues()
        values.put("categoryName", categoryBookDTO.name)
        try {
            result = dbWritable.update(
                "CategoryBook",
                values,
                "categoryID = ?",
                arrayOf(categoryBookDTO.id.toString())
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }

}