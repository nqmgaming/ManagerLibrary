package com.example.managerlibrary.fragment.manager

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.managerlibrary.ui.manager.AddLoanActivity
import com.example.managerlibrary.adapter.BillsAdapter
import com.example.managerlibrary.dao.BookDAO
import com.example.managerlibrary.dao.LibraryLoanSlipDAO
import com.example.managerlibrary.databinding.FragmentManagerBillsBinding // Import the correct binding class
import com.example.managerlibrary.dto.BookDTO
import com.example.managerlibrary.dto.LibraryLoanSlipDTO
import com.example.managerlibrary.viewmodel.SharedViewModel

class ManagerBillsFragment : Fragment() {
    private lateinit var libraryLoanSlipDAO: LibraryLoanSlipDAO
    private lateinit var listLoanSlip: ArrayList<LibraryLoanSlipDTO>
    lateinit var adapter: BillsAdapter

    private var _binding: FragmentManagerBillsBinding? = null

    private lateinit var sharedViewModel: SharedViewModel

    private val binding get() = _binding!!
    lateinit var bookDTO: BookDTO
    lateinit var bookDAO: BookDAO
    lateinit var listBook: ArrayList<BookDTO>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentManagerBillsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.managerBillsRecyclerView.setHasFixedSize(true)
        binding.managerBillsRecyclerView.layoutManager = LinearLayoutManager(context)
        libraryLoanSlipDAO = LibraryLoanSlipDAO(requireContext())

        listLoanSlip = libraryLoanSlipDAO.getAllLoanSlip()

        if (listLoanSlip.isEmpty()) {
            Toast.makeText(requireContext(), "List is empty", Toast.LENGTH_SHORT).show()
        } else {
            adapter = BillsAdapter(requireContext(), listLoanSlip)
            binding.managerBillsRecyclerView.adapter = adapter
            adapter.notifyDataSetChanged()
        }

        val data = arguments?.getString("ok")
        if (data.equals("ok")){
            refreshList()
        }

        //get all book name from database
        val listBookName = ArrayList<Int>()
        for (i in 0 until listLoanSlip.size) {
            listBookName.add(listLoanSlip[i].idBook)
        }

        //get all book from database by id
        bookDAO = BookDAO(requireContext())
        listBook = ArrayList<BookDTO>()
        for (i in 0 until listBookName.size) {
            bookDTO = bookDAO.getBookByID(listBookName[i])
            listBook.add(bookDTO)
        }
        var filterList = ArrayList<LibraryLoanSlipDTO>()

        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        sharedViewModel.searchText.observe(viewLifecycleOwner, Observer { newText ->

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

        })

        binding.fabAddBill.setOnClickListener() {
            //intent to add bill
            Intent(requireContext(), AddLoanActivity::class.java).also {
                startActivity(it)
            }
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun updateData(newList: ArrayList<LibraryLoanSlipDTO>) {
        // Assuming your data list in the adapter is named `dataList`
        listLoanSlip.clear()
        listLoanSlip.addAll(newList)
    }
    fun refreshList() {
        listLoanSlip.clear()
        listLoanSlip.addAll(libraryLoanSlipDAO.getAllLoanSlip())
        adapter.notifyDataSetChanged()
    }



}