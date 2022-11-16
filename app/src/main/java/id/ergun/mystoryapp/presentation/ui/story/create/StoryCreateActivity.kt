package id.ergun.mystoryapp.presentation.ui.story.create

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import id.ergun.mystoryapp.common.util.ResponseWrapper
import id.ergun.mystoryapp.databinding.ActivityStoryCreateBinding
import id.ergun.mystoryapp.presentation.viewmodel.StoryViewModel

/**
 * @author erikgunawan
 * Created 02/10/22 at 23.14
 */
@AndroidEntryPoint
class StoryCreateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStoryCreateBinding

    private val viewModel by viewModels<StoryViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupObserve()
        setupListener()
    }

    private fun setupObserve() {
        viewModel.createStoryResponse.observe(this) {
            when (it.status) {
                ResponseWrapper.Status.SUCCESS -> {
                    if (it.data?.error == false) {
                        onCreateStorySuccess(it.data.message)
                    }
                }
                else -> {

                }
            }
            Log.d("respxxx", Gson().toJson(it))
        }
    }

    private fun setupListener() {
        binding.btnPhoto.setOnClickListener {

        }

        binding.btnCreateStory.setOnClickListener {
            createStory()
        }
    }

    private fun createStory() {
        val description = binding.etDescription.text.toString()
        viewModel.createStory(description)
    }

    private fun onCreateStorySuccess(message: String) {
        if (message.isNotEmpty())
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

        val intent = Intent()
        setResult(Activity.RESULT_OK, intent)
        finish()
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
        fun newIntent(context: Context): Intent =
            Intent(context, StoryCreateActivity::class.java).apply {
            }
    }
}