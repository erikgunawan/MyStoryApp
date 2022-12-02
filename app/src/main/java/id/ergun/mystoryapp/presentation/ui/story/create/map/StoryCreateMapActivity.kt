package id.ergun.mystoryapp.presentation.ui.story.create.map

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.location.LocationManagerCompat.getCurrentLocation
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import id.ergun.mystoryapp.R
import id.ergun.mystoryapp.common.util.Helper.getAddressName
import id.ergun.mystoryapp.common.util.Helper.showToast
import id.ergun.mystoryapp.databinding.ActivityStoryCreateMapBinding
import id.ergun.mystoryapp.databinding.MapInfoWindowCustomViewBinding
import id.ergun.mystoryapp.domain.model.StoryDataModel
import javax.inject.Inject

/**
 * Created by alfacart on 01/12/22.
 */

@AndroidEntryPoint
class StoryCreateMapActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.InfoWindowAdapter {
  private lateinit var binding: ActivityStoryCreateMapBinding

  private lateinit var mMap: GoogleMap
  private lateinit var fusedLocationClient: FusedLocationProviderClient

  @Inject
  lateinit var model: StoryCreateMapModel

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityStoryCreateMapBinding.inflate(layoutInflater)
    setContentView(binding.root)

    fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

    setupToolbar()
    setupMapView()
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

  private fun setupMapView() {
    val mapFragment = supportFragmentManager.findFragmentById(R.id.map_view) as SupportMapFragment
    mapFragment.getMapAsync(this)
  }

  private fun setupListener() {
    binding.btnSelectLocation.setOnClickListener {
      getCurrentLocation()
    }
  }

  private fun onLocationSelected(latitude: Double, longitude: Double) {
    val address = getAddressName(this, latitude, longitude)
    model.updateSelectedLocation(latitude, longitude, address)

    val intent = Intent().apply {
      putExtra("latitude", model.selectedLatitude)
      putExtra("longitude", model.selectedLongitude)
      putExtra("address", model.addressName)
    }
    setResult(Activity.RESULT_OK, intent)
    finish()
  }

  override fun onMapReady(googleMap: GoogleMap) {
    mMap = googleMap

    mMap.uiSettings.isZoomControlsEnabled = true
    mMap.uiSettings.isIndoorLevelPickerEnabled = true
    mMap.uiSettings.isCompassEnabled = true
    mMap.uiSettings.isMapToolbarEnabled = true

    mMap.setInfoWindowAdapter(this)

    getCurrentLocation()

    mMap.setOnInfoWindowClickListener { marker ->
      onLocationSelected(marker.position.latitude, marker.position.longitude)
    }

    mMap.setOnMapClickListener {
      onMarkerSelected(it)
    }

    mMap.setOnPoiClickListener {
      onMarkerSelected(it.latLng)
    }
  }

  private fun onMarkerSelected(latLng: LatLng) {
    mMap.clear()
    mMap.addMarker(MarkerOptions().position(latLng))?.showInfoWindow()
    mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
  }

  override fun getInfoContents(marker: Marker): View? {
    return null
  }

  override fun getInfoWindow(marker: Marker): View {
    val inflater = LayoutInflater.from(this)
    val infoWindowBinding = MapInfoWindowCustomViewBinding.inflate(inflater)

    val latitude = marker.position.latitude
    val longitude = marker.position.longitude
    val address = getAddressName(this, latitude, longitude)

    infoWindowBinding.tvAddress.text = address
    infoWindowBinding.tvLatLng.text = "Lat Long : " + latitude + ", " + longitude

    return infoWindowBinding.root
  }

  private val boundsBuilder = LatLngBounds.Builder()
  private fun getCurrentLocation() {
    // checking location permission
    if (ActivityCompat.checkSelfPermission(this,
        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
      // request permission
      ActivityCompat.requestPermissions(this,
        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQ_CODE);
      return
    }
    fusedLocationClient.lastLocation
      .addOnSuccessListener { location ->
        // getting the last known or current location
        val latLng = LatLng(location.latitude, location.longitude)
        onMarkerSelected(latLng)
        boundsBuilder.include(latLng)
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
      .addOnFailureListener {
        Toast.makeText(this, "Failed on getting current location",
          Toast.LENGTH_SHORT).show()
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

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      android.R.id.home -> supportFinishAfterTransition()
    }
    return super.onOptionsItemSelected(item)
  }

  companion object {
    private const val TAG = "MapsActivity"
    private val LOCATION_PERMISSION_REQ_CODE = 1000;

    fun newIntent(context: Context): Intent =
      Intent(context, StoryCreateMapActivity::class.java)
  }
}