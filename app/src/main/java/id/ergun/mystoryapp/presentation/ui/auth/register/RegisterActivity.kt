package id.ergun.mystoryapp.presentation.ui.auth.register

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import id.ergun.mystoryapp.data.remote.model.AuthRequest
import id.ergun.mystoryapp.databinding.ActivityRegisterBinding
import id.ergun.mystoryapp.presentation.viewmodel.RegisterViewModel
import timber.log.Timber

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
            Timber.d(Gson().toJson(it))
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

    companion object {
        fun newIntent(context: Context): Intent =
            Intent(context, RegisterActivity::class.java).apply {
            }
    }
}