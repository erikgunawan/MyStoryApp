package id.ergun.mystoryapp.presentation.ui.story.list

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.util.Pair
import androidx.activity.result.ActivityResult
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import dagger.hilt.android.AndroidEntryPoint
import id.ergun.mystoryapp.R
import id.ergun.mystoryapp.common.util.ActivityResultLauncher
import id.ergun.mystoryapp.common.util.Const
import id.ergun.mystoryapp.common.util.Helper.showToast
import id.ergun.mystoryapp.common.util.ResponseWrapper
import id.ergun.mystoryapp.databinding.ActivityStoryListBinding
import id.ergun.mystoryapp.domain.model.StoryDataModel
import id.ergun.mystoryapp.presentation.ui.auth.login.LoginActivity
import id.ergun.mystoryapp.presentation.ui.story.create.StoryCreateActivity
import id.ergun.mystoryapp.presentation.ui.story.detail.StoryDetailActivity
import id.ergun.mystoryapp.presentation.ui.story.map.StoryMapActivity
import id.ergun.mystoryapp.presentation.viewmodel.AccountViewModel
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
    private val accountViewModel by viewModels<AccountViewModel>()

    private val adapter: StoryListAdapter by lazy { StoryListAdapter() }

    private lateinit var activityLauncher: ActivityResultLauncher<Intent, ActivityResult>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        activityLauncher = ActivityResultLauncher.register(this)

        setupToolbar()
        setupAdapter()
        setupObserve()
        setupListener()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbarView.toolbar)
        supportActionBar?.run {
            title = ""
        }
    }

    private fun setupAdapter() {
        adapter.itemClickListener = { view, model ->
            gotoDetailStoryPage(view,model)
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

        accountViewModel.logoutResponse.observe(this) {
            when (it.status) {
                ResponseWrapper.Status.SUCCESS -> gotoLoginPage()
                else -> {}
            }
        }
    }

    private fun setupListener() {
        binding.run {
            swipeRefresh.setOnRefreshListener {
                adapter.refresh()
            }

            fabNewStory.setOnClickListener {
                gotoCreateStoryPage()
            }

            btnMap.setOnClickListener {
                gotoStoryMapPage()
            }
        }

        adapter.addLoadStateListener { loadState ->

            showView(CONTENT_VIEW_INDEX)

            val isEmptyList = loadState.refresh is LoadState.NotLoading && adapter.itemCount == 0
            val isLoading = loadState.refresh is LoadState.Loading && adapter.itemCount == 0

            if (isEmptyList) {
                binding.swipeRefresh.isRefreshing = false
                showErrorView("Belum ada data")
                return@addLoadStateListener
            }

            if (isLoading) showView(LOADING_VIEW_INDEX)

            if (loadState.refresh is LoadState.Loading ||
                loadState.append is LoadState.Loading
            ) {
                binding.swipeRefresh.isRefreshing = true
            }
            else {
                binding.swipeRefresh.isRefreshing = false

                val errorState = when {
                    loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                    loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                    loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                    else -> null
                }
                errorState?.let {
                    if (adapter.itemCount == 0) showErrorView(it.error.toString())
                    showToast(it.error.toString())
                }
            }
        }
    }

    private fun refreshData() {
        adapter.refresh()
    }

    private fun gotoCreateStoryPage() {

        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
            binding.toolbarView.ivToolbar, "toolbar_image")

        val intent = StoryCreateActivity.newIntent(this)
        activityLauncher.launch(intent,
            object : ActivityResultLauncher.OnActivityResult<ActivityResult> {
                override fun onActivityResult(result: ActivityResult) {
                    if (result.resultCode == Activity.RESULT_OK) {
                        refreshData()
                    }
                }
            },
            options)
    }

    private fun showLogoutConfirmationDialog() {
        MaterialDialog(this).show {
            title(R.string.dialog_logout_confirmation_title)
            message(R.string.dialog_logout_confirmation_message)
            cancelOnTouchOutside(false)
            cancelable(false)
            positiveButton(R.string.yes) { dialog ->
                accountViewModel.logout()
                dialog.dismiss()
            }
            negativeButton(R.string.cancel) { dialog ->
                dialog.dismiss()
            }
        }
    }

    private fun gotoDetailStoryPage(view: View, model: StoryDataModel) {
        val intent = StoryDetailActivity.newIntent(this, model)
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
          Pair.create(view, Const.SHARED_ELEMENT_PHOTO),
          Pair.create(binding.toolbarView.ivToolbar, "toolbar_image")
        )

        startActivity(intent, options.toBundle())
    }

    private fun gotoLoginPage() {
        startActivity(LoginActivity.newIntent(this))
        finish()
    }

    private fun gotoStoryMapPage() {
        val intent = StoryMapActivity.newIntent(this)
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
            Pair.create(binding.toolbarView.ivToolbar, "toolbar_image")
        )

        startActivity(intent, options.toBundle())
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_logout -> {
                showLogoutConfirmationDialog()
            }
            R.id.action_locale_setting -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_logout, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun showErrorView(message: String) {
        showView(ERROR_VIEW_INDEX)

        binding.warningView.run {
            tvWarning.text = message
            btnWarning.setOnClickListener {
                refreshData()
            }
        }

    }

    private fun showView(viewId: Int) {
        binding.viewAnimator.displayedChild = viewId
    }

    companion object {

        const val LOADING_VIEW_INDEX = 0
        const val CONTENT_VIEW_INDEX = 1
        const val ERROR_VIEW_INDEX = 2

        fun newIntent(context: Context): Intent =
            Intent(context, StoryListActivity::class.java)
    }
}