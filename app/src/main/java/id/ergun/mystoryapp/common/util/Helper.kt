package id.ergun.mystoryapp.common.util

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.text.Html
import android.text.Spanned
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import id.ergun.mystoryapp.R
import id.ergun.mystoryapp.R.string
import java.io.*
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
        errorDrawable: Int = R.drawable.img_placeholder_error,
        progressDrawable: Int = R.drawable.img_placeholder_loading
    ) {
        val builder = Glide.with(this.context)
            .load(url)
            .error(errorDrawable)
            .placeholder(progressDrawable)

        builder.into(this)
    }

    private val timeStamp: String = SimpleDateFormat(
        "ddMMMyyyyHHmmss",
        Locale.US
    ).format(System.currentTimeMillis())

    fun createCustomTempFile(context: Context): File {
        val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(timeStamp, ".jpg", storageDir)
    }

    fun uriToFile(selectedImg: Uri, context: Context): File {
        val contentResolver: ContentResolver = context.contentResolver
        val myFile = createCustomTempFile(context)

        val inputStream = contentResolver.openInputStream(selectedImg) as InputStream
        val outputStream: OutputStream = FileOutputStream(myFile)
        val buf = ByteArray(1024)
        var len: Int
        while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
        outputStream.close()
        inputStream.close()

        return myFile
    }

    fun rotateBitmap(bitmap: Bitmap, isBackCamera: Boolean = false): Bitmap {
        val matrix = Matrix()
        return if (isBackCamera) {
            matrix.postRotate(90f)
            Bitmap.createBitmap(
                bitmap,
                0,
                0,
                bitmap.width,
                bitmap.height,
                matrix,
                true
            )
        } else {
            matrix.postRotate(-90f)
            matrix.postScale(-1f, 1f, bitmap.width / 2f, bitmap.height / 2f)
            Bitmap.createBitmap(
                bitmap,
                0,
                0,
                bitmap.width,
                bitmap.height,
                matrix,
                true
            )
        }
    }

    fun createFile(application: Application): File {
        val mediaDir = application.externalMediaDirs.firstOrNull()?.let {
            File(it, application.resources.getString(string.app_name)).apply { mkdirs() }
        }

        val outputDirectory = if (
            mediaDir != null && mediaDir.exists()
        ) mediaDir else application.filesDir

        return File(outputDirectory, "$timeStamp.jpg")
    }

    fun String.toLocalDateFormat(): String {
        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", getLocale())
            val date = sdf.parse(this) ?: Date()

            val formatter = SimpleDateFormat("dd MMMM yyyy HH:mm", getLocale())

            val timeInMillis = formatter.format(date.time)

            val newDate = Date(timeInMillis)
            formatter.timeZone = TimeZone.getDefault()
            return formatter.format(newDate)
        } catch (e: Exception) {
            this
        }
    }

    private fun getLocale(): Locale = Locale.getDefault()

    fun Context.showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun View.visible(visible: Boolean = true) {
        visibility = if (visible) View.VISIBLE else View.GONE
    }

    fun View.gone() {
        visibility = View.GONE
    }


    @Suppress("DEPRECATION")
    fun getAddressName(context: Context, lat: Double, lon: Double): String {
        var addressName = ""
        val geocoder = Geocoder(context, Locale.getDefault())
        try {
            val list = geocoder.getFromLocation(lat, lon, 1)
            if (list != null && list.size != 0) {
                addressName = list[0].getAddressLine(0)
            }
        } catch (e: IOException) {
            e.printStackTrace()
            addressName = context.getString(string.unknown_location)
        }
        return addressName
    }

    fun Double?.replaceIfNull(): Double = this ?: 0.0

}