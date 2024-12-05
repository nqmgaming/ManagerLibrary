import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.nqmgaming.managerlibrary.R
import com.nqmgaming.managerlibrary.dao.LibrarianDAO
import com.nqmgaming.managerlibrary.databinding.ActivityLoginBinding
import com.nqmgaming.managerlibrary.databinding.DialogLoginSuccessBinding
import com.nqmgaming.managerlibrary.sharepre.LoginSharePreference
import com.nqmgaming.managerlibrary.ui.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var librarianDAO: LibrarianDAO
    private lateinit var loginSharePreference: LoginSharePreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loginSharePreference = LoginSharePreference(this)
        if (loginSharePreference.getRemember()) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLoginClick.setOnClickListener {
            binding.constraintLayout.visibility =
                if (binding.constraintLayout.visibility == android.view.View.VISIBLE) {
                    android.view.View.GONE
                } else {
                    android.view.View.VISIBLE
                }
            binding.btnLoginClick.visibility = android.view.View.GONE
        }

        binding.btnLogin.setOnClickListener {
            val username = binding.edtUsername.text.toString().trim()
            val password = binding.edtPassword.text.toString().trim()

            if (username.isEmpty()) {
                binding.edtUsername.error = "Nhập username"
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                binding.edtPassword.error = "Nhập mật khẩu"
                return@setOnClickListener
            }

            librarianDAO = LibrarianDAO(this)

            CoroutineScope(Dispatchers.Main).launch {
                val dialog = showProcessingDialog()

                val result = withContext(Dispatchers.IO) {
                    librarianDAO.getLibrarianUsernameByID(username)
                }

                if (result == -1) {
                    dialog.dismiss()
                    binding.edtUsername.error = "Username sai"
                    return@launch
                }

                val passwordCheck = withContext(Dispatchers.IO) {
                    librarianDAO.checkPasswordLibrarian(username, password)
                }

                if (passwordCheck == -1) {
                    dialog.dismiss()
                    binding.edtPassword.error = "Mật khẩu sai"
                    return@launch
                }

                val librarian = withContext(Dispatchers.IO) {
                    librarianDAO.getLibrarianByID(username)
                }

                loginSharePreference.saveLogin(librarian)
                loginSharePreference.isRemember(binding.cbRememberMe.isChecked)

                dialog.dismiss()
                showSuccessDialog()
            }
        }
    }

    private fun showProcessingDialog(): AlertDialog {
        val builder = AlertDialog.Builder(this, R.style.CustomDialog)
        builder.setView(layoutInflater.inflate(R.layout.dialog_proccessing, null))
        builder.setCancelable(false)
        val dialog = builder.create()
        dialog.show()
        return dialog
    }

    private fun showSuccessDialog() {
        val builderDialog = AlertDialog.Builder(this, R.style.CustomDialog)
        val bindingDialog = DialogLoginSuccessBinding.inflate(layoutInflater)
        builderDialog.setView(bindingDialog.root)
        val dialogLogin = builderDialog.create()

        bindingDialog.btnLoginSuccess.setOnClickListener {
            dialogLogin.dismiss()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        dialogLogin.show()
    }
}
