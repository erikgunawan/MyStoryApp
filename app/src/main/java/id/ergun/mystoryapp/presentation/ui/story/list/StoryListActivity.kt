package id.ergun.mystoryapp.presentation.ui.story.list

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import id.ergun.mystoryapp.databinding.ActivityStoryListBinding
import id.ergun.mystoryapp.presentation.viewmodel.StoryViewModel

/**
 * @author erikgunawan
 * Created 01/10/22 at 22.38
 */

@AndroidEntryPoint
class StoryListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStoryListBinding

    private val viewModel by viewModels<StoryViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupObserve()
        setupListener()
        getStories()
    }

    private fun setupObserve() {
    }

    private fun setupListener() {
    }

    private fun getStories() {
        viewModel.getStories()
    }

    private fun dologin() {
    }


    companion object {
        fun newIntent(context: Context): Intent =
            Intent(context, StoryListActivity::class.java).apply {
            }
    }
}