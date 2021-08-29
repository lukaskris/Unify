package id.co.app.components.carousell.pagetransformer

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import id.co.app.components.R


/**
 * Created by Lukas Kristianto on 27/08/21 19.13.
 * Sinarmas APP
 * lukas_kristianto@app.co.id
 */

class CardMarginPageTransformer : ViewPager2.PageTransformer {
    private var offset = 0f

    override fun transformPage(page: View, position: Float) {
        val pageMargin = page.resources.getDimensionPixelOffset(R.dimen.spacing_lvl5).toFloat()
        val pageOffset = page.resources.getDimensionPixelOffset(R.dimen.spacing_lvl3).toFloat()
        val newOffset = position * -(2 * pageOffset + pageMargin)
        if (offset == 0f) {
            val cardWidth = page.width
//            offset = -page.width / 2f + ((cardWidth - 100) * 0.8f)
            offset = newOffset
        }
        val myOffset = position * -(2 * pageOffset + pageMargin)
        when {
            position < -1 -> {
                page.translationX = -myOffset
            }
            position <= 1 -> {
                val scaleFactor = 0.7f.coerceAtLeast(1 - Math.abs(position - 0.14285715f))
                page.translationX = myOffset
//                page.scaleY = scaleFactor
//                page.alpha = scaleFactor
            }
            else -> {
//                page.alpha = 0f
                page.translationX = myOffset
            }
        }

//        page.translationX = offset * position
    }
}