package id.ergun.mystoryapp.presentation.ui.story.list

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import id.ergun.mystoryapp.common.util.ActivityResultLauncher
import id.ergun.mystoryapp.databinding.ActivityStoryListBinding
import id.ergun.mystoryapp.presentation.ui.story.create.StoryCreateActivity
import id.ergun.mystoryapp.presentation.ui.story.detail.StoryDetailActivity
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

    private lateinit var activityLauncher: ActivityResultLauncher<Intent, ActivityResult>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        activityLauncher = ActivityResultLauncher.register(this)

        setupAdapter()
        setupObserve()
        setupListener()
    }

    private fun setupAdapter() {
        adapter.itemClickListener = { view, model ->
            val intent = StoryDetailActivity.newIntent(this, model)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                view, "photo_image")

            startActivity(intent, options.toBundle())

            Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show()
        }

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

    private fun setupListener() {
        binding.fabNewStory.setOnClickListener {
            gotoStoryNewActivity()
        }
    }

    private fun gotoStoryNewActivity() {
        val intent = StoryCreateActivity.newIntent(this)
        activityLauncher.launch(intent,
            object : ActivityResultLauncher.OnActivityResult<ActivityResult> {
                override fun onActivityResult(result: ActivityResult) {
                    if (result.resultCode == Activity.RESULT_OK) {
                        adapter.refresh()
                        binding.rvData.scrollToPosition(0)
                    }
                }
            })
    }

    companion object {
        fun newIntent(context: Context): Intent =
            Intent(context, StoryListActivity::class.java).apply {
            }
    }
}