package id.co.app.components.loader

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import id.co.app.components.R
import id.co.app.components.utils.dpToPx

/**
 * Created by Lukas Kristianto on 7/3/2021.
 * App Sinarmas
 * lukas_kristianto@app.co.id
 */
class LoaderUnify : androidx.appcompat.widget.AppCompatImageView {
    var circular : AnimatedVectorDrawableCompat? = null
    var circularWhite : AnimatedVectorDrawableCompat? = null
    var shimmer : AnimatedVectorDrawableCompat? = null

    var avd : AnimatedVectorDrawableCompat? = null

    var cornerRadius: Float = 8f.dpToPx()

    var type: Int = TYPE_CIRCULAR
    set(value) {
        field = value

        when(value) {
            TYPE_CIRCULAR -> {
                if (circular == null) {
                    circular = AnimatedVectorDrawableCompat.create(context, R.drawable.unify_loader)
                }
                avd = circular
            }
            TYPE_CIRCULAR_WHITE -> {
                if (circularWhite == null) {
                    circularWhite = AnimatedVectorDrawableCompat.create(context, R.drawable.unify_loader_bw)
                }
                avd = circularWhite
            }
            else -> {
                if (shimmer == null) {
                    shimmer = AnimatedVectorDrawableCompat.create(context, R.drawable.unify_loader_shimmer)
                }
                avd = shimmer
            }
        }

        applyLoopingAnimatedVectorDrawable()
    }

    private lateinit var rectF: RectF
    private val path = Path()
    private var paint = Paint()

    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        initWithAttr(context, attributeSet)
    }
    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr) {
        initWithAttr(context, attributeSet)
    }

    init {
        scaleType = ScaleType.FIT_XY
//        setLayerType(View.LAYER_TYPE_SOFTWARE, null)

        paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = ContextCompat.getColor(context, R.color.Unify_N0)
            xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
        }
    }

    private fun initWithAttr(context: Context, attributeSet: AttributeSet) {
        val attributeArray = context.obtainStyledAttributes(attributeSet, R.styleable.UnifyLoader)
        type = attributeArray.getInt(R.styleable.UnifyLoader_loader_type, TYPE_CIRCULAR)
        cornerRadius = attributeArray.getDimension(R.styleable.UnifyLoader_loader_corner_radius, cornerRadius)

        attributeArray.recycle()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        rectF = RectF(0f, 0f, w.toFloat(), h.toFloat())
        resetPath()
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
                Canvas.ALL_SAVE_FLAG)
        }
        if (Build.VERSION.SDK_INT > 27) canvas.clipPath(path)
        super.draw(canvas)
        if (Build.VERSION.SDK_INT < 28) canvas.drawPath(path, paint)
        canvas.restoreToCount(save)
    }

    private fun resetPath() {
        path.reset()
        when(type) {
            TYPE_RECT -> path.addRoundRect(rectF, cornerRadius, cornerRadius, Path.Direction.CW)
            TYPE_LINE -> path.addRoundRect(rectF, measuredHeight.toFloat() / 2, measuredHeight.toFloat() / 2, Path.Direction.CW)
            TYPE_CIRCLE -> path.addRoundRect(rectF, measuredHeight.toFloat(), measuredHeight.toFloat(), Path.Direction.CW)
            else -> path.addRoundRect(rectF, 0f, 0f, Path.Direction.CW)
        }

        path.close()
    }

    private fun ImageView.applyLoopingAnimatedVectorDrawable() {
        avd?.registerAnimationCallback(object : Animatable2Compat.AnimationCallback() {
            val mainHandler = Handler(Looper.getMainLooper())
            override fun onAnimationEnd(drawable: Drawable?) {
                mainHandler.post {
                    avd!!.start()
                }
            }
        })
        this.setImageDrawable(avd)
        avd?.start()
    }

    override fun setVisibility(visibility: Int) {
        super.setVisibility(visibility)

        if(visibility == View.VISIBLE){
            applyLoopingAnimatedVectorDrawable()
        }else {
            circular?.stop()
            circular?.clearAnimationCallbacks()

            circularWhite?.stop()
            circularWhite?.clearAnimationCallbacks()

            shimmer?.stop()
            shimmer?.clearAnimationCallbacks()

            avd?.stop()
            avd?.clearAnimationCallbacks()
        }
    }

    override fun onDetachedFromWindow() {
        circular?.stop()
        circular?.clearAnimationCallbacks()

        circularWhite?.stop()
        circularWhite?.clearAnimationCallbacks()

        shimmer?.stop()
        shimmer?.clearAnimationCallbacks()

        avd?.stop()
        avd?.clearAnimationCallbacks()

        super.onDetachedFromWindow()
    }

    override fun onAttachedToWindow() {

        applyLoopingAnimatedVectorDrawable()

        super.onAttachedToWindow()
    }

    companion object {
        const val TYPE_CIRCULAR = 0
        const val TYPE_CIRCULAR_WHITE = 1
        const val TYPE_RECT = 2
        const val TYPE_LINE = 3
        const val TYPE_CIRCLE = 4
    }
}