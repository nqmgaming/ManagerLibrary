package com.example.managerlibrary.fragment.account

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.managerlibrary.dao.LibrarianDAO
import com.example.managerlibrary.dto.LibrarianDTO
import com.example.managerlibrary.sharepre.LoginSharePreference
import com.example.managerlibrary.R
import com.example.managerlibrary.databinding.DialogLoginSuccessBinding
import com.example.managerlibrary.databinding.DialogProccessingBinding
import com.example.managerlibrary.databinding.FragmentAddNewUserBinding


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class AddNewUserFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentAddNewUserBinding? = null
    private val binding get() = _binding!!
    private var userSharePreference: LoginSharePreference? = null
    private var librarianDAO: LibrarianDAO? = null
    private var librarianDTO: LibrarianDTO? = null
    lateinit var role: String

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
        _binding = FragmentAddNewUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userSharePreference = LoginSharePreference(requireContext())
        val password = userSharePreference!!.getPassword()
        val username = userSharePreference!!.getID()
        val roleUser = userSharePreference!!.getRole()
        if (roleUser == "librarian") {
            Toast.makeText(
                requireContext(),
                "Bạn không có quyền thêm người dùng",
                Toast.LENGTH_SHORT
            ).show()

        } else {
            binding.imgAddUser.setImageResource(R.drawable.changepasswordlock)
            binding.txtAddUser.text = "Nhập mật khẩu để xác thực"
            binding.btnVerify.setOnClickListener() {
                Log.d("AAA", "onViewCreated: " + binding.edtPassword.text.toString().trim())
                Toast.makeText(requireContext(), password, Toast.LENGTH_SHORT).show()
                Toast.makeText(
                    requireContext(),
                    binding.edtPassword.text.toString().trim(),
                    Toast.LENGTH_SHORT
                ).show()
                if (binding.edtPasswordVerify.text.toString().trim() == password) {
                    binding.linearlayoutAddUser.visibility = View.VISIBLE
                    binding.linearVerify.visibility = View.GONE
                    binding.edtPasswordVerify.setText("")
                    binding.imgAddUser.setImageResource(R.drawable.adduser)
                    binding.txtAddUser.text = "Thêm người dùng"
                } else {
                    binding.edtPasswordVerify.error = "Mật khẩu không đúng"
                    return@setOnClickListener
                    Log.d("AAA", "onViewCreated: " + binding.edtPassword.text.toString().trim())
                }
            }
            binding.radioGroup.setOnCheckedChangeListener() { group, checkedId ->
                Toast.makeText(requireContext(), checkedId.toString(), Toast.LENGTH_SHORT).show()
                when (checkedId) {
                    R.id.radio_admin -> {
                        role = "admin"
                        Toast.makeText(requireContext(), role, Toast.LENGTH_SHORT).show()
                    }

                    R.id.radio_group -> {
                        role = "librarian"
                        Toast.makeText(requireContext(), role, Toast.LENGTH_SHORT).show()
                    }
                }
            }

            binding.btnCancelAddUser.setOnClickListener() {
                binding.linearlayoutAddUser.visibility = View.GONE
                binding.linearVerify.visibility = View.VISIBLE
                binding.imgAddUser.setImageResource(R.drawable.changepasswordlock)
                binding.txtAddUser.text = "Nhập mật khẩu để xác thực"
            }

            binding.btnSaveAddUser.setOnClickListener() {
                var usernameNew: String = binding.edtAddUser.text.toString().trim()
                var passwordNew: String = binding.edtPassword.text.toString().trim()
                val nameNew: String = binding.edtAddFullname.text.toString().trim()
                var passwordConfirm: String = binding.edtPasswordConfirm.text.toString().trim()

                if (usernameNew.isEmpty()) {
                    binding.edtAddUser.error = "Nhập username"
                    return@setOnClickListener
                }

                librarianDAO = LibrarianDAO(requireContext())
                if (librarianDAO!!.checkUsernameExist(usernameNew)) {
                    binding.edtAddUser.error = "Username đã tồn tại"
                    return@setOnClickListener
                }

                //username chỉ có chữ và số + _ và -
                val regex = Regex("^[a-zA-Z0-9_-]*\$")
                if (!regex.matches(usernameNew)) {
                    binding.edtAddUser.error = "Username chỉ có chữ và số + _ và -"
                    return@setOnClickListener
                }
                if (nameNew.isEmpty()) {
                    binding.edtAddFullname.error = "Nhập họ tên"
                    return@setOnClickListener
                }
                if (passwordNew.isEmpty()) {
                    binding.edtPassword.error = "Nhập mật khẩu"
                    return@setOnClickListener
                }
                if (passwordNew.length < 6) {
                    binding.edtPassword.error = "Mật khẩu phải lớn hơn 6 ký tự"
                    return@setOnClickListener
                }
                if (passwordConfirm.isEmpty()) {
                    binding.edtPasswordConfirm.error = "Nhập lại mật khẩu"
                    return@setOnClickListener
                }
                if (passwordNew != passwordConfirm) {
                    binding.edtPasswordConfirm.error = "Mật khẩu không trùng khớp"
                    return@setOnClickListener
                }

                librarianDTO = LibrarianDTO()
                librarianDTO!!.id = usernameNew
                librarianDTO!!.name = nameNew
                librarianDTO!!.password = passwordNew
                librarianDTO!!.role = role
                if (roleUser !== "admin") {
                    Toast.makeText(
                        requireContext(),
                        "Bạn không có quyền thêm người dùng",
                        Toast.LENGTH_SHORT
                    ).show()

                } else {

                    val result = librarianDAO!!.insertLibrarian(librarianDTO!!)

                    if (result > 0) {
                        val builder = AlertDialog.Builder(requireContext(), R.style.CustomDialog)
                        val bindingDialogProcess = DialogProccessingBinding.inflate(layoutInflater)
                        builder.setView(bindingDialogProcess.root)
                        bindingDialogProcess.textViewLoading.text = "Đang thêm người dùng"

                        builder.setCancelable(false)

                        val dialog = builder.create()

                        dialog.show()

                        Handler().postDelayed({
                            dialog.dismiss()
                            val builderDialog =
                                AlertDialog.Builder(requireContext(), R.style.CustomDialog)
                            val bindingDialog = DialogLoginSuccessBinding.inflate(layoutInflater)
                            builderDialog.setView(bindingDialog.root)
                            val dialogLogin = builderDialog.create()
                            bindingDialog.txtLoginSuccess.text = "Thêm người dùng thành công"
                            bindingDialog.btnLoginSuccess.setOnClickListener {
                                if (binding.linearlayoutAddUser.visibility == View.VISIBLE) {
                                    binding.linearlayoutAddUser.visibility = View.GONE
                                    binding.linearVerify.visibility = View.VISIBLE
                                    binding.imgAddUser.setImageResource(R.drawable.success)
                                    binding.txtAddUser.text = "Nhập mật khẩu để xác thực"
                                    binding.edtAddUser.setText("")
                                    binding.edtAddFullname.setText("")
                                    binding.edtPassword.setText("")
                                    binding.edtPasswordConfirm.setText("")
                                }
                                dialogLogin.dismiss()
                                //intent to main activity
                            }

                            dialogLogin.show()
                        }, 1000)

                    } else {
                        val builder = AlertDialog.Builder(requireContext(), R.style.CustomDialog)
                        val bindingDialog = DialogLoginSuccessBinding.inflate(layoutInflater)
                        builder.setView(bindingDialog.root)
                        val dialogLogin = builder.create()
                        bindingDialog.txtLoginSuccess.text = "Thêm người dùng thất bại"
                        bindingDialog.btnLoginSuccess.text = "Thử lại"
                        bindingDialog.imgSuccess.setImageResource(R.drawable.error)
                        bindingDialog.btnLoginSuccess.setOnClickListener {
                            dialogLogin.dismiss()
                            //intent to main activity
                        }

                        dialogLogin.show()
                    }
                }
            }
        }
    }


    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddNewUserFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}