package com.nqmgaming.managerlibrary.ui.manager

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.nqmgaming.managerlibrary.R
import com.nqmgaming.managerlibrary.adapter.BookLoanAdapter
import com.nqmgaming.managerlibrary.adapter.MemberLoanAdapter
import com.nqmgaming.managerlibrary.dao.BookDAO
import com.nqmgaming.managerlibrary.dao.LibraryLoanSlipDAO
import com.nqmgaming.managerlibrary.dao.MemberDAO
import com.nqmgaming.managerlibrary.databinding.ActivityAddLoanBinding
import com.nqmgaming.managerlibrary.dto.BookDTO
import com.nqmgaming.managerlibrary.dto.LibraryLoanSlipDTO
import com.nqmgaming.managerlibrary.dto.MemberDTO
import com.nqmgaming.managerlibrary.sharepre.LoginSharePreference
import com.nqmgaming.managerlibrary.ui.MainActivity
import java.text.SimpleDateFormat
import java.util.Calendar

class AddLoanActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddLoanBinding
    lateinit var listBooks: ArrayList<BookDTO>
    lateinit var listMembers: ArrayList<MemberDTO>
    lateinit var book: BookDTO
    lateinit var member: MemberDTO
    private lateinit var bookAdapter: BookLoanAdapter
    private lateinit var memberAdapter: MemberLoanAdapter
    private lateinit var memberDAO: MemberDAO
    private lateinit var bookDAO: BookDAO
    private lateinit var userSharePreferences: LoginSharePreference
    private lateinit var libraryLoanSlipDTO: LibraryLoanSlipDTO
    private lateinit var libraryLoanSlipDAO: LibraryLoanSlipDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddLoanBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setSupportActionBar(binding.toolbarAddLoan)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        //get all book
        bookDAO = BookDAO(this)
        listBooks = bookDAO.getAllBook()

        //get all member
        memberDAO = MemberDAO(this)
        listMembers = memberDAO.getAllMember()

        //set adapter for spinner
        bookAdapter = BookLoanAdapter(this, listBooks)
        memberAdapter = MemberLoanAdapter(this, listMembers)

        //set adapter for spinner
        binding.spinnerAddLoanNameBook.adapter = bookAdapter
        binding.spinnerAddLoanNameMember.adapter = memberAdapter

        //set event for spinner
        binding.spinnerAddLoanNameBook.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>?,
                    view: View,
                    position: Int,
                    l: Long
                ) {
                    book = listBooks[position]
                    //set value for text view
                    binding.spinnerAddLoanNameBook.hint = book.name
                }

                override fun onNothingSelected(adapterView: AdapterView<*>?) {}
            }

        //set event for spinner
        binding.spinnerAddLoanNameMember.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>?,
                    view: View,
                    position: Int,
                    l: Long
                ) {
                    member = listMembers[position]
                    //set value for text view
                    binding.spinnerAddLoanNameMember.hint = member.name
                }

                override fun onNothingSelected(adapterView: AdapterView<*>?) {}
            }

        binding.btnCancelAddLoan.setOnClickListener {
            finish()
        }

        binding.btnAddLoan.setOnClickListener {

            //if user  not choose book return
            if (binding.spinnerAddLoanNameBook.selectedItem == null) {
                binding.spinnerAddLoanNameBook.errorText = "Please choose book"
                Toast.makeText(this, "Please choose book", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else {
                binding.spinnerAddLoanNameBook.setErrorText("")
            }

            //if user  not choose member return
            if (binding.spinnerAddLoanNameMember.selectedItem == null) {
                binding.spinnerAddLoanNameMember.errorText = "Please choose member"
                Toast.makeText(this, "Please choose member", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else {
                binding.spinnerAddLoanNameMember.setErrorText("")
            }

            //get current date
            val currentDateTime = Calendar.getInstance().time
            val dateFormat = SimpleDateFormat("yyyy-MM-dd")
            val date = dateFormat.format(currentDateTime)

            //get id librarian
            userSharePreferences = LoginSharePreference(this)
            //check null after get id librarian
            val idLibrarian = userSharePreferences.getID() ?: ""


            //get id book
            val idBook = book.idBook

            //get id member
            val idMember = member.id

            libraryLoanSlipDTO = LibraryLoanSlipDTO(
                0,
                idBook,
                idLibrarian,
                idMember,
                date,
                0,
            )
            libraryLoanSlipDAO = LibraryLoanSlipDAO(this)
            val result = libraryLoanSlipDAO.insertLoanSlip(libraryLoanSlipDTO)

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
                bindingSuccess.txtLoginSuccess.text = "Thêm thành công phiếu mượn!"
                bindingSuccess.btnLoginSuccess.setOnClickListener {
                    //inent data to main activity
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("ok", "ok")
                    startActivity(intent)

                    finish()
                }
            } else {
                Toast.makeText(this, "Thêm phiếu mượn lỗi", Toast.LENGTH_SHORT).show()
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