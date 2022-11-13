package id.ergun.mystoryapp

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import id.ergun.mystoryapp.databinding.ActivityMainBinding
import id.ergun.mystoryapp.presentation.ui.auth.login.LoginActivity
import id.ergun.mystoryapp.presentation.ui.auth.register.RegisterActivity
import id.ergun.mystoryapp.presentation.ui.story.list.StoryListActivity
import id.ergun.mystoryapp.presentation.ui.story.new.StoryNewActivity
import id.ergun.mystoryapp.presentation.viewmodel.StoryViewModel
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val viewModel by viewModels<StoryViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.stories.observe(this) {
            Timber.d(Gson().toJson(it))
        }

        setupListener()
    }

    private fun setupListener() {
        binding.run {
            btnStoryNew.setOnClickListener {
                startActivity(StoryNewActivity.newIntent(this@MainActivity))
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
}