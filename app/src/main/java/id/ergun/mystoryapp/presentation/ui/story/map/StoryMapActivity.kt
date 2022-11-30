package id.ergun.mystoryapp.presentation.ui.story.map

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import id.ergun.mystoryapp.R
import id.ergun.mystoryapp.common.util.ResponseWrapper
import id.ergun.mystoryapp.databinding.ActivityStoryMapBinding
import id.ergun.mystoryapp.domain.model.StoryDataModel
import id.ergun.mystoryapp.presentation.ui.story.detail.StoryDetailActivity
import id.ergun.mystoryapp.presentation.viewmodel.StoryViewModel
import timber.log.Timber

/**
 * @author erikgunawan
 * Created 27/11/22 at 13.38
 */

@AndroidEntryPoint
class StoryMapActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityStoryMapBinding

    private val viewModel by viewModels<StoryViewModel>()

    private lateinit var mMap: GoogleMap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupMapView()

        val params = hashMapOf<String, String>()
        params["location"] = "1"
        viewModel.getStories(params)

        setupObserve()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbarView.toolbar)
        supportActionBar?.run {
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            title = ""
        }
    }

    private fun setupMapView() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map_view) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }


    private fun setupObserve() {
        viewModel.stories.observe(this) {
            when (it.status) {
                ResponseWrapper.Status.SUCCESS -> {
                    it.data?.let { it1 -> addMarkers(it1) }
                }
                else -> {}
            }
        }
    }

    private fun addMarkers(stories: ArrayList<StoryDataModel>) {
        stories.forEach { story ->
            val latLng = LatLng(story.lat, story.lon)
            mMap.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title(story.name)
                    .snippet(story.description)
            )?.tag = story
            boundsBuilder.include(latLng)
        }
        val bounds: LatLngBounds = boundsBuilder.build()
        mMap.animateCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds,
                resources.displayMetrics.widthPixels,
                resources.displayMetrics.heightPixels,
                300
            )
        )
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        val dicodingSpace = LatLng(-6.8957643, 107.6338462)

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(dicodingSpace, 15f))

        getMyLocation()
        setMapStyle()


        mMap.setOnInfoWindowClickListener { marker ->
            val story: StoryDataModel = marker.tag as StoryDataModel
            gotoDetailStoryPage(story)
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun setMapStyle() {
        try {
            val success =
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
            if (!success) {
                Timber.tag(TAG).e("Style parsing failed.")
            }
        } catch (exception: Resources.NotFoundException) {
            Timber.tag(TAG).e(exception, "Can't find style. Error: ")
        }
    }

    private val boundsBuilder = LatLngBounds.Builder()

    private fun gotoDetailStoryPage(story: StoryDataModel) {
        val intent = StoryDetailActivity.newIntent(this, story)
        startActivity(intent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> supportFinishAfterTransition()
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private const val TAG = "MapsActivity"

        fun newIntent(context: Context): Intent =
            Intent(context, StoryMapActivity::class.java)
    }
}