package com.example.managerlibrary.fragment.manager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.managerlibrary.dao.BookDAO
import com.example.managerlibrary.dto.BookDTO
import com.example.managerlibrary.adapter.BooksAdapter
import com.example.managerlibrary.adapter.Top10Adapter
import com.example.managerlibrary.databinding.FragmentManagerBooksBinding
import com.example.managerlibrary.viewmodel.SharedViewModel

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class ManagerBooksFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var bookDAO: BookDAO
    lateinit var listBook: ArrayList<BookDTO>
    lateinit var adapter: BooksAdapter
    private lateinit var sharedViewModel: SharedViewModel

    private var _binding: FragmentManagerBooksBinding? = null
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
        _binding = FragmentManagerBooksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.managerBooksRecyclerView.setHasFixedSize(true)
        binding.managerBooksRecyclerView.layoutManager = LinearLayoutManager(context)
        bookDAO = BookDAO(requireContext())

        listBook   = bookDAO.getAllBook()

        if (listBook.isEmpty()) {
            Toast.makeText(requireContext(), "List is empty", Toast.LENGTH_SHORT).show()
        } else {
            adapter = BooksAdapter(requireContext(), listBook)
            binding.managerBooksRecyclerView.adapter = adapter
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
            binding.managerBooksRecyclerView.adapter = BooksAdapter(requireContext(), filterList)
            adapter.notifyDataSetChanged()

        })
    }
    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ManagerBooksFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}