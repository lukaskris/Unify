package id.co.app.components.image

import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import id.co.app.components.R
import id.co.app.components.icon.IconUnify
import id.co.app.components.icon.getIconUnifyDrawable
import id.co.app.components.utils.toDp
import id.co.app.components.utils.toPx
import java.io.File
import java.net.URI
import java.util.*

/**
 * Created by Lukas Kristianto on 15/07/21 09.19.
 * App Sinarmas
 * lukas_kristianto@app.co.id
 */

class ImageUnify : AppCompatImageView {

    var type: Int = TYPE_RECT
    var cornerRadius: Int = 8
    var heightRatio: Float? = null
    var urlSrc: String = ""
        set(value) {
            field = value

            if (urlSrc.isNotEmpty()) {
                setImageUrl(value)
            }
        }

    var onUrlLoaded: ((isSuccess: Boolean) -> Any)? = null
    var isGif: Boolean? = null

    var customLoadingAVD: AnimatedVectorDrawableCompat? = null
    var disableShimmeringPlaceholder: Boolean = false
    private var shimmeringPlaceholder: AnimatedVectorDrawableCompat? = null
    private var placeholder: Int = 0

    private lateinit var rectF: RectF
    private val path = Path()
    private var paint = Paint()
    private var isMeasured = false
    private var isImageLoaded = false
    private var isLoadError = false
    private var hasImageUrl = false
    private var scaleTypeConfigOnDraw = false
    private var userPrefScaleType: ScaleType? = ScaleType.FIT_XY
    private val errorDrawable = LayerDrawable(arrayOf(AppCompatResources.getDrawable(context, R.drawable.imagestate_error)))
    private var shimmerDrawable: AnimatedVectorDrawableCompat? = null
    private var prevWidth = 0
    private var isAttached = false
    private var url: String = ""
    private var placeholderHeight: Int? = null
    private var isSkipCache: Boolean = false

    private val reloadIcon = getIconUnifyDrawable(
        context,
        IconUnify.RELOAD,
        ContextCompat.getColor(context, R.color.Unify_N0)
    )
    private val reloadDrawable = LayerDrawable(
        arrayOf(
            AppCompatResources.getDrawable(
                context,
                R.drawable.ic_imagestate_reload
            ), reloadIcon
        )
    )
    private val defaultPlaceholderDrawable = LayerDrawable(arrayOf(AppCompatResources.getDrawable(context, R.drawable.imagestate_placeholder)))
    private val defaultBackgroundDrawable =
        ColorDrawable(ContextCompat.getColor(context, R.color.Unify_N75))

    var isRetryable = false

    var initialWidth: Int? = null
        set(value) {
            field = value

            if (value != null) {
                val h = heightRatio
                if (h != null) {
                    this.layoutParams.height = (value * h.toInt())

                    this.requestLayout()
                }
            }
        }

    constructor(context: Context) : super(context) {
        initPlaceholder()
    }

    constructor(context: Context, placeholder: Int) : super(context) {
        this.placeholder = placeholder
        initPlaceholder()
    }

    constructor(
        context: Context,
        customLoadingAvd: AnimatedVectorDrawableCompat?
    ) : super(context) {
        this.customLoadingAVD = customLoadingAvd
        initPlaceholder()
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        initWithAttr(context, attributeSet)
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(
        context,
        attributeSet,
        defStyleAttr
    ) {
        initWithAttr(context, attributeSet)
    }

    init {
        paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = ContextCompat.getColor(context, R.color.Unify_N0)
            xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
        }
    }

    private fun initWithAttr(context: Context, attributeSet: AttributeSet) {
        val attributeArray = context.obtainStyledAttributes(attributeSet, R.styleable.UnifyImage)
        val androidAttributeArray = intArrayOf(
            android.R.attr.scaleType
        )
        val scaleType = context.obtainStyledAttributes(attributeSet, androidAttributeArray).getInt(0, -1)
        if (scaleType != -1) {
            userPrefScaleType = ScaleType.values()[scaleType]
        }
        type = attributeArray.getInt(R.styleable.UnifyImage_unify_image_type, TYPE_RECT)
        cornerRadius = attributeArray.getInt(R.styleable.UnifyImage_unify_image_corner_radius, 8)
        placeholder =
            attributeArray.getResourceId(R.styleable.UnifyImage_unify_image_placeholder, 0)
        var attrCustomLoadingAvd =
            attributeArray.getResourceId(R.styleable.UnifyImage_unify_image_custom_loading_avd, 0)

        if (attrCustomLoadingAvd != 0) {
            customLoadingAVD = AnimatedVectorDrawableCompat.create(context, attrCustomLoadingAvd)
        }

        disableShimmeringPlaceholder = attributeArray.getBoolean(R.styleable.UnifyImage_unify_image_disable_shimmering_placeholder, false)

        initPlaceholder()
        urlSrc = attributeArray.getString(R.styleable.UnifyImage_unify_image_url_src) ?: ""

        attributeArray.recycle()
    }

    private fun initPlaceholder() {
        if (placeholder == 0 && drawable == null) {
            setImageDrawable(defaultPlaceholderDrawable)
            background = defaultBackgroundDrawable
        } else {
            setBackgroundResource(placeholder)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        rectF = RectF(0f, 0f, w.toFloat(), h.toFloat())
        resetPath()

        // reload dimension is 32dp for small image (< 256dp)
        var reloadPaddingH = measuredWidth / 2 - 16.toPx()
        var reloadIconPaddingH = measuredWidth / 2 - 12.toPx()
        var reloadPaddingV = measuredHeight / 2 - 16.toPx()
        var reloadIconPaddingV = measuredHeight / 2 - 12.toPx()

        if (measuredWidth.toDp() > 256 && measuredHeight.toDp() > 256) {
            // reload dimension is 48dp for large image (< 256dp)
            reloadPaddingH = measuredWidth / 2 - 24.toPx()
            reloadIconPaddingH = measuredWidth / 2 - 16.toPx()
            reloadPaddingV = measuredHeight / 2 - 24.toPx()
            reloadIconPaddingV = measuredHeight / 2 - 16.toPx()

            errorDrawable.setLayerInset(0,
                measuredWidth / 2 - 128.toPx(),
                measuredHeight / 2 - 128.toPx(),
                measuredWidth / 2 - 128.toPx(),
                measuredHeight / 2 - 128.toPx()
            )

            defaultPlaceholderDrawable.setLayerInset(0,
                measuredWidth / 2 - 128.toPx(),
                measuredHeight / 2 - 128.toPx(),
                measuredWidth / 2 - 128.toPx(),
                measuredHeight / 2 - 128.toPx()
            )
        } else if (measuredWidth.toDp() <= 256 || measuredHeight.toDp() <= 256){
            if (measuredWidth <= measuredHeight) {
                errorDrawable.setLayerInset(0,
                    0,
                    measuredHeight / 2 - measuredWidth / 2,
                    0,
                    measuredHeight / 2 - measuredWidth / 2
                )
                defaultPlaceholderDrawable.setLayerInset(0,
                    0,
                    measuredHeight / 2 - measuredWidth / 2,
                    0,
                    measuredHeight / 2 - measuredWidth / 2
                )
            } else {
                errorDrawable.setLayerInset(0,
                    measuredWidth / 2 - measuredHeight / 2,
                    0,
                    measuredWidth / 2 - measuredHeight / 2,
                    0
                )
                defaultPlaceholderDrawable.setLayerInset(0,
                    measuredWidth / 2 - measuredHeight / 2,
                    0,
                    measuredWidth / 2 - measuredHeight / 2,
                    0
                )
            }
        }

        reloadDrawable.setLayerInset(0, reloadPaddingH, reloadPaddingV, reloadPaddingH, reloadPaddingV)
        reloadDrawable.setLayerInset(1, reloadIconPaddingH, reloadIconPaddingV, reloadIconPaddingH, reloadIconPaddingV)
    }

    override fun draw(canvas: Canvas) {
        val save = when {
            /**
             * saveLayer without flag was added in API 21
             */
            Build.VERSION.SDK_INT >= 21 -> canvas.saveLayer(
                RectF(0f, 0f, width.toFloat(), height.toFloat()),
                null
            )
            /**
             * saveLayer with flag is deprecated in API 26
             */
            else -> canvas.saveLayer(
                RectF(0f, 0f, width.toFloat(), height.toFloat()),
                null,
                Canvas.ALL_SAVE_FLAG
            )
        }
        if (Build.VERSION.SDK_INT > 27) canvas.clipPath(path)
        super.draw(canvas)
        if (Build.VERSION.SDK_INT < 28) canvas.drawPath(path, paint)
        canvas.restoreToCount(save)
    }

    private fun resetPath() {
        path.reset()
        when (type) {
            TYPE_RECT -> path.addRoundRect(
                rectF,
                cornerRadius.toPx().toFloat(),
                cornerRadius.toPx().toFloat(),
                Path.Direction.CW
            )
            TYPE_CIRCLE -> path.addRoundRect(
                rectF,
                measuredHeight.toFloat(),
                measuredHeight.toFloat(),
                Path.Direction.CW
            )
            else -> path.addRoundRect(rectF, 0f, 0f, Path.Direction.CW)
        }

        path.close()
    }

    fun setGif(drawable: Int) {
        Glide.with(context).asGif().load(drawable).into(this)
    }

    fun setImageUrl(url: String, heightRatio: Float? = null, placeholderHeight: Int? = null, isSkipCache: Boolean = false) {
        this.url = url
        this.placeholderHeight = placeholderHeight
        this.isSkipCache = isSkipCache
        this.heightRatio = heightRatio
        this.post {
            heightRatio?.let {
                configHeightRatio(it)
            }

            startShimmer()
            hasImageUrl = true

            if (placeholderHeight != null) {
                this.layoutParams.height = placeholderHeight
            }

            val ext = extReader(url)

            when (isGif) {
                true -> {
                    loadGif(url, placeholderHeight)
                }
                false -> {
                    loadImage(url, placeholderHeight, isSkipCache)
                }
                else -> {
                    if (ext.isNotEmpty() && (ext == "gif" || ext == "gifv")) {
                        loadGif(url, placeholderHeight)
                    } else {
                        loadImage(url, placeholderHeight, isSkipCache)
                    }
                }
            }
        }
    }

    private fun applyLoopingAnimatedVectorDrawable() {
        shimmeringPlaceholder?.registerAnimationCallback(object :
            Animatable2Compat.AnimationCallback() {
            val mainHandler = Handler(Looper.getMainLooper())
            override fun onAnimationEnd(drawable: Drawable?) {
                mainHandler.post {
                    shimmeringPlaceholder!!.start()
                }
            }
        })
        shimmeringPlaceholder?.start()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if(drawable == defaultPlaceholderDrawable){
            scaleTypeConfigOnDraw = true
            scaleType = ScaleType.FIT_XY
            scaleTypeConfigOnDraw = false
        }

        if (drawable != null && drawable != defaultPlaceholderDrawable && drawable != errorDrawable && drawable != reloadDrawable) {
            this.background = null
            this.scaleType = userPrefScaleType
        }

        if (measuredWidth != prevWidth && heightRatio != null) {
            configHeightRatio(heightRatio!!)
            prevWidth = measuredWidth
        }
    }

    private fun onError() {
        onUrlLoaded?.invoke(false)
        this@ImageUnify.isImageLoaded = true
        this@ImageUnify.isLoadError = true
        this.background = defaultBackgroundDrawable
    }

    private fun loadGif(url: String, placeholderHeight: Int?) {
        if(!isAttached) return
        val mRequestBuilder: RequestBuilder<GifDrawable> = Glide.with(context).asGif()

        if (heightRatio == null) mRequestBuilder.dontTransform()

        mRequestBuilder.load(url).error(R.drawable.ic_broken_image)
            .listener(object : RequestListener<GifDrawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<GifDrawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    clearShimmerAnimation()

                    onError()
                    return false
                }

                override fun onResourceReady(
                    resource: GifDrawable?,
                    model: Any?,
                    target: Target<GifDrawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    clearShimmerAnimation()

                    onUrlLoaded?.invoke(true)
                    this@ImageUnify.isImageLoaded = true
                    this@ImageUnify.scaleType = userPrefScaleType

                    if (resource != null) {
                        renderSource(
                            resource.intrinsicHeight,
                            resource.intrinsicHeight,
                            placeholderHeight
                        )
                    }
                    return false
                }
            })
            .into(this)
    }

    private fun loadImage(url: String, placeholderHeight: Int?, isSkipCache: Boolean) {
        if(!isAttached) return
        val mRequestBuilder: RequestBuilder<Bitmap> = Glide.with(context).asBitmap()

        if (heightRatio == null) mRequestBuilder.dontTransform()

        mRequestBuilder.load(url).error(if (isRetryable) reloadDrawable else errorDrawable)
            .listener(object : RequestListener<Bitmap> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Bitmap>?,
                    isFirstResource: Boolean
                ): Boolean {
                    clearShimmerAnimation()

                    onError()
                    return false
                }

                override fun onResourceReady(
                    resource: Bitmap?,
                    model: Any?,
                    target: Target<Bitmap>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    clearShimmerAnimation()

                    onUrlLoaded?.invoke(true)
                    this@ImageUnify.isImageLoaded = true
                    this@ImageUnify.scaleType = userPrefScaleType

                    if (resource != null) {
                        renderSource(resource.width, resource.height, placeholderHeight)
                    }
                    return false
                }
            })
            .skipMemoryCache(isSkipCache)
            .diskCacheStrategy(if(isSkipCache) DiskCacheStrategy.NONE else DiskCacheStrategy.AUTOMATIC)
            .into(this)
    }

    private fun renderSource(w: Int, h: Int, placeholderHeight: Int?) {
        val hRatio: Float =
            (h.toDouble() / w.toDouble()).toFloat()
        if (this@ImageUnify.layoutParams.height == ViewGroup.LayoutParams.WRAP_CONTENT || placeholderHeight != null) {
            this@ImageUnify.post {
                this@ImageUnify.layoutParams.height =
                    (this@ImageUnify.measuredWidth * hRatio).toInt()
                this@ImageUnify.requestLayout()
            }
        }
    }

    private fun clearShimmerAnimation() {
        background = null
        shimmeringPlaceholder?.stop()
        shimmeringPlaceholder?.clearAnimationCallbacks()
    }

    private fun extReader(url: String): String {
        val uri: URI? = try {
            URI(url)
        } catch (e: Exception) {
            null
        }

        return try {
            File(uri?.path).extension.toLowerCase(Locale.ENGLISH)
        } catch (e: Exception) {
            ""
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        isAttached = true

        if (url.isNotEmpty()) {
            setImageUrl(url, heightRatio, placeholderHeight, isSkipCache)
        }
    }

    override fun onDetachedFromWindow() {
        clearShimmerAnimation()

        super.onDetachedFromWindow()

        isAttached = false
    }

    override fun setScaleType(scaleType: ScaleType?) {
        super.setScaleType(scaleType)

        if (!scaleTypeConfigOnDraw) {
            userPrefScaleType = scaleType
        }
    }

    private fun configHeightRatio(h: Float) {
        heightRatio = h
        layoutParams.height = (measuredWidth * h).toInt()
        requestLayout()
        isMeasured = true
    }

    private fun startShimmer() {
        shimmerDrawable = shimmerDrawable ?: AnimatedVectorDrawableCompat.create(
            context,
            R.drawable.unify_loader_shimmer
        )

        shimmeringPlaceholder = customLoadingAVD ?: shimmerDrawable

        if (customLoadingAVD != null || !disableShimmeringPlaceholder) {
            background = shimmeringPlaceholder
            applyLoopingAnimatedVectorDrawable()
        } else {
            background = defaultBackgroundDrawable
        }

    }

    companion object {
        const val TYPE_RECT = 0
        const val TYPE_CIRCLE = 1
    }
}