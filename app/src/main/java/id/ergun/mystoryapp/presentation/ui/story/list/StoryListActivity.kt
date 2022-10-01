package id.ergun.mystoryapp.presentation.ui.story.list

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import id.ergun.mystoryapp.databinding.ActivityStoryListBinding
import id.ergun.mystoryapp.presentation.viewmodel.StoryListViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * @author erikgunawan
 * Created 01/10/22 at 22.38
 */

@AndroidEntryPoint
class StoryListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStoryListBinding

    private val viewModel by viewModels<StoryListViewModel>()

    private val adapter: StoryListAdapter by lazy { StoryListAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAdapter()
        setupObserve()
    }

    private fun setupAdapter() {
        binding.rvData.let {
            it.layoutManager = LinearLayoutManager(this)
            it.adapter = adapter
        }
    }

    private fun setupObserve() {
        lifecycleScope.launch {
            viewModel.getStories().collectLatest {
                adapter.submitData(it)
            }
        }
    }

    companion object {
        fun newIntent(context: Context): Intent =
            Intent(context, StoryListActivity::class.java).apply {
            }
    }
}