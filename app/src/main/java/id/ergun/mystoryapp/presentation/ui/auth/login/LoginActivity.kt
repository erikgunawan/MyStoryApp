package id.ergun.mystoryapp.presentation.ui.auth.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import id.ergun.mystoryapp.MainActivity
import id.ergun.mystoryapp.common.util.ResponseWrapper
import id.ergun.mystoryapp.data.remote.model.AuthRequest
import id.ergun.mystoryapp.databinding.ActivityLoginBinding
import id.ergun.mystoryapp.presentation.ui.auth.register.RegisterActivity
import id.ergun.mystoryapp.presentation.ui.story.list.StoryListActivity
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
            when (it.status) {
                ResponseWrapper.Status.SUCCESS -> {
                    if (it.data != null) {
                        Toast.makeText(this, "Login berhasil", Toast.LENGTH_SHORT).show()
                        gotoMainPage()
                    }
                }
                else -> {

                }
            }
            Timber.d(Gson().toJson(it))
        }
    }

    private fun setupListener() {
        binding.run {
            btnLogin.setOnClickListener {
                dologin()
            }

            tvRegister.setOnClickListener {
                gotoRegisterPage()
            }
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

    private fun gotoRegisterPage() {
        val intent = RegisterActivity.newIntent(this)
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
            binding.ivAppLogo, "logo_image")

        startActivity(intent, options.toBundle())
    }

    private fun gotoMainPage() {
        val intent = StoryListActivity.newIntent(this)
        startActivity(intent)
        finish()
    }

    companion object {
        fun newIntent(context: Context): Intent =
            Intent(context, LoginActivity::class.java).apply {
            }
    }
}