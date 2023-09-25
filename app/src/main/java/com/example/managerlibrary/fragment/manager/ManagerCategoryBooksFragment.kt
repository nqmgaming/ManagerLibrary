package com.example.managerlibrary.fragment.manager

import com.example.managerlibrary.adapter.CategoryBooksAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.managerlibrary.dao.CategoryBookDAO
import com.example.managerlibrary.databinding.FragmentManagerCategoryBooksBinding
import com.example.managerlibrary.dto.CategoryBookDTO

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class ManagerCategoryBooksFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentManagerCategoryBooksBinding? = null
    private val binding get() = _binding!!

    lateinit var adapter: CategoryBooksAdapter
    lateinit var listCategoryBooks: ArrayList<CategoryBookDTO>
    lateinit var categoryBookDAO: CategoryBookDAO

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
    ): View {
        _binding = FragmentManagerCategoryBooksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.managerCategoryRecyclerView.setHasFixedSize(true)
        binding.managerCategoryRecyclerView.layoutManager = LinearLayoutManager(context)
        categoryBookDAO = CategoryBookDAO(requireContext())
        listCategoryBooks = categoryBookDAO.getAllCategoryBooks()

        if (listCategoryBooks.isEmpty()) {
            binding.managerCategoryRecyclerView.visibility = View.GONE
        } else {
            adapter = CategoryBooksAdapter(requireContext(), listCategoryBooks)
            binding.managerCategoryRecyclerView.adapter = adapter
            binding.managerCategoryRecyclerView.visibility = View.VISIBLE
            adapter.notifyDataSetChanged()
        }

    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ManagerCategoryBooksFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}