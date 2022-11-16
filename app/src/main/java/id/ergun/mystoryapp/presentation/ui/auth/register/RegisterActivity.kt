package id.ergun.mystoryapp.presentation.ui.auth.register

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupObserve()
        setupListener()
    }

    private fun setupObserve() {
        viewModel.registerResponse.observe(this) {
            when (it.status) {
                ResponseWrapper.Status.SUCCESS -> {
                    if (it.data?.error == false) {
                        onRegisterSuccess(it.data.message)
                    }
                }
                else -> {

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
        val request = AuthRequest(
            name, email, password
        )
        viewModel.register(request)
    }

    private fun onRegisterSuccess(message: String) {
        if (message.isNotEmpty())
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

        finish()
    }

    companion object {
        fun newIntent(context: Context): Intent =
            Intent(context, RegisterActivity::class.java).apply {
            }
    }
}