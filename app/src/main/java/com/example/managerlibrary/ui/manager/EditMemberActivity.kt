package com.example.managerlibrary.ui.manager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import com.example.managerlibrary.R
import com.example.managerlibrary.dao.MemberDAO
import com.example.managerlibrary.databinding.ActivityEditMemberBinding
import com.example.managerlibrary.dto.MemberDTO
import com.example.managerlibrary.ui.MainActivity

class EditMemberActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditMemberBinding
    lateinit var memberDAO: MemberDAO
    lateinit var memberDTO: MemberDTO
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditMemberBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setSupportActionBar(binding.toolbarEditMember)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val intent = intent
        val idCategoryBook = intent.getStringExtra("idMember")

        if (idCategoryBook != null) {
            memberDAO = MemberDAO(this)
            memberDTO = memberDAO.getMemberDTOById(idCategoryBook.toInt())

            binding.edtIdMember.setText(memberDTO.id.toString())
            binding.edtNameMember.setText(memberDTO.name)
            binding.edtBirthMemberEdit.setText(memberDTO.birthYear)

        }

        binding.edtIdMember.setOnClickListener() {
            binding.edtIdMember.error = "Không thể thay đổi mã thành viên"
        }

        binding.btnCancelEditMember.setOnClickListener() {
            finish()
        }

        binding.btnSaveEditMember.setOnClickListener() {
            var idMember = binding.edtIdMember.text.toString()
            var nameMember = binding.edtNameMember.text.toString()
            var birthMember = binding.edtBirthMemberEdit.text.toString()

            if (nameMember.isEmpty()) {
                binding.edtNameMember.error = "Tên thành viên không được để trống"
                return@setOnClickListener
            }

            if (birthMember.isEmpty()) {
                binding.edtBirthMemberEdit.error = "Năm sinh không được để trống"
                return@setOnClickListener
            }

            if (birthMember.toInt() < 1900 || birthMember.toInt() > 2021) {
                binding.edtBirthMemberEdit.error = "Năm sinh không hợp lệ"
                return@setOnClickListener
            }

            memberDAO = MemberDAO(this)
            memberDTO = MemberDTO(idMember.toInt(), nameMember, birthMember)
            val result = memberDAO.updateMember(memberDTO)
            if(result> 0){
                val builderSuccess = AlertDialog.Builder(this, R.style.CustomDialog)
                val bindingSuccess =
                    com.example.managerlibrary.databinding.DialogLoginSuccessBinding.inflate(
                        layoutInflater
                    )
                builderSuccess.setView(bindingSuccess.root)
                val dialogSuccess = builderSuccess.create()
                dialogSuccess.show()
                bindingSuccess.txtLoginSuccess.text = "Sửa thành viên thành công"
                bindingSuccess.btnLoginSuccess.setOnClickListener() {
                    dialogSuccess.dismiss()
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("ok", "member")
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}