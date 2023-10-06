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
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ManagerCategoryBooksFragment : Fragment() {

    private var _binding: FragmentManagerCategoryBooksBinding? = null
    private val binding get() = _binding!!

    lateinit var adapter: CategoryBooksAdapter
    private lateinit var listCategoryBooks: ArrayList<CategoryBookDTO>
    private lateinit var categoryBookDAO: CategoryBookDAO
    private lateinit var sharedViewModel: SharedViewModel

    val dbCategory = Firebase.firestore

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

        listCategoryBooks = ArrayList()
//        dbCategory.collection("category books")
//            .get()
//            .addOnSuccessListener { result ->
//                for (document in result) {
//                    val category = CategoryBookDTO(
//                        document.data["id"].toString(),
//                        document.data["name"].toString()
//                    )
//                    listCategoryBooks.add(category)
//                }
//                adapter = CategoryBooksAdapter(requireContext(), listCategoryBooks)
//                binding.managerCategoryRecyclerView.adapter = adapter
//                adapter.notifyDataSetChanged()
//            }
//            .addOnFailureListener { exception ->
//                println("Error getting documents: $exception")
//            }


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

        // update when data change from firestore
        dbCategory.collection("category books")
            .addSnapshotListener { value, error ->
                if (error != null) {
                    return@addSnapshotListener
                }
                listCategoryBooks.clear()
                for (document in value!!) {
                    val category = CategoryBookDTO(
                        document.data["id"].toString(),
                        document.data["name"].toString()
                    )
                    listCategoryBooks.add(category)
                }

                adapter = CategoryBooksAdapter(requireContext(), listCategoryBooks)
                binding.managerCategoryRecyclerView.adapter = adapter
                adapter.notifyDataSetChanged()
            }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}