package id.co.app.components.carousell

import android.util.TypedValue
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import id.co.app.components.R
import id.co.app.components.image.ImageUnify
import id.co.app.components.utils.setMargins


/**
 * Created by Lukas Kristianto on 27/08/21 14.53.
 * Sinarmas APP
 * lukas_kristianto@app.co.id
 */
class CarouselAdapter : RecyclerView.Adapter<CarouselAdapter.CarouselViewHolder>() {
    private val data = mutableListOf<CarouselItem>()
    private var radius = 0
    private var marginAndOffset = 0
    private var isCenter = false
    var listener: CarouselClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselViewHolder {
        val frameLayout = FrameLayout(parent.context)
        val layoutParams = ViewGroup.MarginLayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        frameLayout.layoutParams = layoutParams

        val image = ImageUnify(parent.context)
        val outValue = TypedValue()
        parent.context.theme.resolveAttribute(
            android.R.attr.selectableItemBackground,
            outValue,
            true
        )
        image.id = R.id.carousel_image
        image.layoutParams = layoutParams
        image.foreground = ContextCompat.getDrawable(parent.context, outValue.resourceId)
        image.isClickable = true
        image.isFocusable = true

        val textView = TextView(parent.context)
        textView.layoutParams = layoutParams
        textView.id = R.id.carousel_text
        textView.setTextColor(ContextCompat.getColor(parent.context, R.color.Unify_N0))
        textView.textSize = parent.context.resources.getDimension(R.dimen.fontSize_lvl4)

        frameLayout.addView(image)
        frameLayout.addView(textView)

        return CarouselViewHolder(frameLayout, image)
    }

    override fun onBindViewHolder(holder: CarouselViewHolder, position: Int) {
        holder.bind(data[position], position)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setRadius(radius: Int) {
        this.radius = radius
    }

    fun setCenter(isCenter: Boolean) {
        this.isCenter = isCenter
    }

    fun setMargin(marginAndOffset: Int) {
        this.marginAndOffset = marginAndOffset
    }

    fun submitList(
        list: List<CarouselItem>
    ) {
        this.data.clear()
        this.data.addAll(list)
        notifyDataSetChanged()
    }

    inner class CarouselViewHolder(
        frameLayout: FrameLayout,
        private val view: ImageUnify
    ) : RecyclerView.ViewHolder(frameLayout) {
        fun bind(item: CarouselItem, position: Int) {
            if (isCenter) {
                view.setMargins(50, 0, 50, 0)
            }
//            frameLayout.findViewById<TextView>(R.id.carousel_text).text = position.toString()
            view.cornerRadius = radius
            view.setOnClickListener {
                listener?.onBannerClick(item)
            }
            if (item.url.isNotEmpty()) {
                view.setImageUrl(item.url, isSkipCache = false)
            } else if (item.res != -1) {
                view.setImageDrawable(ContextCompat.getDrawable(view.context, item.res))
            }
        }
    }
}