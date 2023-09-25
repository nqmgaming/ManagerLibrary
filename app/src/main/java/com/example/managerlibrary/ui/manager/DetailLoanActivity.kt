package com.example.managerlibrary.ui.manager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.example.managerlibrary.dao.LibrarianDAO
import com.example.managerlibrary.dto.LibrarianDTO
import com.example.managerlibrary.dao.LibraryLoanSlipDAO
import com.example.managerlibrary.dto.LibraryLoanSlipDTO
import com.example.managerlibrary.dao.MemberDAO
import com.example.managerlibrary.dto.MemberDTO
import com.example.managerlibrary.dao.BookDAO
import com.example.managerlibrary.dao.CategoryBookDAO
import com.example.managerlibrary.databinding.ActivityDetailLoanBinding
import com.example.managerlibrary.dto.BookDTO
import com.example.managerlibrary.dto.CategoryBookDTO

class DetailLoanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailLoanBinding

    lateinit var loanSlipDAO: LibraryLoanSlipDAO
    lateinit var bookDAO: BookDAO
    lateinit var bookDTO: BookDTO
    lateinit var categoryBookDAO: CategoryBookDAO
    lateinit var categoryBookDTO: CategoryBookDTO
    lateinit var memberDAO: MemberDAO
    lateinit var memberDTO: MemberDTO
    lateinit var librarianDAO: LibrarianDAO
    lateinit var librarianDTO: LibrarianDTO
    lateinit var loanSlipDTO: LibraryLoanSlipDTO
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailLoanBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //set nút back trên toolbar   app:navigationIcon="@drawable/arrow"
        setSupportActionBar(binding.toolbarDetailLoan)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        //get data from intent
        val intent = intent

//        get data from database
        loanSlipDAO = LibraryLoanSlipDAO(this)
        val idLoanSlip = intent.getStringExtra("idLoanSlip")
        Toast.makeText(this, idLoanSlip + "", Toast.LENGTH_SHORT).show()

        if (idLoanSlip != null) {
            loanSlipDTO = loanSlipDAO.getLoanSlipByID(idLoanSlip.toInt())
            memberDAO = MemberDAO(this)
            memberDTO = memberDAO.getMemberDTOById(loanSlipDTO.idMember)
            librarianDAO = LibrarianDAO(this)
            librarianDTO = librarianDAO.getLibrarianByID(loanSlipDTO.idLibrarian)
            bookDAO = BookDAO(this)
            bookDTO = bookDAO.getBookByID(loanSlipDTO.idBook)
            categoryBookDAO = CategoryBookDAO(this)
            categoryBookDTO = categoryBookDAO.getCategoryBookById(bookDTO.category)
        }

        binding.tvIdBillDetail.text = "Mã phiếu mượn: " + loanSlipDTO.id.toString()
        binding.tvNameBookDetail.text = "Tên sách: " + bookDTO.name
        binding.tvCategoryBookDetail.text = "Loại sách: " + categoryBookDTO.name
        binding.tvPriceBookDetail.text = "Giá thuê: " + bookDTO.rentalFee.toString()
        binding.tvDateLoanDetail.text = "Ngày mượn: " + loanSlipDTO.dateLoan
        binding.tvNameUserDetail.text = "Tên thủ thư: " + librarianDTO.name
        binding.tvNameUserBorrowDetail.text = "Thành viên mượn: " + memberDTO.name
        binding.tvStatusDetail.text =
            if (loanSlipDTO.status == 0) "Trạng thái:  Chưa trả" else "Trạng thái: Đã trả"

        binding.btnOkDetail.setOnClickListener() {
            finish()
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