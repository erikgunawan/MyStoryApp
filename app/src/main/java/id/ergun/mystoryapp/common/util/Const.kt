package id.ergun.mystoryapp.common.util

import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.android.gms.maps.model.LatLng

/**
 * @author erikgunawan
 * Created 25/09/22 at 00.14
 */
object Const {
    const val MEDIA_TYPE_TEXT_PLAIN = "text/plain"

    const val SHARED_ELEMENT_PHOTO = "photo_image"
    const val SHARED_ELEMENT_TOOLBAR_IMAGE = "toolbar_image"

    val PREFERENCES_KEY_TOKEN = stringPreferencesKey("token")

    val INDONESIA_LATLNG = LatLng(-2.4153253,108.8513128)
}