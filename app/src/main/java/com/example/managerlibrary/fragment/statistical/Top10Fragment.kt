package com.example.managerlibrary.fragment.statistical

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.managerlibrary.BookDTO
import com.example.managerlibrary.LibraryLoanSlipDAO
import com.example.managerlibrary.MemberDAO
import com.example.managerlibrary.R
import com.example.managerlibrary.Top10Adapter
import com.example.managerlibrary.databinding.FragmentTop10Binding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class Top10Fragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var bookDAO: MemberDAO
    lateinit var loanSlipDAO: LibraryLoanSlipDAO
    lateinit var listBook: ArrayList<BookDTO>
    lateinit var adapter: Top10Adapter

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
}