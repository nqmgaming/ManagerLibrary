package com.example.managerlibrary.fragment.account

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.managerlibrary.dao.LibraryLoanSlipDAO
import com.example.managerlibrary.sharepre.LoginSharePreference
import com.example.managerlibrary.R
import com.example.managerlibrary.databinding.FragmentProfileBinding
import com.example.managerlibrary.ui.account.EditAccountActivity


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class ProfileFragment : Fragment() {
    var param1: String? = null
    private var param2: String? = null
    lateinit var userSharePreference: LoginSharePreference
    private var _binding: FragmentProfileBinding? = null
    lateinit var libraryLoanSlipDAO: LibraryLoanSlipDAO
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
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.cardEditProfile.setOnClickListener() {
            Intent(requireContext(), EditAccountActivity::class.java).also {
                startActivity(it)
            }
        }


        userSharePreference = LoginSharePreference(requireContext())
        val username = userSharePreference.getID()
        val role = userSharePreference.getRole()
        val fullname = userSharePreference.getName()

        libraryLoanSlipDAO = LibraryLoanSlipDAO(requireContext())

        binding.tvUsernameProfile.text = username
        binding.tvNameProfile.text = fullname
        binding.tvRoleProfile.text = "Người dùng: " + role
        binding.tvTotalBorrowedProfile.text = "Phiếu mượn đã tạo: " + username?.let {
            libraryLoanSlipDAO.getNumberOfLoanSlipByID(
                it
            ).toString()
        }

        binding.cardFeature.setOnClickListener() {
            //intent to fragment change password
            val changePasswordFragment = ChangePasswordFragment()
            val fragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.nav_host_fragment, changePasswordFragment)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()

        }
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun updateData() {
        userSharePreference = LoginSharePreference(requireContext())
        val username = userSharePreference.getID()
        val role = userSharePreference.getRole()
        val fullname = userSharePreference.getName()

        libraryLoanSlipDAO = LibraryLoanSlipDAO(requireContext())

        binding.tvUsernameProfile.text = username
        binding.tvNameProfile.text = fullname
        binding.tvRoleProfile.text = "Người dùng: " + role
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