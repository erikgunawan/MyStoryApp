package id.ergun.mystoryapp.presentation.ui.auth.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import id.ergun.mystoryapp.data.remote.model.AuthRequest
import id.ergun.mystoryapp.databinding.ActivityLoginBinding
import id.ergun.mystoryapp.presentation.viewmodel.LoginViewModel
import timber.log.Timber

/**
 * @author erikgunawan
 * Created 01/10/22 at 21.10
 */
@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupObserve()
        setupListener()
    }

    private fun setupObserve() {
        viewModel.loginResponse.observe(this) {
            Timber.d(Gson().toJson(it))
        }
    }

    private fun setupListener() {
        binding.btnLogin.setOnClickListener {
            dologin()
        }
    }

    private fun dologin() {
        val email = binding.edLoginEmail.text.toString()
        val password = binding.edLoginPassword.text.toString()
        val request = AuthRequest(
            null, email, password
        )
        viewModel.login(request)
    }


    companion object {
        fun newIntent(context: Context): Intent =
            Intent(context, LoginActivity::class.java).apply {
            }
    }
}