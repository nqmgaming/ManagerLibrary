package com.nqmgaming.managerlibrary.dao

import android.content.ContentValues
import android.content.Context
import com.nqmgaming.managerlibrary.database.ManagerBookDataBase
import com.nqmgaming.managerlibrary.dto.MemberDTO

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

    //get all member
    fun getAllMember(): ArrayList<MemberDTO> {
        val dbReadable = db.readableDatabase
        val sql = "SELECT * FROM Member"
        val cursor = dbReadable.rawQuery(sql, null)
        val listMember = ArrayList<MemberDTO>()
        if (cursor.count > 0) {
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                val idMember = cursor.getInt(0)
                val name = cursor.getString(1)
                val birthYear = cursor.getString(2)
                val memberDTO = MemberDTO(idMember, name, birthYear)
                listMember.add(memberDTO)
                cursor.moveToNext()
            }
        }
        cursor.close()
        dbReadable.close()
        return listMember
    }

    //insert member
    fun insertMember(memberDTO: MemberDTO): Long {
        var result = -1L
        val dbWritable = db.writableDatabase
        val values = ContentValues()
        values.put("memberName", memberDTO.name)
        values.put("birthYear", memberDTO.birthYear)
        try {
            result = dbWritable.insert("Member", null, values)
        } catch (e: Exception) {
            return result
        }
        dbWritable.close()
        return result
    }

    //delete member
    fun deleteMember(id: Int): Boolean {
        val dbWritable = db.writableDatabase
        val result = dbWritable.delete("Member", "memberID = ?", arrayOf(id.toString()))
        dbWritable.close()
        return result > 0
    }

    //update member
    fun updateMember(memberDTO: MemberDTO): Int {
        val dbWritable = db.writableDatabase
        val values = ContentValues()
        values.put("memberName", memberDTO.name)
        values.put("birthYear", memberDTO.birthYear)
        val result = dbWritable.update("Member", values, "memberID = ?", arrayOf(memberDTO.id.toString()))
        dbWritable.close()
        return result
    }

}