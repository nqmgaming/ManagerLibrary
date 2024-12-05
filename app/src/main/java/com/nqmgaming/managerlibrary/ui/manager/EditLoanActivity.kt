package com.nqmgaming.managerlibrary.ui.manager

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.nqmgaming.managerlibrary.R
import com.nqmgaming.managerlibrary.adapter.BookLoanAdapter
import com.nqmgaming.managerlibrary.adapter.LibrarianLoanAdapter
import com.nqmgaming.managerlibrary.adapter.MemberLoanAdapter
import com.nqmgaming.managerlibrary.dao.BookDAO
import com.nqmgaming.managerlibrary.dao.LibrarianDAO
import com.nqmgaming.managerlibrary.dao.LibraryLoanSlipDAO
import com.nqmgaming.managerlibrary.dao.MemberDAO
import com.nqmgaming.managerlibrary.databinding.ActivityEditLoanBinding
import com.nqmgaming.managerlibrary.dto.BookDTO
import com.nqmgaming.managerlibrary.dto.LibrarianDTO
import com.nqmgaming.managerlibrary.dto.LibraryLoanSlipDTO
import com.nqmgaming.managerlibrary.dto.MemberDTO
import com.nqmgaming.managerlibrary.ui.MainActivity

class EditLoanActivity : AppCompatActivity() {
    private lateinit var loanSlipDAO: LibraryLoanSlipDAO
    private lateinit var loanSlipDTO: LibraryLoanSlipDTO
    lateinit var bookDAO: BookDAO
    lateinit var bookDTO: BookDTO
    private lateinit var memberDAO: MemberDAO
    lateinit var memberDTO: MemberDTO
    lateinit var librarianDTO: LibrarianDTO
    private lateinit var librarianDAO: LibrarianDAO

    private lateinit var bookLoanAdapter: BookLoanAdapter
    private lateinit var memberLoanAdapter: MemberLoanAdapter
    private lateinit var librarianLoanAdapter: LibrarianLoanAdapter

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

        binding.edtFromDateLoan.setOnClickListener {
            // Show the date picker dialog
            val datePickerDialog = DatePickerDialog(this)
            datePickerDialog.setOnDateSetListener { _, year, month, dayOfMonth ->
                val endDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)
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

        binding.btnSaveEditLoan.setOnClickListener {

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
                    com.nqmgaming.managerlibrary.databinding.DialogLoginSuccessBinding.inflate(
                        layoutInflater
                    )
                builderSuccess.setView(bindingSuccess.root)
                val dialogSuccess = builderSuccess.create()
                dialogSuccess.show()
                dialogSuccess.setCancelable(false)
                bindingSuccess.txtLoginSuccess.text = "Sửa thành công phiếu mượn!"
                bindingSuccess.btnLoginSuccess.setOnClickListener {
                    //inent data to main activity
                    val intentMain = Intent(this, MainActivity::class.java)
                    intentMain.putExtra("ok", "ok")
                    startActivity(intentMain)
                    finish()
                }
            } else {
                Toast.makeText(this, "Sửa lỗi", Toast.LENGTH_SHORT).show()
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
