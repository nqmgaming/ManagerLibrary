package com.example.managerlibrary.ui.manager

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.managerlibrary.dao.BookDAO
import com.example.managerlibrary.dao.CategoryBookDAO
import com.example.managerlibrary.dao.LibrarianDAO
import com.example.managerlibrary.dao.LibraryLoanSlipDAO
import com.example.managerlibrary.dao.MemberDAO
import com.example.managerlibrary.databinding.ActivityDetailLoanBinding
import com.example.managerlibrary.dto.BookDTO
import com.example.managerlibrary.dto.CategoryBookDTO
import com.example.managerlibrary.dto.LibrarianDTO
import com.example.managerlibrary.dto.LibraryLoanSlipDTO
import com.example.managerlibrary.dto.MemberDTO

class DetailLoanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailLoanBinding

    private lateinit var loanSlipDAO: LibraryLoanSlipDAO
    lateinit var bookDAO: BookDAO
    private lateinit var bookDTO: BookDTO
    private lateinit var categoryBookDAO: CategoryBookDAO
    private lateinit var categoryBookDTO: CategoryBookDTO
    private lateinit var memberDAO: MemberDAO
    private lateinit var memberDTO: MemberDTO
    private lateinit var librarianDAO: LibrarianDAO
    private lateinit var librarianDTO: LibrarianDTO
    private lateinit var loanSlipDTO: LibraryLoanSlipDTO
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

        binding.btnOkDetail.setOnClickListener {
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