package com.example.managerlibrary.fragment.manager

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.managerlibrary.adapter.CategoryBooksAdapter
import com.example.managerlibrary.dao.CategoryBookDAO
import com.example.managerlibrary.databinding.FragmentManagerCategoryBooksBinding
import com.example.managerlibrary.dto.CategoryBookDTO
import com.example.managerlibrary.ui.manager.AddCategoryBooksActivity
import com.example.managerlibrary.viewmodel.SharedViewModel

class ManagerCategoryBooksFragment : Fragment() {

    private var _binding: FragmentManagerCategoryBooksBinding? = null
    private val binding get() = _binding!!

    lateinit var adapter: CategoryBooksAdapter
    private lateinit var listCategoryBooks: ArrayList<CategoryBookDTO>
    private lateinit var categoryBookDAO: CategoryBookDAO
    private lateinit var sharedViewModel: SharedViewModel

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

        if (listCategoryBooks.isNotEmpty()) {
            adapter = CategoryBooksAdapter(requireContext(), listCategoryBooks)
            binding.managerCategoryRecyclerView.adapter = adapter
            binding.managerCategoryRecyclerView.visibility = View.VISIBLE
            adapter.notifyDataSetChanged()
        }

        val data = arguments?.getString("ok")
        if (data.equals("category")) {
            refreshList()
        }
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        sharedViewModel.searchText.observe(viewLifecycleOwner) { newText ->
            // Cập nhật giao diện hoặc thực hiện tìm kiếm với `newText`
            val filterList = ArrayList<CategoryBookDTO>()
            for (category in listCategoryBooks) {
                if (category.name.contains(newText, ignoreCase = true)) {
                    filterList.add(category)
                }
            }

            //update to adapter
            binding.managerCategoryRecyclerView.adapter =
                CategoryBooksAdapter(requireContext(), filterList)
            adapter.notifyDataSetChanged()

        }

        binding.fabAddCategory.setOnClickListener {
            Intent(requireContext(), AddCategoryBooksActivity::class.java).also {
                startActivity(it)
            }
        }

    }

    private fun refreshList() {
        listCategoryBooks.clear()
        listCategoryBooks = categoryBookDAO.getAllCategoryBooks()
        binding.managerCategoryRecyclerView.adapter =
            CategoryBooksAdapter(requireContext(), listCategoryBooks)
        adapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}