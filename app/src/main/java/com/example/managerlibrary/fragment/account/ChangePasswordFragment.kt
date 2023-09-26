package com.example.managerlibrary.fragment.account

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.example.managerlibrary.dao.LibrarianDAO
import com.example.managerlibrary.dto.LibrarianDTO
import com.example.managerlibrary.sharepre.LoginSharePreference
import com.example.managerlibrary.R
import com.example.managerlibrary.databinding.DialogLoginSuccessBinding
import com.example.managerlibrary.databinding.DialogProccessingBinding
import com.example.managerlibrary.databinding.FragmentChangePasswordBinding


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class ChangePasswordFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    lateinit var userSharePreference: LoginSharePreference
    lateinit var librarianDTO: LibrarianDTO
    lateinit var librarianDAO: LibrarianDAO

    private var _binding: FragmentChangePasswordBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChangePasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userSharePreference = LoginSharePreference(requireContext())
        val password = userSharePreference.getPassword()
        val username = userSharePreference.getID()

        binding.btnConfirmChangePassword.setOnClickListener() {
            val oldPassword = binding.edtOldPassword.text.toString().trim()
            if (oldPassword.isEmpty()) {
                binding.edtOldPassword.error = "Old password is empty"
                return@setOnClickListener
            }
            if (oldPassword != password) {
                binding.edtOldPassword.error = "Old password is incorrect"
                return@setOnClickListener
            }

            if (binding.linearlayoutOldPassword.visibility == View.VISIBLE) {
                binding.linearlayoutOldPassword.visibility = View.GONE
                binding.linearlayoutChangePassword.visibility = View.VISIBLE
                binding.btnConfirmChangePassword.visibility = View.GONE
                binding.tvChangePassword.text = "Đặt mật khẩu mới"
                //thay ảnh
                binding.imgLock.setImageResource(R.drawable.user_profile)
            }

        }

        librarianDAO = LibrarianDAO(requireContext())
        librarianDTO = username?.let { librarianDAO.getLibrarianByID(it) }!!
        binding.btnConfirm.setOnClickListener() {
            val newPassword = binding.edtNewPassword.text.toString().trim()
            val confirmPassword = binding.edtConfirmNewPassword.text.toString().trim()
            if (newPassword.isEmpty()) {
                binding.edtNewPassword.error = "New password is empty"
                return@setOnClickListener
            }
            if (confirmPassword.isEmpty()) {
                binding.edtConfirmNewPassword.error = "Confirm password is empty"
                return@setOnClickListener
            }
            if (newPassword != confirmPassword) {
                binding.edtConfirmNewPassword.error = "Confirm password is incorrect"
                return@setOnClickListener
            }
            librarianDTO = LibrarianDTO(username, librarianDTO.name, newPassword, librarianDTO.role)
            val result = librarianDAO.editLibrarian(librarianDTO)
            if (result > 0) {
                userSharePreference.saveLogin(librarianDTO)

                val builder = AlertDialog.Builder(requireContext(), R.style.CustomDialog)
                val bindingDialogProcess = DialogProccessingBinding.inflate(layoutInflater)
                builder.setView(bindingDialogProcess.root)
                bindingDialogProcess.textViewLoading.text = "Đang đổi mật khẩu"

                builder.setCancelable(false)

                val dialog = builder.create()

                dialog.show()

                Handler().postDelayed({
                    dialog.dismiss()
                    val builderDialog = AlertDialog.Builder(requireContext(), R.style.CustomDialog)
                    val bindingDialog = DialogLoginSuccessBinding.inflate(layoutInflater)
                    builderDialog.setView(bindingDialog.root)
                    val dialogLogin = builderDialog.create()
                    bindingDialog.txtLoginSuccess.text = "Đổi mật khẩu thành công"
                    bindingDialog.btnLoginSuccess.setOnClickListener {
                        if (binding.linearlayoutOldPassword.visibility == View.GONE) {
                            binding.linearlayoutOldPassword.visibility = View.VISIBLE
                            binding.linearlayoutChangePassword.visibility = View.GONE
                            binding.btnConfirmChangePassword.visibility = View.VISIBLE
                            binding.tvChangePassword.text = "Đổi mật khẩu"
                            binding.edtOldPassword.setText("")
                            binding.edtNewPassword.setText("")
                            binding.edtConfirmNewPassword.setText("")
                            binding.imgLock.setImageResource(R.drawable.ok)
                        }
                        dialogLogin.dismiss()
                        //intent to main activity
                    }

                    dialogLogin.show()
                }, 1000)
            } else {

                //dialog custom
                val builder = AlertDialog.Builder(requireContext(), R.style.CustomDialog)
                val bindingDialog = DialogLoginSuccessBinding.inflate(layoutInflater)
                builder.setView(bindingDialog.root)
                val dialogLogin = builder.create()
                bindingDialog.txtLoginSuccess.text = "Đổi mật khẩu thất bại"
                bindingDialog.btnLoginSuccess.text = "Thử lại"
                bindingDialog.imgSuccess.setImageResource(R.drawable.error)
                bindingDialog.btnLoginSuccess.setOnClickListener {
                    dialogLogin.dismiss()

                }

                binding.edtNewPassword.error = "Edit failed"
            }
        }


    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) = ChangePasswordFragment().apply {
            arguments = Bundle().apply {
                putString(ARG_PARAM1, param1)
                putString(ARG_PARAM2, param2)
            }
        }
    }
}