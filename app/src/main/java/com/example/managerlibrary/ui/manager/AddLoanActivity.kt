package com.example.managerlibrary.ui.manager

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import com.example.managerlibrary.adapter.BillsAdapter
import com.example.managerlibrary.adapter.BookLoanAdapter
import com.example.managerlibrary.adapter.MemberLoanAdapter
import com.example.managerlibrary.dao.BookDAO
import com.example.managerlibrary.dao.LibraryLoanSlipDAO
import com.example.managerlibrary.dao.MemberDAO
import com.example.managerlibrary.databinding.ActivityAddLoanBinding
import com.example.managerlibrary.dto.BookDTO
import com.example.managerlibrary.dto.LibraryLoanSlipDTO
import com.example.managerlibrary.dto.MemberDTO
import com.example.managerlibrary.fragment.manager.ManagerBillsFragment
import com.example.managerlibrary.sharepre.LoginSharePreference
import com.example.managerlibrary.ui.MainActivity
import java.util.ArrayList

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
    lateinit var libraryLoanSlipDTO: LibraryLoanSlipDTO
    lateinit var libraryLoanSlipDAO: LibraryLoanSlipDAO
    lateinit var billAdapter: BillsAdapter

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

        binding.btnAddLoan.setOnClickListener() {

            //if user  not choose book return
            if (binding.spinnerAddLoanNameBook.selectedItem == null) {
                binding.spinnerAddLoanNameBook.setErrorText("Please choose book")
                Toast.makeText(this, "Please choose book", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }else{
                binding.spinnerAddLoanNameBook.setErrorText("")
            }

            //if user  not choose member return
            if (binding.spinnerAddLoanNameMember.selectedItem == null) {
                binding.spinnerAddLoanNameMember.setErrorText("Please choose member")
                Toast.makeText(this, "Please choose member", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }else{
                binding.spinnerAddLoanNameMember.setErrorText("")
            }

            //get current date
            val currentDateTime = java.util.Calendar.getInstance().time
            val dateFormat = java.text.SimpleDateFormat("yyyy-MM-dd")
            val date = dateFormat.format(currentDateTime)

            //get id librarian
            userSharePreferences = LoginSharePreference(this)
            //check null after get id librarian
            val idLibrarian = userSharePreferences.getID() ?: ""


            //get id book
            val idBook = book.idBook

            //get id member
            val idMember = member.id

            //get rental fee
            val rentalFee = book.rentalFee

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
               //inent data to main activity
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("ok", "ok")
                startActivity(intent)

                finish()
            } else {
                Toast.makeText(this, "Add loan slip fail", Toast.LENGTH_SHORT).show()
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