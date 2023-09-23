package com.example.managerlibrary.fragment.manager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.managerlibrary.BillsAdapter
import com.example.managerlibrary.LibraryLoanSlipDAO
import com.example.managerlibrary.databinding.FragmentManagerBillsBinding // Import the correct binding class
import com.example.managerlibrary.LibraryLoanSlipDTO

class ManagerBillsFragment : Fragment() {
    private lateinit var libraryLoanSlipDAO: LibraryLoanSlipDAO
    private lateinit var listLoanSlip: ArrayList<LibraryLoanSlipDTO>
    lateinit var adapter: BillsAdapter

    private var _binding: FragmentManagerBillsBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

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




    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}