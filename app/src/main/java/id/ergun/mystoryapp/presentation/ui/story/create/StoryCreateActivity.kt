package id.ergun.mystoryapp.presentation.ui.story.create

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import androidx.activity.result.ActivityResult
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import dagger.hilt.android.AndroidEntryPoint
import id.ergun.mystoryapp.R
import id.ergun.mystoryapp.common.util.ActivityResultLauncher
import id.ergun.mystoryapp.common.util.Const.SHARED_ELEMENT_TOOLBAR_IMAGE
import id.ergun.mystoryapp.common.util.Helper.createCustomTempFile
import id.ergun.mystoryapp.common.util.Helper.loadImage
import id.ergun.mystoryapp.common.util.Helper.rotateBitmap
import id.ergun.mystoryapp.common.util.Helper.showToast
import id.ergun.mystoryapp.common.util.Helper.uriToFile
import id.ergun.mystoryapp.common.util.Helper.visible
import id.ergun.mystoryapp.common.util.ResponseWrapper
import id.ergun.mystoryapp.data.remote.model.StoryFormRequest
import id.ergun.mystoryapp.databinding.ActivityStoryCreateBinding
import id.ergun.mystoryapp.presentation.ui.story.create.map.StoryCreateMapActivity
import id.ergun.mystoryapp.presentation.viewmodel.StoryViewModel
import java.io.File

/**
 * @author erikgunawan
 * Created 02/10/22 at 23.14
 */
@AndroidEntryPoint
class StoryCreateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStoryCreateBinding

    private val viewModel by viewModels<StoryViewModel>()

    private lateinit var activityLauncher: ActivityResultLauncher<Intent, ActivityResult>

    private var photoFile: File? = null

    private var latitude: Double? = null
    private var longitude: Double? = null
    private var address: String = ""

    private val loadingDialog by lazy {
        MaterialDialog(this)
            .title(R.string.loading)
            .customView(R.layout.loading_dialog)
            .cancelOnTouchOutside(false)
            .cancelable(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        activityLauncher = ActivityResultLauncher.register(this)

        setupToolbar()
        setupObserve()
        setupListener()
        setupLocationView()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbarView.toolbar)
        supportActionBar?.run {
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            title = ""
        }
    }

    private fun setupLocationView() {
        binding.run {
            val isLocationSelected = latitude != null && longitude != null
            btnSelectLocation.visible(!isLocationSelected)
            containerAddress.visible(isLocationSelected)
            tvAddressLocation.visible(isLocationSelected)
            ivClearLocation.visible(isLocationSelected)


            tvAddressLocation.text = address
        }
    }

    private fun setupObserve() {
        viewModel.createStoryResponse.observe(this) {
            loadingDialog.dismiss()
            when (it.status) {
                ResponseWrapper.Status.SUCCESS -> {
                    if (it.data?.error == false) {
                        onCreateStorySuccess(it.data.message)
                    }
                }
                ResponseWrapper.Status.ERROR -> {
                    it.message?.let { it1 -> showToast(it1) }
                }
                else -> {
                    // do something
                }
            }
        }
    }

    private fun setupListener() {
        binding.run {
            btnCamera.setOnClickListener {
                gotoCamera()
            }
            btnGallery.setOnClickListener {
                gotoGallery()
            }
            btnSelectLocation.setOnClickListener {
                gotoSelectLocationPage()
            }
            btnCreateStory.setOnClickListener {
                createStory()
            }

            ivClearLocation.setOnClickListener {
                clearLocation()
            }
        }
    }

    private fun gotoCamera() {
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
            return
        }
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)

        createCustomTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this,
                "id.ergun.mystoryapp",
                it
            )
            photoFile = File(it.absolutePath)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            activityLauncher.launch(intent,
                object : ActivityResultLauncher.OnActivityResult<ActivityResult> {
                    override fun onActivityResult(result: ActivityResult) {

                        val resultCamera = rotateBitmap(
                            BitmapFactory.decodeFile(photoFile?.path),
                            true
                        )

                        binding.ivPhoto.setImageBitmap(resultCamera)
                    }
                }
            )
        }
    }

    private fun gotoGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, getString(R.string.choose_picture))

        activityLauncher.launch(chooser,
            object : ActivityResultLauncher.OnActivityResult<ActivityResult> {
                override fun onActivityResult(result: ActivityResult) {
                    if (result.resultCode == RESULT_OK) {
                        val selectedImg: Uri = result.data?.data as Uri
                        photoFile = uriToFile(selectedImg, this@StoryCreateActivity)
                        binding.ivPhoto.loadImage(selectedImg.toString())
                    }
                }
            })

    }

    private fun createStory() {
        val description = binding.etDescription.text.toString()

        if (description.isEmpty()) {
            showToast(getString(R.string.input_description_empty_error))
            return
        }

        if (photoFile == null) {
            showToast(getString(R.string.need_upload_photo_error))
            return
        }

        val request = StoryFormRequest(
            description, photoFile ?: File.createTempFile("abc", "def"), latitude, longitude
        )

        loadingDialog.show()
        viewModel.createStory(request)
    }

    private fun onCreateStorySuccess(message: String) {
        if (message.isNotEmpty()) showToast(message)

        val intent = Intent()
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    private fun clearLocation() {
        latitude = null
        longitude = null
        address = ""

        setupLocationView()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                showToast(getString(R.string.need_permission_message))
                return
            }

            gotoCamera()
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun gotoSelectLocationPage() {

        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
            binding.toolbarView.ivToolbar, SHARED_ELEMENT_TOOLBAR_IMAGE)

        val intent = StoryCreateMapActivity.newIntent(this)
        activityLauncher.launch(intent,
            object : ActivityResultLauncher.OnActivityResult<ActivityResult> {
                override fun onActivityResult(result: ActivityResult) {
                    if (result.resultCode == Activity.RESULT_OK) {
                        result.data?.extras?.run {
                            latitude = getDouble("latitude")
                            longitude = getDouble("longitude")
                            address = getString("address") ?: ""
                        }
                        setupLocationView()
                    }
                }
            },
            options)
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
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10

        fun newIntent(context: Context): Intent =
            Intent(context, StoryCreateActivity::class.java)
    }
}