package com.example.managerlibrary.fragment.statistical

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.managerlibrary.dto.BookDTO
import com.example.managerlibrary.dao.LibraryLoanSlipDAO
import com.example.managerlibrary.dao.MemberDAO
import com.example.managerlibrary.adapter.Top10Adapter
import com.example.managerlibrary.databinding.FragmentTop10Binding
import com.example.managerlibrary.ui.MainActivity
import com.example.managerlibrary.viewmodel.SharedViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class Top10Fragment : Fragment(), MainActivity.SearchListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var bookDAO: MemberDAO
    lateinit var loanSlipDAO: LibraryLoanSlipDAO
    lateinit var listBook: ArrayList<BookDTO>
    lateinit var adapter: Top10Adapter
    private lateinit var sharedViewModel: SharedViewModel

    private var _binding: FragmentTop10Binding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTop10Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.reTopRecyclerView.setHasFixedSize(true)
        binding.reTopRecyclerView.layoutManager = LinearLayoutManager(context)
        bookDAO = MemberDAO(requireContext())
        loanSlipDAO = LibraryLoanSlipDAO(requireContext())
        listBook = loanSlipDAO.getTop10Book()

        if (listBook.isEmpty()) {
            Toast.makeText(requireContext(), "List is empty", Toast.LENGTH_SHORT).show()
        } else {
            adapter = Top10Adapter(requireContext(), listBook)
            binding.reTopRecyclerView.adapter = adapter
            adapter.notifyDataSetChanged()
        }

        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        sharedViewModel.searchText.observe(viewLifecycleOwner, Observer { newText ->
            // Cập nhật giao diện hoặc thực hiện tìm kiếm với `newText`
            var filterList = ArrayList<BookDTO>()
            for (book in listBook) {
                if (book.name.contains(newText, ignoreCase = true)) {
                    filterList.add(book)
                }
            }

            //update to adapter
            binding.reTopRecyclerView.adapter = Top10Adapter(requireContext(), filterList)
            adapter.notifyDataSetChanged()

        })
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Top10Fragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


    override fun onQueryTextSubmit(query: String) {
        var filterList = ArrayList<BookDTO>()
        for (book in listBook) {
            if (book.name.contains(query, ignoreCase = true)) {
                filterList.add(book)
            }
        }

        //update to adapter
        binding.reTopRecyclerView.adapter = Top10Adapter(requireContext(), filterList)
        adapter.notifyDataSetChanged()

    }

    override fun onQueryTextChange(newText: String) {
        var filterList = ArrayList<BookDTO>()
        for (book in listBook) {
            if (book.name.contains(newText, ignoreCase = true)) {
                filterList.add(book)
            }
        }

        //update to adapter
        binding.reTopRecyclerView.adapter = Top10Adapter(requireContext(), filterList)
        adapter.notifyDataSetChanged()
    }
}