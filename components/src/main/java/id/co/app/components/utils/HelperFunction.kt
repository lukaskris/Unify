package id.co.app.components.utils

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
import android.net.Uri
import android.os.AsyncTask
import android.text.*
import android.text.style.*
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.animation.TranslateAnimation
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.core.view.ViewCompat
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import id.co.app.components.R
import id.co.app.components.typography.getTypeface
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

/**
 * Created by Lukas Kristianto on 7/3/2021.
 * App Sinarmas
 * lukas_kristianto@app.co.id
 */

// ------------------------------ Text format helper ------------------------------

/**
 * Helper to set text formatting according to unify body text specification
 */
fun TextView.setBodyText(fontLevel: Int = 0, isBold: Boolean = false) {
    val fontType = fontTypeLoader(context, isBold)

    val fontSize = when (fontLevel) {
        1 -> R.dimen.fontSize_lvl4 // 16sp
        2 -> R.dimen.fontSize_lvl3 // 14sp
        3 -> R.dimen.fontSize_lvl2 // 12sp
        else -> R.dimen.fontSize_lvl3 // 14sp
    }

    val fontColor = fontColorList(context)

    setTextColor(fontColor)
    fontType?.run {
        typeface = fontType
    }
    setTextSize(TypedValue.COMPLEX_UNIT_PX, context.resources.getDimension(fontSize))
    setLineSpacing(10f, 1f)

    val paddingValue = 5 // half of line spacing extra
    setPadding(paddingLeft,paddingValue,paddingRight,paddingValue)
}

/**
 * Helper to set text formatting according to unify heading text specification
 */
fun TextView.setHeadingText(fontLevel: Int) {
    val fontType = getTypeface(context, "NunitoSansExtraBold.ttf")
    val fontPaddingExtra = if (fontLevel >= 5) 4f else 6f

    val fontSize: Int = when (fontLevel) {
        1 -> R.dimen.fontSize_lvl8 // 28sp
        2 -> R.dimen.fontSize_lvl6 // 20sp
        3 -> R.dimen.fontSize_lvl5 // 18sp
        4 -> R.dimen.fontSize_lvl4 // 16sp
        5 -> R.dimen.fontSize_lvl3 // 14sp
        6 -> R.dimen.fontSize_lvl2 // 12sp
        else -> R.dimen.fontSize_lvl3 // 14sp
    }

    val fontColor = fontColorList(context)

    setTextColor(fontColor)
    fontType?.run {
        typeface = fontType
    }
    setTextSize(TypedValue.COMPLEX_UNIT_PX, context.resources.getDimension(fontSize))
    setLineSpacing(fontPaddingExtra, 1f)

    val paddingValue = (fontPaddingExtra/2).toInt() // half of line spacing extra
    setPadding(paddingLeft,paddingValue,paddingRight,paddingValue)
}

/**
 * Helper to set text formatting according to unify small text specification
 */
fun TextView.setSmallText(isBold: Boolean = false) {
    val fontType = fontTypeLoader(context, isBold)

    val fontColor = fontColorList(context)

    setTextColor(fontColor)
    fontType?.run {
        typeface = fontType
    }
    setTextSize(TypedValue.COMPLEX_UNIT_PX, context.resources.getDimension(R.dimen.fontSize_lvl1)) // 10sp
    setLineSpacing(4f,1f)

    val paddingValue = 2 // half of line spacing extra
    setPadding(paddingLeft,paddingValue,paddingRight,paddingValue)
}

/**
 *  Regular font type loader, load between bold and normal
 */
fun fontTypeLoader(context: Context, isBold: Boolean): Typeface? {
    return if (isBold) {
        getTypeface(context, "NotoSansBold.ttf")
    } else {
        getTypeface(context, "NotoSansRegular.ttf")
    }
}

/**
 *  Set color state list for text color according to unify text specification
 */
fun fontColorList(context: Context): ColorStateList {
    val stateList = arrayOf(
        IntArray(1) { android.R.attr.state_enabled },
        IntArray(1) { -android.R.attr.state_enabled }
    )

    val colorList = IntArray(2)
    colorList[0] = ContextCompat.getColor(context, R.color.Unify_N700_96)
    colorList[1] = ContextCompat.getColor(context, R.color.Unify_N700_20)

    return ColorStateList(stateList, colorList)
}

/**
 * Text formatter for timer add 0 when value below 9
 * 8 -> 08
 * 7 -> 07
 */
fun timeFormatter(value: Int): String{
    return if(value < 10){
        "0$value"
    } else {
        value.toString()
    }
}


// ------------------------------ Background shadow generator helper ------------------------------

/**
 *  Helper to generate drawable with shadow
 */
fun generateBackgroundWithShadow(
    view: View, @ColorRes backgroundColor: Int,
    @DimenRes borderRadius: Int,
    @ColorRes shadowColor: Int,
    @DimenRes elevation: Int,
    shadowGravity: Int,
    outerRadius: FloatArray = FloatArray(0)
): Drawable {
    val borderRadiusValue = view.context.resources.getDimension(borderRadius)
    val elevationValue = view.context.resources.getDimension(elevation).toInt()
    val shadowColorValue = ContextCompat.getColor(view.context, shadowColor)
    val backgroundColorValue = ContextCompat.getColor(view.context, backgroundColor)

    val outerRadius = if(outerRadius.isNotEmpty()) outerRadius else FloatArray(8){borderRadiusValue}

    val backgroundPaint = Paint()
    backgroundPaint.style = Paint.Style.FILL
    backgroundPaint.setShadowLayer(borderRadiusValue, 0F, 0F, 0)

    val DY: Int = when (shadowGravity) {
        Gravity.CENTER -> {
            0
        }
        Gravity.TOP -> {
            -1 * elevationValue / 3
        }
        Gravity.BOTTOM -> {
            elevationValue / 3
        }
        else -> {
            elevationValue / 3
        }
    }

    val shapeDrawable = ShapeDrawable()

    shapeDrawable.paint.color = backgroundColorValue
    shapeDrawable.paint.setShadowLayer(4F, 0F, DY.toFloat(), shadowColorValue)

    view.setLayerType(View.LAYER_TYPE_SOFTWARE, shapeDrawable.paint)

    shapeDrawable.shape = RoundRectShape(outerRadius, null, null)

    val drawable = LayerDrawable(arrayOf<Drawable>(shapeDrawable))
    drawable.setLayerInset(0, elevationValue, elevationValue, elevationValue, elevationValue)

    return drawable
}


// ------------------------------ Type converter helper ------------------------------
fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()

fun Int.toDp(): Int = (this / Resources.getSystem().displayMetrics.density).toInt()

fun Float.dpToPx(): Float = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, Resources.getSystem().displayMetrics)

fun Float.spToPx(): Float = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, this, Resources.getSystem().displayMetrics)


// ------------------------------ Loader helper ----------------------------------------
/**
 * Helper for getting bitmap from URL
 */
class ImageUrlLoader : AsyncTask<Any, Void, Bitmap>() {
    lateinit var onImageLoaded: (result: Bitmap?) -> Unit

    override fun doInBackground(vararg params: Any): Bitmap? {

        return try {
            val url = URL(params[0].toString())
            val connection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input = connection.inputStream
            onImageLoaded = params[1] as (Bitmap?) -> Unit
            BitmapFactory.decodeStream(input)
        } catch (e: IOException) {
            null
        }
    }

    override fun onPostExecute(result: Bitmap?) {
        result?.let {
            this.onImageLoaded(result)
        }
    }
}


// ------------------------------ Other ------------------------------
fun View?.findSuitableParent(): ViewGroup? {
    var view = this
    var fallback: ViewGroup? = null
    do {
        if (view is androidx.coordinatorlayout.widget.CoordinatorLayout) {
            // We've found a CoordinatorLayout, use it
            return view
        } else if (view is FrameLayout) {
            if (view.id == android.R.id.content) {
                // If we've hit the decor content view, then we didn't find a CoL in the
                // hierarchy, so use it.
                return view
            } else {
                // It's not the content view but we'll use it as our fallback
                fallback = view
            }
        }

        if (view != null) {
            // Else, we will loop and crawl up the view hierarchy and try to find a parent
            val parent = view.parent
            view = if (parent is View) parent else null
        }
    } while (view != null)

    // If we reach here then we didn't find a CoL or a suitable content view so we'll fallback
    return fallback
}

/**
 * Helper to load url image into ImageView with custom corner radius
 * @param url Sets a drawable from url as the content of this ImageView.
 * @param cornerRadius for custom corner radius of this ImageView
 */
@Deprecated("Please use ImageUnify instead of ImageView")
fun ImageView.setImage(url: String, cornerRadius: Float) {
    val iv : ImageView = this
    Glide.with(context).asBitmap().load(url).error(R.drawable.ic_broken_image).fitCenter().into(object: BitmapImageViewTarget(iv) {
        override fun setResource(resource: Bitmap?) {
            val circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context.resources, resource)
            circularBitmapDrawable.cornerRadius = cornerRadius

            iv.setImageDrawable(circularBitmapDrawable)
        }
    })
}

/**
 * Helper to load drawable image into ImageView with custom corner radius
 * @param drawable Sets a drawable as the content of this ImageView.
 * @param cornerRadius for custom corner radius of this ImageView
 */
@Deprecated("Please use ImageUnify instead of ImageView")
fun ImageView.setImage(drawable: Int, cornerRadius: Float) {
    val iv : ImageView = this
    Glide.with(context).asBitmap().load(drawable).error(R.drawable.ic_broken_image).fitCenter().into(object: BitmapImageViewTarget(iv) {
        override fun setResource(resource: Bitmap?) {
            val circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context.resources, resource)
            circularBitmapDrawable.cornerRadius = cornerRadius

            iv.setImageDrawable(circularBitmapDrawable)
        }
    })
}

/**
 * Helper to style anchor tags based on unify spec and to manage click event for each link
 * @param context
 * @param htmlString the HTML string to be modified
 */
class HtmlLinkHelper(context: Context, htmlString: String) {
    var spannedString: CharSequence? = null
    var urlList: MutableList<UrlLinkManager> = ArrayList()

    init {
        val spannedHtmlString: Spanned =
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N)
                Html.fromHtml(htmlString, Html.FROM_HTML_MODE_COMPACT)
            else
                Html.fromHtml(htmlString)

        val spanHandler = SpannableStringBuilder(spannedHtmlString)
        val urlListArr = spanHandler.getSpans(0, spannedHtmlString.length, URLSpan::class.java)
        val styleSpanArr = spanHandler.getSpans(0, spannedHtmlString.length, StyleSpan::class.java)
        val boldSpanArr: MutableList<StyleSpan> = mutableListOf()
        styleSpanArr.forEach {
            if (it.style == Typeface.BOLD) {
                boldSpanArr.add(it)
            }
        }

        boldSpanArr.forEach {
            val boldStart = spanHandler.getSpanStart(it)
            val boldEnd = spanHandler.getSpanEnd(it)

            spanHandler.setSpan(UnifyCustomTypefaceSpan(getTypeface(context, "NotoSansBold.ttf")), boldStart, boldEnd, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        }

        urlListArr.forEachIndexed { index, it ->
            urlList.add(UrlLinkManager())
            val linkStart = spanHandler.getSpanStart(it)
            val linkEnd = spanHandler.getSpanEnd(it)
            val linkText = spanHandler.substring(linkStart, linkEnd)
            val linkUrl = it.url

            urlList[index].linkText = linkText
            urlList[index].linkUrl = linkUrl

            spanHandler.removeSpan(it)
            spanHandler.setSpan(object : ClickableSpan() {
                override fun onClick(p0: View) {
                    if (urlList[index].onClick === null) {
                        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(linkUrl))
                        context.startActivity(browserIntent)
                    } else {
                        urlList[index].onClick?.invoke()
                    }
                }
            }, linkStart, linkEnd, Spanned.SPAN_EXCLUSIVE_INCLUSIVE)
            spanHandler.setSpan(UrlSpanNoUnderline(linkUrl), linkStart, linkEnd, Spanned.SPAN_EXCLUSIVE_INCLUSIVE)
            spanHandler.setSpan(ForegroundColorSpan(context.resources.getColor(R.color.Unify_G500)), linkStart, linkEnd, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
            spanHandler.setSpan(StyleSpan(Typeface.BOLD), linkStart, linkEnd, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
        }

        spannedString = spanHandler
    }
}

class UrlSpanNoUnderline(url: String): URLSpan(url) {
    override fun updateDrawState(ds: TextPaint) {
        super.updateDrawState(ds)
        ds.isUnderlineText = false
    }
}

class UnifyCustomTypefaceSpan(private val typeface: Typeface?) : MetricAffectingSpan() {
    override fun updateDrawState(paint: TextPaint) {
        paint.typeface = typeface
    }

    override fun updateMeasureState(paint: TextPaint) {
        paint.typeface = typeface
    }
}

class UrlLinkManager {
    var linkText: String = ""
    var linkUrl: String = ""
    var onClick: (() -> Unit)? = null

    fun setOnClickListener(unit: () -> Unit) {
        onClick = unit
    }
}

/**
 * Helper to slide the view from above to its current position
 * @param view View to be animate
 * @param duration animation duration
 */
fun slideDown(view: View, duration: Long) {
    val animate = TranslateAnimation(
        0f, // fromXDelta
        0f, // toXDelta
        -view.height.toFloat(), // fromYDelta
        0f // toYDelta
    )
    animate.duration = duration
    animate.fillAfter = true
    view.startAnimation(animate)
}

fun View.fade(value: Float, ms: Long) {
    this.animate().alpha(value).setDuration(ms).start()
}

fun isUsingNightModeResources(): Boolean {
    return AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
}

fun View.makeMeasureSpec() {
    measure(
        View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
    )
}

inline fun <reified T : ViewGroup.LayoutParams> View.doOnLayoutWithHeight(view: View) {
    view.makeMeasureSpec()
    doOnLayoutWithHeight<T>(view.measuredHeight)
}

inline fun <reified T : ViewGroup.LayoutParams> View.doOnLayoutWithHeight(newMeasuredHeight: Int) {
    if (layoutParams.height != measuredHeight) {
        layoutParams = (layoutParams as T).also { lp -> lp.height = newMeasuredHeight }
    }
}

fun View.setMargins(
    leftMarginDp: Int? = null,
    topMarginDp: Int? = null,
    rightMarginDp: Int? = null,
    bottomMarginDp: Int? = null
) {
    if (layoutParams is ViewGroup.MarginLayoutParams) {
        val params = layoutParams as ViewGroup.MarginLayoutParams
        leftMarginDp?.run { params.leftMargin = this.dpToPx(context) }
        topMarginDp?.run { params.topMargin = this.dpToPx(context) }
        rightMarginDp?.run { params.rightMargin = this.dpToPx(context) }
        bottomMarginDp?.run { params.bottomMargin = this.dpToPx(context) }
        requestLayout()
    }
}

fun Int.dpToPx(context: Context): Int {
    val metrics = context.resources.displayMetrics
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), metrics).toInt()
}


fun ViewPager2.setShowSideItems(pageMarginPx : Int, offsetPx : Int) {

    clipToPadding = false
    clipChildren = false
    offscreenPageLimit = 3

    setPageTransformer { page, position ->

        val offset = position * -(2 * offsetPx + pageMarginPx)
        if (this.orientation == ViewPager2.ORIENTATION_HORIZONTAL) {
            if (ViewCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_RTL) {
                page.translationX = -offset
            } else {
                page.translationX = offset
            }
        } else {
            page.translationY = offset
        }
    }

}