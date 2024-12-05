package com.nqmgaming.managerlibrary.fragment.statistical

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.nqmgaming.managerlibrary.adapter.Top10Adapter
import com.nqmgaming.managerlibrary.dao.LibraryLoanSlipDAO
import com.nqmgaming.managerlibrary.dao.MemberDAO
import com.nqmgaming.managerlibrary.databinding.FragmentTop10Binding
import com.nqmgaming.managerlibrary.dto.BookDTO
import com.nqmgaming.managerlibrary.viewmodel.SharedViewModel



class Top10Fragment : Fragment() {


    lateinit var bookDAO: MemberDAO
    private lateinit var loanSlipDAO: LibraryLoanSlipDAO
    private lateinit var listBook: ArrayList<BookDTO>
    lateinit var adapter: Top10Adapter
    private lateinit var sharedViewModel: SharedViewModel

    private var _binding: FragmentTop10Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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

        if (listBook.isNotEmpty()) {
            adapter = Top10Adapter(requireContext(), listBook)
            binding.reTopRecyclerView.adapter = adapter
            adapter.notifyDataSetChanged()
        }

        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        sharedViewModel.searchText.observe(viewLifecycleOwner) { newText ->
            // Cập nhật giao diện hoặc thực hiện tìm kiếm với `newText`
            val filterList = ArrayList<BookDTO>()
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}