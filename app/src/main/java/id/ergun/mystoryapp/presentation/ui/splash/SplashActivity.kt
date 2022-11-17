package id.ergun.mystoryapp.presentation.ui.splash

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import id.ergun.mystoryapp.MainActivity
import id.ergun.mystoryapp.common.util.ResponseWrapper
import id.ergun.mystoryapp.databinding.ActivitySplashBinding
import id.ergun.mystoryapp.presentation.ui.auth.login.LoginActivity
import id.ergun.mystoryapp.presentation.ui.story.list.StoryListActivity
import id.ergun.mystoryapp.presentation.viewmodel.AccountViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * @author erikgunawan
 * Created 15/11/22 at 05.41
 */
@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    private val viewModel by viewModels<AccountViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setupObserve()

        lifecycleScope.launch {
            delay(2000)
            viewModel.getToken()
        }
    }

    private fun setupObserve() {
        lifecycleScope.launch {
            viewModel.token.observe(this@SplashActivity) {
                when (it.status) {
                    ResponseWrapper.Status.SUCCESS -> gotoHomePage()
                    else -> gotoLoginPage()
                }
            }
        }
    }

    private fun gotoHomePage() {
        startActivity(StoryListActivity.newIntent(this))
        finish()
    }

    private fun gotoLoginPage() {
        startActivity(LoginActivity.newIntent(this))
        finish()
    }

}