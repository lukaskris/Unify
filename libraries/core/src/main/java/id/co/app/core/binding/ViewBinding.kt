package id.co.app.core.binding

import android.graphics.drawable.Drawable
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable
import com.google.android.material.floatingactionbutton.FloatingActionButton

object ViewBinding {
    private val factory = DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()

    // This is the placeholder for the imageView
    private val shimmerDrawable = ShimmerDrawable().apply {
        setShimmer(
            Shimmer.AlphaHighlightBuilder()// The attributes for a ShimmerDrawable is set by this builder
                .setBaseAlpha(0.9f) //the alpha of the underlying children
                .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
                .setAutoStart(true)
                .build()
        )
    }

    @JvmStatic
    @BindingAdapter("gone")
    fun bindGone(view: View, shouldBeGone: Boolean) {
        view.visibility = if (shouldBeGone) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    @JvmStatic
    @BindingAdapter("titleText")
    fun bindTitleText(view: TextView, value: String) {
        view.text = value.capitalize()
    }

    @JvmStatic
    @BindingAdapter("imageFromUrl")
    fun bindImageFromUrl(view: ImageView, imageUrl: String?) {
        if (!imageUrl.isNullOrEmpty()) {

            Glide.with(view.context)
                .load(imageUrl)
                .placeholder(shimmerDrawable)
                .transition(withCrossFade(factory))
                .centerCrop()
                .into(view)
        }
    }

    @JvmStatic
    @BindingAdapter("imageFromDrawable")
    fun bindImageFromDrawable(view: ImageView, drawable: Drawable?) {
        if (drawable != null) {
            Glide.with(view.context)
                .load(drawable)
                .transition(withCrossFade())
                .centerCrop()
                .into(view)
        }
    }

    @JvmStatic
    @BindingAdapter("imageFromDrawableCenterInside")
    fun bindImageFromDrawableCenterInside(view: ImageView, drawable: Drawable?) {
        if (drawable != null) {
            Glide.with(view.context)
                .load(drawable)
                .transition(withCrossFade())
                .centerInside()
                .into(view)
        }
    }

    @JvmStatic
    @BindingAdapter("isFabGone")
    fun bindIsFabGone(view: FloatingActionButton, isGone: Boolean?) {
        if (isGone == null || isGone) {
            view.hide()
        } else {
            view.show()
        }
    }

    @JvmStatic
    @BindingAdapter("renderHtml")
    fun bindRenderHtml(view: TextView, description: String?) {
        if (description != null) {
            view.text = HtmlCompat.fromHtml(description, HtmlCompat.FROM_HTML_MODE_COMPACT)
            view.movementMethod = LinkMovementMethod.getInstance()
        } else {
            view.text = ""
        }
    }
}