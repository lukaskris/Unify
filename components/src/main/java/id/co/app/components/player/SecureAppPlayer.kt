package id.co.app.components.player

import android.content.Context
import android.util.AttributeSet
import android.view.TextureView
import android.widget.FrameLayout
import androidx.core.net.toUri
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView


/**
 * Created by Lukas Kristianto on 7/5/2021.
 * App Sinarmas
 * lukas_kristianto@app.co.id
 */
open class SecureAppPlayer @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), LifecycleObserver {
    private val textureView: TextureView = TextureView(context)
    private val exoPlayerView = PlayerView(context)
    private var player = SimpleExoPlayer.Builder(context).build()
    private var lifecycleOwner: LifecycleOwner? = null
    init {
        this.addView(exoPlayerView)
        initPlayer()
    }

    private fun initPlayer() {
        // Build the media item.
        val mediaItem: MediaItem = MediaItem.fromUri("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4".toUri())
        // Set the media item to be played.
        player.setMediaItem(mediaItem)
        // Prepare the player.
        player.prepare()

//        player.setVideoTextureView(textureView)
        exoPlayerView.player = player
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