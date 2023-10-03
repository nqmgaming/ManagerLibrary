package com.example.managerlibrary.fragment.account

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.managerlibrary.R
import com.example.managerlibrary.dao.LibraryLoanSlipDAO
import com.example.managerlibrary.databinding.FragmentProfileBinding
import com.example.managerlibrary.sharepre.LoginSharePreference
import com.example.managerlibrary.ui.account.EditAccountActivity


class ProfileFragment : Fragment() {

    private lateinit var userSharePreference: LoginSharePreference
    private var _binding: FragmentProfileBinding? = null
    private lateinit var libraryLoanSlipDAO: LibraryLoanSlipDAO
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.cardEditProfile.setOnClickListener {
            Intent(requireContext(), EditAccountActivity::class.java).also {
                startActivity(it)
            }
        }


        userSharePreference = LoginSharePreference(requireContext())
        val username = userSharePreference.getID()
        val role = userSharePreference.getRole()
        val fullname = userSharePreference.getName()

        libraryLoanSlipDAO = LibraryLoanSlipDAO(requireContext())

        binding.tvUsernameProfile.text = "@$username"
        binding.tvNameProfile.text = fullname
        binding.tvRoleProfile.text = "Người dùng: $role"
        binding.tvTotalBorrowedProfile.text = "Phiếu mượn đã tạo: " + username?.let {
            libraryLoanSlipDAO.getNumberOfLoanSlipByID(
                it
            ).toString()
        }

        binding.cardFeature.setOnClickListener {
            //intent to fragment change password
            val changePasswordFragment = ChangePasswordFragment()
            val fragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.nav_host_fragment, changePasswordFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()

        }
    }


    private fun updateData() {
        userSharePreference = LoginSharePreference(requireContext())
        val username = userSharePreference.getID()
        val role = userSharePreference.getRole()
        val fullname = userSharePreference.getName()

        libraryLoanSlipDAO = LibraryLoanSlipDAO(requireContext())

        binding.tvUsernameProfile.text = "@$username"
        binding.tvNameProfile.text = fullname
        binding.tvRoleProfile.text = "Người dùng: $role"
        binding.tvTotalBorrowedProfile.text = "Phiếu mượn đã tạo: " + username?.let {
            libraryLoanSlipDAO.getNumberOfLoanSlipByID(
                it
            ).toString()
        }
    }


    //on resume update data
    override fun onResume() {
        super.onResume()
        updateData()
    }
}