package id.co.app.components.player

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.extractor.ExtractorsFactory
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource


/**
 * Created by Lukas Kristianto on 7/5/2021.
 * App Sinarmas
 * lukas_kristianto@app.co.id
 */
open class SecureAppPlayer @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), LifecycleObserver {
    private val exoPlayerView = PlayerView(context)

    private var player = SimpleExoPlayer.Builder(context).build()
    private var lifecycleOwner: LifecycleOwner? = null

    init {
        this.addView(exoPlayerView)
        initPlayer()
    }

    private fun initPlayer() {
        exoPlayerView.player = player
    }

    fun setVideo(uri: Uri, key: String){
        val dataSourceFactory: DataSource.Factory = EncryptedDataSourceFactory(key)
        val extractorsFactory: ExtractorsFactory = DefaultExtractorsFactory()
        val videoSource: MediaSource = ProgressiveMediaSource.Factory(dataSourceFactory, extractorsFactory)
            .createMediaSource(MediaItem.fromUri(uri))
        player.setMediaSource(videoSource)
        player.prepare()
        player.playWhenReady = true
    }

    fun setLifecycleOwner(lifecycleOwner: LifecycleOwner){
        this.lifecycleOwner = lifecycleOwner
        this.lifecycleOwner?.lifecycle?.addObserver(this)
    }

    fun play(){
        // Start the playback.
        player.play()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun pause(){
        player.pause()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun dispose(){
        player.stop()
        player.release()
        exoPlayerView.player = null
    }

    override fun onDetachedFromWindow() {
        dispose()
        super.onDetachedFromWindow()
    }
}