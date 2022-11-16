package id.ergun.mystoryapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.afollestad.materialdialogs.MaterialDialog
import dagger.hilt.android.AndroidEntryPoint
import id.ergun.mystoryapp.common.util.ResponseWrapper
import id.ergun.mystoryapp.databinding.ActivityMainBinding
import id.ergun.mystoryapp.presentation.ui.auth.login.LoginActivity
import id.ergun.mystoryapp.presentation.ui.auth.register.RegisterActivity
import id.ergun.mystoryapp.presentation.ui.story.create.StoryCreateActivity
import id.ergun.mystoryapp.presentation.ui.story.list.StoryListActivity
import id.ergun.mystoryapp.presentation.viewmodel.AccountViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val viewModel by viewModels<AccountViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarView.toolbar)
        supportActionBar?.run {
            title = ""
        }

        setupListener()
        setupObserve()
    }

    private fun setupObserve() {
        viewModel.logoutResponse.observe(this) {
            when (it.status) {
                ResponseWrapper.Status.SUCCESS -> gotoLoginPage()
                else -> {}
            }
        }
    }
    private fun setupListener() {
        binding.run {
            btnStoryNew.setOnClickListener {
                startActivity(StoryCreateActivity.newIntent(this@MainActivity))
            }
            btnStories.setOnClickListener {
                startActivity(StoryListActivity.newIntent(this@MainActivity))
            }
            btnLogin.setOnClickListener {
                startActivity(LoginActivity.newIntent(this@MainActivity))
            }

            btnRegister.setOnClickListener {
                startActivity(RegisterActivity.newIntent(this@MainActivity))
            }
        }
    }

    private fun showLogoutConfirmationDialog() {
        MaterialDialog(this).show {
            title(R.string.dialog_logout_confirmation_title)
            message(R.string.dialog_logout_confirmation_message)
            cancelOnTouchOutside(false)
            cancelable(false)
            positiveButton(R.string.yes) { dialog ->
                viewModel.logout()
                dialog.dismiss()
            }
            negativeButton(R.string.cancel) { dialog ->
                dialog.dismiss()
            }
        }
    }

    private fun gotoLoginPage() {
        startActivity(LoginActivity.newIntent(this))
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_logout -> {
                showLogoutConfirmationDialog()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_logout, menu)
        return super.onCreateOptionsMenu(menu)
    }

    companion object {
        fun newIntent(context: Context): Intent =
            Intent(context, MainActivity::class.java).apply {
            }
    }
}