package id.ergun.mystoryapp.presentation.ui.story.create.map

import javax.inject.Inject

/**
 * @author erikgunawan
 * Created 03/12/22 at 05.40
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