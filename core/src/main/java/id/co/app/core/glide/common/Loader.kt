package id.co.app.core.glide.common

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

@SuppressLint("StaticFieldLeak")
object Loader {

    private var context: Context? = null

    // reducing fetch time of remote config
    private var isAdaptiveImage: Boolean = false

    private const val KEY_ADAPTIVE_IMAGE = "is_adaptive_image_status"

    @JvmStatic
    fun init(application: Application) {
        context = application.applicationContext
    }

    fun urlBuilder(url: String): String {
        if (context == null) return url
//        Todo("Waiting backend support adaptive image")
//        val networkState = networkManagerState(context)
//        return if (isAdaptiveImage) {
//            urlBuilder(networkState, settings.qualitySettings(), url)
//        } else url

        return url
    }

}