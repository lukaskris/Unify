package id.co.app.core.glide.listener

import android.graphics.Bitmap
import id.co.app.core.glide.utils.MediaException
import id.co.app.core.glide.wrapper.MediaDataSource

interface MediaListener {
    fun onLoaded(resource: Bitmap?, dataSource: MediaDataSource?)
    fun onFailed(error: MediaException?)
}