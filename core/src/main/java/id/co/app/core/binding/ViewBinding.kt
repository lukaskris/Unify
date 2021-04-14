package id.co.app.core.binding

import android.graphics.drawable.Drawable
import android.text.method.LinkMovementMethod
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.text.HtmlCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton

object ViewBinding {
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
    fun bindTitleText(view: TextView, value: String){
        view.text = value.capitalize()
    }

    @JvmStatic
    @BindingAdapter("imageFromUrl")
    fun bindImageFromUrl(view: ImageView, imageUrl: String?) {
        if (!imageUrl.isNullOrEmpty()) {
            Glide.with(view.context)
                .load(imageUrl)
                .transition(DrawableTransitionOptions.withCrossFade())
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
                .transition(DrawableTransitionOptions.withCrossFade())
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
                .transition(DrawableTransitionOptions.withCrossFade())
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