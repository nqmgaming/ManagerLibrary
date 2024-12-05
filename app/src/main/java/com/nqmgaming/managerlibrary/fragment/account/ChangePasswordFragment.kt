package com.nqmgaming.managerlibrary.fragment.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.nqmgaming.managerlibrary.R
import com.nqmgaming.managerlibrary.dao.LibrarianDAO
import com.nqmgaming.managerlibrary.databinding.DialogLoginSuccessBinding
import com.nqmgaming.managerlibrary.databinding.DialogProccessingBinding
import com.nqmgaming.managerlibrary.databinding.FragmentChangePasswordBinding
import com.nqmgaming.managerlibrary.dto.LibrarianDTO
import com.nqmgaming.managerlibrary.sharepre.LoginSharePreference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChangePasswordFragment : Fragment() {

    private lateinit var userSharePreference: LoginSharePreference
    private lateinit var librarianDTO: LibrarianDTO
    private lateinit var librarianDAO: LibrarianDAO

    private var _binding: FragmentChangePasswordBinding? = null
    private val binding get() = _binding!!

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChangePasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userSharePreference = LoginSharePreference(requireContext())
        val password = userSharePreference.getPassword()
        val username = userSharePreference.getID()

        binding.btnConfirmChangePassword.setOnClickListener {
            val oldPassword = binding.edtOldPassword.text.toString().trim()
            if (oldPassword.isEmpty()) {
                binding.edtOldPassword.error = getString(R.string.txt_old_password_is_empty)
                return@setOnClickListener
            }
            if (oldPassword != password) {
                binding.edtOldPassword.error = getString(R.string.txt_old_password_is_incorrect)
                return@setOnClickListener
            }

            if (binding.linearlayoutOldPassword.visibility == View.VISIBLE) {
                binding.linearlayoutOldPassword.visibility = View.GONE
                binding.linearlayoutChangePassword.visibility = View.VISIBLE
                binding.btnConfirmChangePassword.visibility = View.GONE
                binding.tvChangePassword.text = getString(R.string.txt_set_new_password)
                binding.imgLock.setImageResource(R.drawable.user_profile)
            }
        }

        librarianDAO = LibrarianDAO(requireContext())
        librarianDTO = username?.let { librarianDAO.getLibrarianByID(it) }!!

        binding.btnConfirm.setOnClickListener {
            val newPassword = binding.edtNewPassword.text.toString().trim()
            val confirmPassword = binding.edtConfirmNewPassword.text.toString().trim()

            if (newPassword.isEmpty()) {
                binding.edtNewPassword.error = getString(R.string.txt_new_password_is_empty)
                return@setOnClickListener
            }
            if (confirmPassword.isEmpty()) {
                binding.edtConfirmNewPassword.error =
                    getString(R.string.txt_confirm_password_is_empty)
                return@setOnClickListener
            }
            if (newPassword != confirmPassword) {
                binding.edtConfirmNewPassword.error =
                    getString(R.string.txt_confirm_password_is_incorrect)
                return@setOnClickListener
            }

            coroutineScope.launch {
                val result = withContext(Dispatchers.IO) {
                    librarianDTO =
                        LibrarianDTO(username, librarianDTO.name, newPassword, librarianDTO.role)
                    librarianDAO.editLibrarian(librarianDTO)
                }

                if (result > 0) {
                    userSharePreference.saveLogin(librarianDTO)
                    showProcessingDialog(getString(R.string.txt_chaning_password))
                    delay(1000)
                    showSuccessDialog(getString(R.string.txt_change_password_success))
                } else {
                    showErrorDialog()
                }
            }
        }

        binding.btnConfirmCancel.setOnClickListener {
            resetPasswordForm()
        }
    }

    private fun resetPasswordForm() {
        binding.apply {
            linearlayoutOldPassword.visibility = View.VISIBLE
            linearlayoutChangePassword.visibility = View.GONE
            btnConfirmChangePassword.visibility = View.VISIBLE
            tvChangePassword.text = getString(R.string.txt_change_password)
            edtOldPassword.setText("")
            edtNewPassword.setText("")
            edtConfirmNewPassword.setText("")
            imgLock.setImageResource(R.drawable.ok)
        }
    }

    private fun showProcessingDialog(message: String) {
        val builder = AlertDialog.Builder(requireContext(), R.style.CustomDialog)
        val bindingDialogProcess = DialogProccessingBinding.inflate(layoutInflater)
        builder.setView(bindingDialogProcess.root)
        bindingDialogProcess.textViewLoading.text = message
        builder.setCancelable(false)
        val dialog = builder.create()
        dialog.show()
    }

    private fun showSuccessDialog(message: String) {
        val builderDialog = AlertDialog.Builder(requireContext(), R.style.CustomDialog)
        val bindingDialog = DialogLoginSuccessBinding.inflate(layoutInflater)
        builderDialog.setView(bindingDialog.root)
        val dialogLogin = builderDialog.create()
        bindingDialog.txtLoginSuccess.text = message
        bindingDialog.btnLoginSuccess.setOnClickListener {
            resetPasswordForm()
            dialogLogin.dismiss()
        }
        dialogLogin.show()
    }

    private fun showErrorDialog() {
        val builder = AlertDialog.Builder(requireContext(), R.style.CustomDialog)
        val bindingDialog = DialogLoginSuccessBinding.inflate(layoutInflater)
        builder.setView(bindingDialog.root)
        val dialogLogin = builder.create()
        bindingDialog.txtLoginSuccess.text = getString(R.string.txt_change_password_fail)
        bindingDialog.btnLoginSuccess.text = getString(R.string.txt_retry)
        bindingDialog.imgSuccess.setImageResource(R.drawable.error)
        bindingDialog.btnLoginSuccess.setOnClickListener {
            dialogLogin.dismiss()
        }
        dialogLogin.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        coroutineScope.cancel()
    }
}
