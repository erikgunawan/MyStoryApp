package id.ergun.mystoryapp.presentation.ui.auth.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import dagger.hilt.android.AndroidEntryPoint
import id.ergun.mystoryapp.R
import id.ergun.mystoryapp.common.util.ActivityResultLauncher
import id.ergun.mystoryapp.common.util.Helper.showToast
import id.ergun.mystoryapp.common.util.ResponseWrapper
import id.ergun.mystoryapp.data.remote.model.AuthRequest
import id.ergun.mystoryapp.databinding.ActivityLoginBinding
import id.ergun.mystoryapp.presentation.ui.auth.register.RegisterActivity
import id.ergun.mystoryapp.presentation.ui.story.create.StoryCreateActivity
import id.ergun.mystoryapp.presentation.ui.story.list.StoryListActivity
import id.ergun.mystoryapp.presentation.viewmodel.LoginViewModel

/**
 * @author erikgunawan
 * Created 01/10/22 at 21.10
 */
@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private val viewModel by viewModels<LoginViewModel>()

    private lateinit var activityLauncher: ActivityResultLauncher<Intent, ActivityResult>

    private val loadingDialog by lazy {
        MaterialDialog(this)
            .title(R.string.loading)
            .customView(R.layout.loading_dialog)
            .cancelOnTouchOutside(false)
            .cancelable(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        activityLauncher = ActivityResultLauncher.register(this)

        setupObserve()
        setupListener()
    }

    private fun setupObserve() {
        viewModel.loginResponse.observe(this) {
            loadingDialog.dismiss()
            when (it.status) {
                ResponseWrapper.Status.SUCCESS -> {
                    if (it.data != null) {
                        showToast(getString(R.string.login_success_message))
                        gotoMainPage()
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

        val emailError = binding.edLoginEmail.error
        val passwordError = binding.edLoginPassword.error

        if (email.isEmpty()) {
            showToast(getString(R.string.input_email_empty_error))
            return
        }

        if (password.isEmpty()) {
            showToast(getString(R.string.input_password_empty_error))
            return
        }

        if (emailError != null || passwordError != null) {
            showToast(getString(R.string.general_input_error))
            return
        }

        val request = AuthRequest(
            null, email, password
        )

        loadingDialog.show()
        viewModel.login(request)
    }

    private fun gotoRegisterPage() {
        val intent = RegisterActivity.newIntent(this)
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
            Pair.create(binding.edLoginEmail, "input_email"),
            Pair.create(binding.edLoginPassword, "input_password"),
        )

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