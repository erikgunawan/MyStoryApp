package id.ergun.mystoryapp.presentation.ui.auth.register

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import dagger.hilt.android.AndroidEntryPoint
import id.ergun.mystoryapp.R
import id.ergun.mystoryapp.common.util.Helper.showToast
import id.ergun.mystoryapp.common.util.ResponseWrapper
import id.ergun.mystoryapp.data.remote.model.AuthRequest
import id.ergun.mystoryapp.databinding.ActivityRegisterBinding
import id.ergun.mystoryapp.presentation.viewmodel.RegisterViewModel

/**
 * @author erikgunawan
 * Created 01/10/22 at 21.10
 */
@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    private val viewModel by viewModels<RegisterViewModel>()

    private val loadingDialog by lazy {
        MaterialDialog(this)
            .title(R.string.loading)
            .customView(R.layout.loading_dialog)
            .cancelOnTouchOutside(false)
            .cancelable(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupObserve()
        setupListener()
    }

    private fun setupObserve() {
        viewModel.registerResponse.observe(this) {
            loadingDialog.dismiss()
            when (it.status) {
                ResponseWrapper.Status.SUCCESS -> {
                    if (it.data?.error == false) {
                        onRegisterSuccess(it.data.message)
                    }
                }
                ResponseWrapper.Status.ERROR -> {
                    it.message?.let { it1 -> showToast(it1) }
                }
                else -> {
                    // do something
                }
            }
        }
    }

    private fun setupListener() {
        binding.btnRegister.setOnClickListener {
            doRegister()
        }
    }

    private fun doRegister() {
        val name = binding.edRegisterName.text.toString()
        val email = binding.edRegisterEmail.text.toString()
        val password = binding.edRegisterPassword.text.toString()

        val nameError = binding.edRegisterName.error
        val emailError = binding.edRegisterEmail.error
        val passwordError = binding.edRegisterPassword.error

        if (name.isEmpty()) {
            showToast(getString(R.string.input_name_empty_error))
            return
        }

        if (email.isEmpty()) {
            showToast(getString(R.string.input_email_empty_error))
            return
        }

        if (password.isEmpty()) {
            showToast(getString(R.string.input_password_empty_error))
            return
        }

        if (nameError != null || emailError != null || passwordError != null) {
            showToast(getString(R.string.general_input_error))
            return
        }

        val request = AuthRequest(
            name, email, password
        )

        loadingDialog.show()
        viewModel.register(request)
    }

    private fun onRegisterSuccess(message: String) {
        if (message.isNotEmpty()) showToast(message)
        finish()
    }

    companion object {
        fun newIntent(context: Context): Intent =
            Intent(context, RegisterActivity::class.java).apply {
            }
    }
}