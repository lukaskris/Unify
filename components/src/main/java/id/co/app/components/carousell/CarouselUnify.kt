package id.co.app.components.carousell

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.ORIENTATION_HORIZONTAL
import androidx.viewpager2.widget.ViewPager2.SCROLL_STATE_DRAGGING
import id.co.app.components.R
import id.co.app.components.pagecontrol.PageControl
import id.co.app.components.utils.CountDownTimerExt
import id.co.app.components.utils.setBodyText
import id.co.app.components.utils.setShowSideItems
import id.co.app.components.utils.toPx


/**
 * Created by Lukas Kristianto on 27/08/21 14.50.
 * Sinarmas APP
 * lukas_kristianto@app.co.id
 */
class CarouselUnify(context: Context, attributeSet: AttributeSet) :
    LinearLayout(context, attributeSet) {
    private val carouselAdapter by lazy { CarouselAdapter() }
    val viewPager by lazy {
        ViewPager2(context, attributeSet).apply {
            orientation = ORIENTATION_HORIZONTAL
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )

            adapter = carouselAdapter
        }
    }

    val indicatorWrapper = RelativeLayout(context)
    val indicator = PageControl(context)
    val actionTextView: TextView = TextView(context)

    var cornerRadius: Int = 0
    var centerMode: Boolean = false
        set(value) {
            with(viewPager) {
                clipToPadding = false
                clipChildren = false
                offscreenPageLimit = 3

                if (value) {
                    carouselAdapter.setRadius(16)
                    carouselAdapter.setCenter(value)
                    setShowSideItems(
                        resources.getDimensionPixelOffset(R.dimen.pageMargin),
                        resources.getDimensionPixelOffset(R.dimen.pagerOffset)
                    )

                } else {
                    setPageTransformer(null)
                }
            }
            field = value
        }
    var bannerItemMargin: Int = 8.toPx()
    var indicatorPosition: String = INDICATOR_BC
        set(value) {
            field = value
            initIndicator()
        }

    var actionTextPosition: String = INDICATOR_BC
        set(value) {
            field = value
            initIndicator()
        }

    var timer = object : CountDownTimerExt(3000, 1000) {
        override fun onTimerTick(millisUntilFinished: Long) {

        }

        override fun onTimerFinish() {
            viewPager.currentItem =
                if (!infinite && viewPager.currentItem + 1 == carouselAdapter.itemCount) 0 else viewPager.currentItem + 1
        }

    }

    var autoplayDuration: Long = 3000
    var autoplay: Boolean = false

    var actionText: CharSequence? = null
        set(value) {
            field = value

            initIndicator()
        }

    var freeMode: Boolean = false
    var infinite: Boolean = false
        set(value) {
            if (value) {
                with(viewPager) {
                    clipToPadding = false
                    clipChildren = false
                    offscreenPageLimit = 3
                }
            }
            field = value
        }

    var listener: CarouselClickListener? = null
        set(value) {
            carouselAdapter.listener = value
            field = value
        }

    init {
        orientation = VERTICAL
        addView(viewPager)
        indicatorWrapper.addView(indicator)
        indicatorWrapper.addView(actionTextView)
        initIndicator()

        viewPager.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    if (autoplay) {
                        timer.restart()
                    }

                    indicator.setCurrentIndicator(
                        if (infinite && !centerMode) when (position) {
                            0 -> carouselAdapter.itemCount - 2
                            carouselAdapter.itemCount - 1 -> 0
                            else -> viewPager.currentItem - 1
                        } else if (infinite && centerMode) when (position) {
                            1 -> carouselAdapter.itemCount - 5
                            carouselAdapter.itemCount - 2 -> 0
                            else -> viewPager.currentItem - 2
                        } else position
                    )
                }

                override fun onPageScrollStateChanged(state: Int) {
                    super.onPageScrollStateChanged(state)
                    if (!autoplay) return
                    if (state == SCROLL_STATE_DRAGGING) {
                        timer.pause()
                    } else {
                        timer.start()
                    }
                }
            }
        )
    }

    fun addData(list: List<CarouselItem>) {
        val newList = if (infinite && list.size > 1) {
            (if (centerMode && list.size > 1) listOf(
                list[list.size - 2],
                list.last()
            ) else listOf(list.last())) +
                    list +
                    (if (centerMode && list.size > 1) listOf(
                        list.first(),
                        list[1],
                    ) else listOf(list.first()))
        } else list
        carouselAdapter.submitList(newList)

        if (infinite && !centerMode) viewPager.setCurrentItem(1, false)
        else if (infinite && centerMode) viewPager.setCurrentItem(2, false)
        else viewPager.setCurrentItem(0, false)

        indicator.setIndicator(list.size)
        indicator.setCurrentIndicator(0)

        (viewPager.getChildAt(0) as RecyclerView).addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!infinite) return
                val layoutManager = (recyclerView.layoutManager as LinearLayoutManager)
                val firstItemVisible = layoutManager.findFirstVisibleItemPosition()
                val lastItemVisible = layoutManager.findLastVisibleItemPosition()
//                Log.d("Lukas", "First Item: $firstItemVisible | dx: $dx")
//                Log.d("Lukas", "Last Item: $lastItemVisible | dx: $dx")
                if (!centerMode) {
                    if (firstItemVisible == newList.size - 1 && dx > 0) {
                        recyclerView.scrollToPosition(1)
                    } else if (lastItemVisible == 0 && dx < 0) {
                        recyclerView.scrollToPosition(newList.size - 2)
                    }
                } else {
                    if (firstItemVisible == (newList.size - 2) && dx > 0) {
                        recyclerView.scrollToPosition(2)
                    } else if (lastItemVisible == 1 && dx < 0) {
                        recyclerView.scrollToPosition(newList.size - 3)
                    }
                }
            }
        })
    }

    private fun initIndicator() {

        (indicator.parent as RelativeLayout).removeView(indicator)
        if (actionTextView.parent != null) {
            (actionTextView.parent as RelativeLayout).removeView(actionTextView)
        }
        removeView(indicatorWrapper)
        indicatorWrapper.setPadding(bannerItemMargin * 2, 6.toPx(), bannerItemMargin * 2, 6.toPx())

        indicatorWrapper.addView(indicator)

        actionText?.let {
            actionTextView.setBodyText(2, false)
            actionTextView.text = actionText
            actionTextView.setTextColor(ContextCompat.getColor(context, R.color.Unify_G500))
        }

        if (indicatorPosition.contains("T") && !indicatorPosition.contains("H")) {
            addView(indicatorWrapper, 0)

            if (actionTextPosition.contains("T")) {
                if (actionTextView.parent == null) {
                    indicatorWrapper.addView(actionTextView)
                }
            } else if (actionTextPosition.contains("B")) {
                if (actionTextView.parent != null) {
                    (actionTextView.parent as RelativeLayout).removeView(actionTextView)
                }

                val actionTextWrapper = RelativeLayout(context)
                actionTextWrapper.apply {
                    setPadding(bannerItemMargin * 2, 0, bannerItemMargin * 2, 0)
                }
                addView(actionTextWrapper)

                actionTextWrapper.addView(actionTextView)
            }

        } else if (indicatorPosition.contains("B") && !indicatorPosition.contains("H")) {
            addView(indicatorWrapper)

            if (actionTextPosition.contains("B")) {
                if (actionTextView.parent == null) {
                    indicatorWrapper.addView(actionTextView)
                }
            } else if (actionTextPosition.contains("T")) {
                if (actionTextView.parent != null) {
                    (actionTextView.parent as RelativeLayout).removeView(actionTextView)
                }

                val actionTextWrapper = RelativeLayout(context)
                actionTextWrapper.apply {
                    setPadding(bannerItemMargin * 2, 0, bannerItemMargin * 2, 0)
                }
                addView(actionTextWrapper, 0)

                actionTextWrapper.addView(actionTextView)
            }
        }
        when {
            indicatorPosition.contains("L") -> {
                indicator.layoutParams =
                    (indicator.layoutParams as RelativeLayout.LayoutParams).apply {
                        addRule(RelativeLayout.ALIGN_PARENT_LEFT)
                    }
            }
            indicatorPosition.contains("C") -> {
                indicator.layoutParams =
                    (indicator.layoutParams as RelativeLayout.LayoutParams).apply {
                        addRule(RelativeLayout.CENTER_IN_PARENT)
                    }
            }
            indicatorPosition.contains("R") -> {
                indicator.layoutParams =
                    (indicator.layoutParams as RelativeLayout.LayoutParams).apply {
                        addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
                    }
            }
        }

        when {
            actionTextPosition.contains("L") -> {
                actionTextView.layoutParams =
                    (actionTextView.layoutParams as RelativeLayout.LayoutParams).apply {
                        addRule(RelativeLayout.ALIGN_PARENT_LEFT)
                    }
            }
            actionTextPosition.contains("C") -> {
                actionTextView.layoutParams =
                    (actionTextView.layoutParams as RelativeLayout.LayoutParams).apply {
                        addRule(RelativeLayout.CENTER_IN_PARENT)
                    }
            }
            actionTextPosition.contains("R") -> {
                actionTextView.layoutParams =
                    (actionTextView.layoutParams as RelativeLayout.LayoutParams).apply {
                        addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
                    }
            }
        }
        indicator.setCurrentIndicator(viewPager.currentItem)
    }


    override fun onDetachedFromWindow() {
        timer.dispose()
        super.onDetachedFromWindow()
    }

    companion object {
        const val INDICATOR_TL = "TL"
        const val INDICATOR_TC = "TC"
        const val INDICATOR_TR = "TR"
        const val INDICATOR_BL = "BL"
        const val INDICATOR_BC = "BC"
        const val INDICATOR_BR = "BR"
        const val INDICATOR_HIDDEN = "H"
    }
}