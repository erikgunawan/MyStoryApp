package id.ergun.mystoryapp.presentation.ui.story.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import id.ergun.mystoryapp.common.util.Helper.loadImage
import id.ergun.mystoryapp.common.util.Helper.toLocalDateFormat
import id.ergun.mystoryapp.databinding.ActivityStoryDetailBinding
import id.ergun.mystoryapp.domain.model.StoryDataModel
import id.ergun.mystoryapp.presentation.viewmodel.StoryViewModel
import kotlinx.coroutines.launch

/**
 * @author erikgunawan
 * Created 15/11/22 at 22.46
 */
@AndroidEntryPoint
class StoryDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStoryDetailBinding

    private val viewModel by viewModels<StoryViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()

        loadExtras()
        adjustView(viewModel.selectedStory)
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbarView.toolbar)
        supportActionBar?.run {
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            title = ""
        }
    }

    private fun loadExtras() {
        intent?.run {
            viewModel.selectedStory = getParcelableExtra(EXTRA_STORY_DATA_MODEL) ?: return
        }
    }

    private fun adjustView(story: StoryDataModel) {
        with(binding) {
            tvName.text = story.name
            tvDescription.text = story.description
            tvDate.text = story.createdAt.toLocalDateFormat()
            ivPhoto.loadImage(story.photoUrl)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> supportFinishAfterTransition()
        }
        return super.onOptionsItemSelected(item)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        supportFinishAfterTransition()
    }

    companion object {
        private const val EXTRA_STORY_DATA_MODEL = "EXTRA_STORY_DATA_MODEL"
        fun newIntent(context: Context, story: StoryDataModel): Intent =
            Intent(context, StoryDetailActivity::class.java).apply {
                putExtra(EXTRA_STORY_DATA_MODEL, story)
            }
    }
}