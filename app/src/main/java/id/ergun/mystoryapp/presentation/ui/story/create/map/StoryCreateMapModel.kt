package id.ergun.mystoryapp.presentation.ui.story.create.map

import javax.inject.Inject

/**
 * Created by alfacart on 01/12/22.
 */
class StoryCreateMapModel @Inject constructor() {
  var selectedLatitude: Double? = null
  var selectedLongitude: Double? = null
  var addressName: String = ""

  fun updateSelectedLocation(latitude: Double, longitude: Double, address: String) {
    selectedLatitude = latitude
    selectedLongitude = longitude
    addressName = address
  }
}