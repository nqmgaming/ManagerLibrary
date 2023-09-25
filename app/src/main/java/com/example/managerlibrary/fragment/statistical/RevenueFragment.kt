package com.example.managerlibrary.fragment.statistical

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.managerlibrary.dao.LibraryLoanSlipDAO
import com.example.managerlibrary.databinding.FragmentRevenueBinding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class RevenueFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var revenue: Int = 0
    private var revenueDate: Int = 0
    private var startDate: String = ""
    private var endDate: String = ""

    lateinit var loanSlipDAO: LibraryLoanSlipDAO
    private var _binding: FragmentRevenueBinding? = null
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
        _binding = FragmentRevenueBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loanSlipDAO = LibraryLoanSlipDAO(requireContext())
        revenue = loanSlipDAO.getRevenue()
        binding.tvTotalRevenue.text = revenue.toString() + "vnđ"

        binding.edtFromDate.setOnClickListener() {
            // Show the date picker dialog
            val datePickerDialog = DatePickerDialog(requireContext())
            datePickerDialog.setOnDateSetListener { view, year, month, dayOfMonth ->
                startDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)
                if (startDate == "") {
                    Toast.makeText(requireContext(), "Chưa chọn ngày", Toast.LENGTH_SHORT).show()
                } else {
                    binding.edtFromDate.setText(startDate)
                }
            }
            datePickerDialog.show()
        }

        binding.edtToDate.setOnClickListener() {
            // Show the date picker dialog
            val datePickerDialog = DatePickerDialog(requireContext())
            datePickerDialog.setOnDateSetListener { view, year, month, dayOfMonth ->
                endDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)
                if (endDate == "") {
                    Toast.makeText(requireContext(), "Chưa chọn ngày", Toast.LENGTH_SHORT).show()
                } else {
                    binding.edtToDate.setText(endDate)
                }
            }
            datePickerDialog.show()
        }

        binding.btnStatistical.setOnClickListener() {
            Toast.makeText(requireContext(), startDate + endDate, Toast.LENGTH_SHORT).show()

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
            binding.tvTotalRevenueDate.text = revenueDate.toString() + " vnđ"
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RevenueFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

}