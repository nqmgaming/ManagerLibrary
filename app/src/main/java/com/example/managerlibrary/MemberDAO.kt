package com.example.managerlibrary

import android.content.Context
import com.example.managerlibrary.database.ManagerBookDataBase
import com.example.managerlibrary.MemberDTO

class MemberDAO(context: Context) {
    private val db: ManagerBookDataBase = ManagerBookDataBase(context)

    //get member name by id return memberDTO
    fun getMemberDTOById(id: Int): MemberDTO {
        val dbReadable = db.readableDatabase
        val sql = "SELECT * FROM Member WHERE memberID = ?"
        val cursor = dbReadable.rawQuery(sql, arrayOf(id.toString()))
        if (cursor.count > 0) {
            cursor.moveToFirst()
            val idMember = cursor.getInt(0)
            val name = cursor.getString(1)
            val birthYear = cursor.getString(2)
            val memberDTO = MemberDTO(idMember, name, birthYear)
            cursor.close()
            dbReadable.close()
            return memberDTO
        }
        cursor.close()
        dbReadable.close()
        return MemberDTO(-1, "", "")
    }
}