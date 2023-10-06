package com.example.managerlibrary.fragment.manager

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.managerlibrary.adapter.BillsAdapter
import com.example.managerlibrary.dao.BookDAO
import com.example.managerlibrary.dao.LibraryLoanSlipDAO
import com.example.managerlibrary.databinding.FragmentManagerBillsBinding
import com.example.managerlibrary.dto.BookDTO
import com.example.managerlibrary.dto.LibraryLoanSlipDTO
import com.example.managerlibrary.ui.manager.AddLoanActivity
import com.example.managerlibrary.viewmodel.SharedViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ManagerBillsFragment : Fragment() {
    private lateinit var libraryLoanSlipDAO: LibraryLoanSlipDAO
    private lateinit var listLoanSlip: ArrayList<LibraryLoanSlipDTO>
    lateinit var adapter: BillsAdapter

    private var _binding: FragmentManagerBillsBinding? = null

    private lateinit var sharedViewModel: SharedViewModel

    private val binding get() = _binding!!
    private lateinit var bookDTO: BookDTO
    lateinit var bookDAO: BookDAO
    private lateinit var listBook: ArrayList<BookDTO>
    val dbLoan = Firebase.firestore
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentManagerBillsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.managerBillsRecyclerView.setHasFixedSize(true)
        binding.managerBillsRecyclerView.layoutManager = LinearLayoutManager(context)
        libraryLoanSlipDAO = LibraryLoanSlipDAO(requireContext())

        listLoanSlip = ArrayList()
//        dbLoan.collection("loanSlip")
//            .get()
//            .addOnSuccessListener { result ->
//                for (document in result) {
//                    val loanSlip = LibraryLoanSlipDTO(
//                        document.data["id"].toString(),
//                        document.data["idBook"].toString().toInt(),
//                        document.data["idLibrarian"].toString(),
//                        document.data["idMember"].toString().toInt(),
//                        document.data["dateLoan"].toString(),
//                        document.data["status"].toString().toInt()
//                    )
//                    listLoanSlip.add(loanSlip)
//                }
//
//                Log.d("TAGGG", listLoanSlip.toString())
//                adapter = BillsAdapter(requireActivity(), listLoanSlip)
//                binding.managerBillsRecyclerView.adapter = adapter
//                adapter.notifyDataSetChanged()
//
//                // Đặt các lệnh khác tương tác với listLoanSlip ở đây
//            }



        //get all book name from database
        val listBookName = ArrayList<Int>()
        for (i in 0 until listLoanSlip.size) {
            listBookName.add(listLoanSlip[i].idBook)
        }

        //get all book from database by id
        bookDAO = BookDAO(requireContext())
        listBook = ArrayList()
        for (i in 0 until listBookName.size) {
            bookDTO = bookDAO.getBookByID(listBookName[i])
            listBook.add(bookDTO)
        }
        val filterList = ArrayList<LibraryLoanSlipDTO>()

        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        sharedViewModel.searchText.observe(viewLifecycleOwner) { newText ->

            listBookName.clear()
            filterList.clear()

            for (book in listBook) {
                if (book.name.contains(newText, ignoreCase = true)) {
                    listBookName.add(book.idBook)
                }
            }
            //get all loan slip by book id

            for (i in 0 until listBookName.size) {
                filterList.add(libraryLoanSlipDAO.getLibrarianByIDBook(listBookName[i]))
            }

            //update to adapter
            updateData(filterList)
            adapter.notifyDataSetChanged()

        }

        binding.fabAddBill.setOnClickListener {
            //intent to add bill
            Intent(requireContext(), AddLoanActivity::class.java).also {
                startActivity(it)
            }
        }

        //update when data change from firestore
        dbLoan.collection("loanSlip")
            .addSnapshotListener { value, error ->
                if (error != null) {
                    return@addSnapshotListener
                }
                listLoanSlip.clear()
                for (document in value!!) {
                    val loanSlip = LibraryLoanSlipDTO(
                        document.data["id"].toString(),
                        document.data["idBook"].toString().toInt(),
                        document.data["idLibrarian"].toString(),
                        document.data["idMember"].toString().toInt(),
                        document.data["dateLoan"].toString(),
                        document.data["status"].toString().toInt()
                    )
                    listLoanSlip.add(loanSlip)
                }
                // Đặt các lệnh khác tương tác với listLoanSlip ở đây
                adapter = BillsAdapter(requireActivity(), listLoanSlip)
                binding.managerBillsRecyclerView.adapter = adapter
                adapter.notifyDataSetChanged()
            }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateData(newList: ArrayList<LibraryLoanSlipDTO>) {
        // Assuming your data list in the adapter is named `dataList`
        listLoanSlip.clear()
        listLoanSlip.addAll(newList)
    }



    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}