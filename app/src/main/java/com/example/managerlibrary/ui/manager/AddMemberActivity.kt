package com.example.managerlibrary.ui.manager

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.managerlibrary.R
import com.example.managerlibrary.dao.MemberDAO
import com.example.managerlibrary.databinding.ActivityAddMemberBinding
import com.example.managerlibrary.dto.MemberDTO
import com.example.managerlibrary.ui.MainActivity

class AddMemberActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddMemberBinding
    private lateinit var memberDAO: MemberDAO
    private lateinit var memberDTO: MemberDTO
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddMemberBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setSupportActionBar(binding.toolbarAddMember)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        binding.btnCancelAddMember.setOnClickListener {
            finish()
        }

        binding.btnSaveAddMember.setOnClickListener {
            val name = binding.edtNameMember.text.toString()
            val birthYear = binding.edtBirthMember.text.toString()

            if (name.isEmpty()) {
                binding.edtNameMember.error = "Tên không được để trống"
                return@setOnClickListener
            }

            if (birthYear.isEmpty()) {
                binding.edtBirthMember.error = "Năm sinh không được để trống"
                return@setOnClickListener
            }

            //giới hạn năm sinh từ 1900 đến 2016
            if (birthYear.toInt() < 1900 || birthYear.toInt() > 2016) {
                binding.edtBirthMember.error = "Năm sinh không hợp lệ"
                return@setOnClickListener
            }

            memberDAO = MemberDAO(this)
            memberDTO = MemberDTO(-1, name, birthYear)
            val result = memberDAO.insertMember(memberDTO)
            if (result > 0) {
                val builderSuccess =
                    androidx.appcompat.app.AlertDialog.Builder(this, R.style.CustomDialog)
                val bindingSuccess =
                    com.example.managerlibrary.databinding.DialogLoginSuccessBinding.inflate(
                        layoutInflater
                    )
                builderSuccess.setView(bindingSuccess.root)
                val dialogSuccess = builderSuccess.create()
                dialogSuccess.show()
                dialogSuccess.setCancelable(false)
                bindingSuccess.txtLoginSuccess.text = "Thêm thành công thành viên!"
                bindingSuccess.btnLoginSuccess.setOnClickListener {
                    dialogSuccess.dismiss()
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("ok", "member")
                    startActivity(intent)
                    finish()
                }

            } else {
                Toast.makeText(this, "Thêm thất bại", Toast.LENGTH_SHORT).show()
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