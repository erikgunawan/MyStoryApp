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
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import id.ergun.mystoryapp.common.util.ActivityResultLauncher
import id.ergun.mystoryapp.common.util.Helper.createCustomTempFile
import id.ergun.mystoryapp.common.util.Helper.rotateBitmap
import id.ergun.mystoryapp.common.util.Helper.uriToFile
import id.ergun.mystoryapp.common.util.ResponseWrapper
import id.ergun.mystoryapp.common.util.ResponseWrapper.Status.ERROR
import id.ergun.mystoryapp.databinding.ActivityStoryCreateBinding
import id.ergun.mystoryapp.presentation.ui.camera.CameraActivity
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

    private lateinit var currentPhotoPath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        activityLauncher = ActivityResultLauncher.register(this)

        setupToolbar()
        setupObserve()
        setupListener()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbarView.toolbar)
        supportActionBar?.run {
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            title = ""
        }
    }

    private fun setupObserve() {
        viewModel.createStoryResponse.observe(this) {
            when (it.status) {
                ResponseWrapper.Status.SUCCESS -> {
                    if (it.data?.error == false) {
                        onCreateStorySuccess(it.data.message)
                    }
                }
                ERROR -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
                else -> {

                }
            }
        }
    }

    private fun setupListener() {
        binding.run {
            btnCamera.setOnClickListener {
                gotoCamera()
//                gotoCameraPage()
            }
            btnGallery.setOnClickListener {
                gotoGallery()
            }
            btnCreateStory.setOnClickListener {
                createStory()
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
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            activityLauncher.launch(intent,
                object : ActivityResultLauncher.OnActivityResult<ActivityResult> {
                    override fun onActivityResult(result: ActivityResult) {

                        val myFile = File(currentPhotoPath)

                        val resultCamera = rotateBitmap(
                            BitmapFactory.decodeFile(myFile.path),
                            true
                        )

                        binding.ivPhoto.setImageBitmap(resultCamera)
                    }
                }
            )
        }
    }

    private fun gotoCameraPage() {
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
            return
        }

        val intent = Intent(this, CameraActivity::class.java)
        activityLauncher.launch(intent,
            object : ActivityResultLauncher.OnActivityResult<ActivityResult> {
                override fun onActivityResult(result: ActivityResult) {
                    if (result.resultCode == CAMERA_X_RESULT) {
                        val myFile = result.data?.getSerializableExtra("picture") as File
                        val isBackCamera = result.data?.getBooleanExtra("isBackCamera", true) as Boolean

                        val result = rotateBitmap(
                            BitmapFactory.decodeFile(myFile.path),
                            isBackCamera
                        )

                        binding.ivPhoto.setImageBitmap(result)
                    }
                }
            }
        )
    }


    private fun gotoGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")

        activityLauncher.launch(chooser,
            object : ActivityResultLauncher.OnActivityResult<ActivityResult> {
                override fun onActivityResult(result: ActivityResult) {
                    if (result.resultCode == RESULT_OK) {
                        val selectedImg: Uri = result.data?.data as Uri
                        binding.ivPhoto.setImageURI(selectedImg)
                    }
                }
            })

    }

    private fun createStory() {
        val description = binding.etDescription.getStringText()
        viewModel.createStory(description)
    }

    private fun onCreateStorySuccess(message: String) {
        if (message.isNotEmpty())
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

        val intent = Intent()
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "Permission diperlukan untuk menggunakan fitur ini.",
                    Toast.LENGTH_SHORT
                ).show()
                return
            }

            gotoCamera()
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
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
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
        fun newIntent(context: Context): Intent =
            Intent(context, StoryCreateActivity::class.java).apply {
            }
    }
}