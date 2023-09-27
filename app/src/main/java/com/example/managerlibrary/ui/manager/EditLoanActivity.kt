package com.example.managerlibrary.ui.manager

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import com.example.managerlibrary.R
import com.example.managerlibrary.adapter.BillsAdapter
import com.example.managerlibrary.adapter.BookLoanAdapter
import com.example.managerlibrary.adapter.BooksAdapter
import com.example.managerlibrary.adapter.LibrarianLoanAdapter
import com.example.managerlibrary.adapter.MemberAdapter
import com.example.managerlibrary.adapter.MemberLoanAdapter
import com.example.managerlibrary.dao.BookDAO
import com.example.managerlibrary.dao.CategoryBookDAO
import com.example.managerlibrary.dao.LibrarianDAO
import com.example.managerlibrary.dao.LibraryLoanSlipDAO
import com.example.managerlibrary.dao.MemberDAO
import com.example.managerlibrary.databinding.ActivityEditLoanBinding
import com.example.managerlibrary.dto.BookDTO
import com.example.managerlibrary.dto.CategoryBookDTO
import com.example.managerlibrary.dto.LibrarianDTO
import com.example.managerlibrary.dto.LibraryLoanSlipDTO
import com.example.managerlibrary.dto.MemberDTO
import com.example.managerlibrary.ui.MainActivity

class EditLoanActivity : AppCompatActivity() {
    lateinit var loanSlipDAO: LibraryLoanSlipDAO
    lateinit var loanSlipDTO: LibraryLoanSlipDTO
    lateinit var bookDAO: BookDAO
    lateinit var bookDTO: BookDTO
    lateinit var categoryBookDAO: CategoryBookDAO
    lateinit var categoryBookDTO: CategoryBookDTO
    lateinit var memberDAO: MemberDAO
    lateinit var memberDTO: MemberDTO
    lateinit var librarianDTO: LibrarianDTO
    lateinit var librarianDAO: LibrarianDAO

    lateinit var bookLoanAdapter: BookLoanAdapter
    lateinit var memberLoanAdapter: MemberLoanAdapter
    lateinit var librarianLoanAdapter: LibrarianLoanAdapter

    lateinit var listBook: ArrayList<BookDTO>
    lateinit var listMember: ArrayList<MemberDTO>
    lateinit var listLibrarian: ArrayList<LibrarianDTO>

    private lateinit var binding: ActivityEditLoanBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditLoanBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setSupportActionBar(binding.toolbarEditLoan)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)


        //get all member
        memberDAO = MemberDAO(this)
        listMember = memberDAO.getAllMember()

        //get all book
        bookDAO = BookDAO(this)
        listBook = bookDAO.getAllBook()

        //get all librarian
        librarianDAO = LibrarianDAO(this)
        listLibrarian = librarianDAO.getAllLibrarian()

        //set adapter for spinner
        bookLoanAdapter = BookLoanAdapter(this, listBook)
        memberLoanAdapter = MemberLoanAdapter(this, listMember)
        librarianLoanAdapter = LibrarianLoanAdapter(this, listLibrarian)

        //set adapter for spinner
        binding.spinnerEditLoanNameBook.adapter = bookLoanAdapter
        binding.spinnerEditLibrarianNameMember.adapter = librarianLoanAdapter
        binding.spinnerEditMemberBookMember.adapter = memberLoanAdapter

        //set event for spinner
        binding.spinnerEditLibrarianNameMember.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>?,
                    view: View,
                    position: Int,
                    l: Long
                ) {
                    librarianDTO = listLibrarian[position]
                    //set value for text view
                    binding.spinnerEditLibrarianNameMember.hint = "Tên thủ thư"
                }

                override fun onNothingSelected(adapterView: AdapterView<*>?) {}
            }

        //set event for spinner
        binding.spinnerEditMemberBookMember.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>?,
                    view: View,
                    position: Int,
                    l: Long
                ) {
                    memberDTO = listMember[position]
                    //set value for text view
                    binding.spinnerEditMemberBookMember.hint = "Tên thành viên"
                }

                override fun onNothingSelected(adapterView: AdapterView<*>?) {}
            }

        //set event for spinner
        binding.spinnerEditLoanNameBook.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>?,
                    view: View,
                    position: Int,
                    l: Long
                ) {
                    bookDTO = listBook[position]
                    //set value for text view
                    binding.spinnerEditLoanNameBook.hint = "Tên sách"
                }

                override fun onNothingSelected(adapterView: AdapterView<*>?) {}
            }

        binding.edtFromDateLoan.setOnClickListener() {
            // Show the date picker dialog
            val datePickerDialog = DatePickerDialog(this)
            datePickerDialog.setOnDateSetListener { view, year, month, dayOfMonth ->
                var endDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)
                if (endDate == "") {
                    Toast.makeText(this, "Chưa chọn ngày", Toast.LENGTH_SHORT).show()
                } else {
                    binding.edtFromDateLoan.setText(endDate)
                }
            }
            datePickerDialog.show()
        }
        val intent = intent

        // get data from database
        loanSlipDAO = LibraryLoanSlipDAO(this)
        val idLoanSlip = intent.getStringExtra("idLoanSlip")

        if (idLoanSlip != null) {
            loanSlipDAO = LibraryLoanSlipDAO(this)
            loanSlipDTO = loanSlipDAO.getLoanSlipByID(idLoanSlip.toInt())

            //get book by id
            bookDAO = BookDAO(this)
            bookDTO = bookDAO.getBookByID(loanSlipDTO.idBook)

            //get member by id
            memberDAO = MemberDAO(this)
            memberDTO = memberDAO.getMemberDTOById(loanSlipDTO.idMember)

            //get  librarian by id
            librarianDAO = LibrarianDAO(this)
            librarianDTO = librarianDAO.getLibrarianByID(loanSlipDTO.idLibrarian)

            //set value to spinner
            val selectedLibrarianPosition = listLibrarian.indexOf(librarianDTO)
            binding.spinnerEditLibrarianNameMember.setSelection(selectedLibrarianPosition)

            val selectedMemberPosition = listMember.indexOf(memberDTO)
            binding.spinnerEditMemberBookMember.setSelection(selectedMemberPosition)

            val selectedBookPosition = listBook.indexOf(bookDTO)
            binding.spinnerEditLoanNameBook.setSelection(selectedBookPosition)

            //set value to edit text
            binding.edtFromDateLoan.setText(loanSlipDTO.dateLoan)

            //set check to radio group
            if (loanSlipDTO.status == 1) {
                binding.radioGroupLoan.check(R.id.radio_return_loan)
            } else {
                binding.radioGroupLoan.check(R.id.radio_not_return_loan)
            }
        }
        binding.btnCancelEditLoan.setOnClickListener {
            finish()
        }

        binding.btnSaveEditLoan.setOnClickListener() {

            //get status
            var status = 0
            if (binding.radioReturnLoan.isChecked) {
                status = 1
            }

            //get date
            val date = binding.edtFromDateLoan.text.toString()

            //get id book
            val idBook = bookDTO.idBook

            //get id member
            val idMember = memberDTO.id

            //get id librarian
            val idLibrarian = librarianDTO.id

            loanSlipDAO = LibraryLoanSlipDAO(this)
            loanSlipDTO = LibraryLoanSlipDTO(
                idLoanSlip!!.toInt(),
                idBook,
                idLibrarian,
                idMember,
                date,
                status
            )

            val result = loanSlipDAO.updateLoanSlip(loanSlipDTO)

            if (result) {
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
                bindingSuccess.txtLoginSuccess.text = "Sửa thành công phiếu mượn!"
                bindingSuccess.btnLoginSuccess.setOnClickListener() {
                    //inent data to main activity
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("ok", "ok")
                    startActivity(intent)

                    Toast.makeText(this, "Edit success", Toast.LENGTH_SHORT).show()
                    finish()
                }
            } else {
                Toast.makeText(this, "Edit failed", Toast.LENGTH_SHORT).show()
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
