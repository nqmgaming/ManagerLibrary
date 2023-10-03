package com.example.managerlibrary.fragment.statistical

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.managerlibrary.dao.LibraryLoanSlipDAO
import com.example.managerlibrary.databinding.FragmentRevenueBinding



class RevenueFragment : Fragment() {

    private var revenue: Int = 0
    private var revenueDate: Int = 0
    private var startDate: String = ""
    private var endDate: String = ""

    private lateinit var loanSlipDAO: LibraryLoanSlipDAO
    private var _binding: FragmentRevenueBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRevenueBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loanSlipDAO = LibraryLoanSlipDAO(requireContext())
        revenue = loanSlipDAO.getRevenue()
        binding.tvTotalRevenue.text = "$revenue vnđ"

        binding.edtFromDate.setOnClickListener {
            // Show the date picker dialog
            val datePickerDialog = DatePickerDialog(requireContext())
            datePickerDialog.setOnDateSetListener { _, year, month, dayOfMonth ->
                startDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)
                if (startDate != "") {
                    binding.edtFromDate.setText(startDate)
                }
            }
            datePickerDialog.show()
        }

        binding.edtToDate.setOnClickListener {
            // Show the date picker dialog
            val datePickerDialog = DatePickerDialog(requireContext())
            datePickerDialog.setOnDateSetListener { _, year, month, dayOfMonth ->
                endDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)
                if (endDate != "") {
                    binding.edtToDate.setText(endDate)
                }
            }
            datePickerDialog.show()
        }

        binding.btnStatistical.setOnClickListener {

            if (startDate == "") {
                binding.tvErrorDate.visibility = View.VISIBLE
                binding.tvErrorDate.text = "*Chưa chọn ngày bắt đầu"
                return@setOnClickListener
            }

            if (endDate == "") {
                binding.tvErrorDate.visibility = View.VISIBLE
                binding.tvErrorDate.text = "*Chưa chọn ngày kết thúc"
                return@setOnClickListener
            }

            binding.tvErrorDate.visibility = View.GONE

            revenueDate = loanSlipDAO.getRevenueByDate(startDate, endDate)
            binding.tvTotalRevenueDate.text = "$revenueDate vnđ"
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}