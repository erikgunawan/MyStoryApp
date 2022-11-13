package id.ergun.mystoryapp.presentation.ui.story.new

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import id.ergun.mystoryapp.databinding.ActivityStoryNewBinding
import id.ergun.mystoryapp.presentation.viewmodel.StoryViewModel

/**
 * @author erikgunawan
 * Created 02/10/22 at 23.14
 */
//@AndroidEntryPoint
class StoryNewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStoryNewBinding

    private val viewModel by viewModels<StoryViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryNewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupObserve()
        setupListener()
    }

    private fun setupObserve() {
    }

    private fun setupListener() {
        binding.btnPhoto.setOnClickListener {

        }

        binding.btnAdd.setOnClickListener {
            createStory()
        }
    }

    private fun createStory() {
        val description = binding.edAddDescription.text.toString()
//        val request = AuthRequest(
//            null, email, password
//        )
//        viewModel.login(request)
    }


    companion object {
        fun newIntent(context: Context): Intent =
            Intent(context, StoryNewActivity::class.java).apply {
            }
    }
}