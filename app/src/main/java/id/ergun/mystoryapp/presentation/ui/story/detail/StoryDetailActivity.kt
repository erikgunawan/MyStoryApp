package id.ergun.mystoryapp.presentation.ui.story.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import id.ergun.mystoryapp.R
import id.ergun.mystoryapp.common.util.Helper.getAddressName
import id.ergun.mystoryapp.common.util.Helper.loadImage
import id.ergun.mystoryapp.common.util.Helper.replaceIfNull
import id.ergun.mystoryapp.common.util.Helper.toLocalDateFormat
import id.ergun.mystoryapp.common.util.Helper.visible
import id.ergun.mystoryapp.databinding.ActivityStoryDetailBinding
import id.ergun.mystoryapp.domain.model.StoryDataModel
import id.ergun.mystoryapp.presentation.viewmodel.StoryViewModel

/**
 * @author erikgunawan
 * Created 15/11/22 at 22.46
 */
@AndroidEntryPoint
class StoryDetailActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityStoryDetailBinding

    private val viewModel by viewModels<StoryViewModel>()

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()

        setupMapView()
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

    private fun setupMapView() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map_view) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    @Suppress("DEPRECATION")
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

            val isLocationAvailable = story.lat != null && story.lon != null
            tvStoryLocation.visible(isLocationAvailable)
            tvStoryLocation.text = getAddressName(this@StoryDetailActivity, story.lat.replaceIfNull(), story.lon.replaceIfNull())

            mapView.visible(isLocationAvailable)

            ivPhoto.loadImage(story.photoUrl)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isScrollGesturesEnabled = false
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        val boundsBuilder = LatLngBounds.Builder()

        val latLng = LatLng(viewModel.selectedStory.lat.replaceIfNull(), viewModel.selectedStory.lon.replaceIfNull())
        val title = getString(R.string.story_name_title_map, viewModel.selectedStory.name)
        mMap.addMarker(MarkerOptions().position(latLng).title(title))?.showInfoWindow()
        boundsBuilder.include(latLng)

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,14f))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> supportFinishAfterTransition()
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private const val EXTRA_STORY_DATA_MODEL = "EXTRA_STORY_DATA_MODEL"
        fun newIntent(context: Context, story: StoryDataModel): Intent =
            Intent(context, StoryDetailActivity::class.java).apply {
                putExtra(EXTRA_STORY_DATA_MODEL, story)
            }
    }
}