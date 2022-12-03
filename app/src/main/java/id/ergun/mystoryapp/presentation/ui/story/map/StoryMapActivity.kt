package id.ergun.mystoryapp.presentation.ui.story.map

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import id.ergun.mystoryapp.R
import id.ergun.mystoryapp.common.util.Helper.replaceIfNull
import id.ergun.mystoryapp.common.util.Helper.showToast
import id.ergun.mystoryapp.common.util.ResponseWrapper
import id.ergun.mystoryapp.databinding.ActivityStoryMapBinding
import id.ergun.mystoryapp.domain.model.StoryDataModel
import id.ergun.mystoryapp.presentation.ui.story.detail.StoryDetailActivity
import id.ergun.mystoryapp.presentation.viewmodel.StoryViewModel

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
            val latLng = LatLng(story.lat.replaceIfNull(), story.lon.replaceIfNull())
            val title = getString(R.string.story_name_title_map, story.name)
            mMap.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title(title)
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

        getMyLocation()

        mMap.setOnInfoWindowClickListener { marker ->
            val story: StoryDataModel = marker.tag as StoryDataModel
            gotoDetailStoryPage(story)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_REQ_CODE -> {
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission granted
                } else {
                    showToast(getString(R.string.need_permission_message))
                    finish()
                }
            }
        }
    }

    private fun getMyLocation() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQ_CODE
            )
            return
        }
        mMap.isMyLocationEnabled = true
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
        private const val LOCATION_PERMISSION_REQ_CODE = 1000

        fun newIntent(context: Context): Intent =
            Intent(context, StoryMapActivity::class.java)
    }
}