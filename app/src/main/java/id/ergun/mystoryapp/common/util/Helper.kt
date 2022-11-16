package id.ergun.mystoryapp.common.util

import android.os.Build
import android.text.Html
import android.text.Spanned
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author erikgunawan
 * Created 15/11/22 at 23.56
 */
object Helper {

    fun getHeaderMap(token: String): Map<String, String> {
        val headerMap = mutableMapOf<String, String>()
        headerMap["Authorization"] = "Bearer $token"
        return headerMap
    }

    @Suppress("DEPRECATION")
    fun String.fromHtml(): Spanned {
        return if (Build.VERSION.SDK_INT >= 24) {
            Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(this)
        }
    }

    fun ImageView.loadImage(
        url: String?,
//    errorDrawable: Int = R.drawable.ic_image_placeholder_error,
//    progressDrawable: Int? = R.drawable.ic_image_placeholder_loading
    ) {
        val requestOptions = RequestOptions()//.build(errorDrawable, progressDrawable)

        val builder = Glide.with(this.context)
            .load(url)
            .apply(requestOptions)

        builder.into(this)
    }

    fun String.toLocalDateFormat(): String {
        return try {
            val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            val date = format.parse(this) as Date

            val textDate = DateFormat.getDateInstance(DateFormat.FULL).format(date)
            val textTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(date)
            "$textDate $textTime"
        } catch (e: Exception) {
            this
        }
    }
}