//package id.co.app.components.carousell
//
//import android.animation.ObjectAnimator
//import android.content.Context
//import android.os.Handler
//import android.os.Looper
//import android.util.AttributeSet
//import android.view.MotionEvent
//import android.view.VelocityTracker
//import android.view.View
//import android.view.ViewGroup
//import android.widget.*
//import androidx.core.content.ContextCompat
//import id.co.app.components.R
//import id.co.app.components.image.ImageUnify
//import id.co.app.components.pagecontrol.PageControl
//import id.co.app.components.typography.UnifyMotion
//import id.co.app.components.utils.setBodyText
//import id.co.app.components.utils.setImage
//import id.co.app.components.utils.toDp
//import id.co.app.components.utils.toPx
//import java.util.*
//import kotlin.math.abs
//import kotlin.math.ceil
//import kotlin.math.round
//import kotlin.math.roundToInt
//
//class CarouselUnify(context: Context, attributeSet: AttributeSet) : LinearLayout(context, attributeSet) {
//
//    var slideToShow: Float = 1f
//    var slideToScroll: Int = 1
//    var centerMode: Boolean = false
//    var activeIndex: Int = 0
//        set (value) {
//            if (field != value) {
//                var prev = field
//                var next = value
//                if (value == -1) {
//                    prev = 0
//                    next = ceil(indicatorCount).toInt() - 1
//                }
//                if (value == ceil(indicatorCount).toInt()) {
//                    prev = ceil(indicatorCount).toInt() - 1
//                    next = 0
//                }
//                if (value == -1 || value == ceil(indicatorCount).toInt()) {
//                    onActiveIndexChangedListener?.onActiveIndexChanged(prev, next)
//                } else if (field != ceil(indicatorCount).toInt() && field != -1) {
//                    onActiveIndexChangedListener?.onActiveIndexChanged(field, value)
//                }
//            }
//            field = value
//
//            if(!isSwiped) {
//                requestLayout()
//            } else {
//                if (!freeMode) calculateScrollX()
//
//                calculateIndicator()
//            }
//        }
//
//    var indicatorPosition: String = INDICATOR_BC
//        set(value) {
//            field = value
//            initIndicator()
//        }
//
//    var actionTextPosition: String = INDICATOR_BC
//        set(value) {
//            field = value
//            initIndicator()
//        }
//
//    var timer = Timer()
//    var autoplayDuration: Long = 3000
//    var autoplay: Boolean = false
//        set(value) {
//            field = value
//            if (value) {
//                timer.scheduleAtFixedRate(object : TimerTask() {
//                    override fun run() {
//                        Handler(Looper.getMainLooper()).post {
//                            isSwiped = true
//                            nextSlide()
//                        }
//                    }
//                }, autoplayDuration, autoplayDuration)
//            } else {
//                timer.cancel()
//            }
//        }
//
//    var freeMode: Boolean = false
//    var infinite: Boolean = false
//
//    val stageParam = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
//    val stageWrapper = ObservableHorizontalScrollView(context)
//    val stage = LinearLayout(context)
//    val indicatorWrapper = RelativeLayout(context)
//    val indicator = PageControl(context)
//    var indicatorCount = 1f
//    var onActiveIndexChangedListener: OnActiveIndexChangedListener? = null
//    var onDragEventListener: OnDragEventListener? = null
//    var shouldSmoothScroll: Boolean = true
//    var bannerItemMargin: Int = 8.toPx()
//    val actionTextView: TextView = TextView(context)
//    var onItemClick: ((index: Int) -> Unit)? = null
//    private var singleItemWidth: Int = 0
//    private var isSwiped = false
//
//    var actionText: CharSequence? = null
//        set(value) {
//            field = value
//
//            initIndicator()
//        }
//
//    private var isBanner = false
//
//    init {
//        orientation = VERTICAL
//
//        stageWrapper.layoutParams = stageParam
//        stageWrapper.overScrollMode = View.OVER_SCROLL_NEVER
//        stage.orientation = HORIZONTAL
//        stageWrapper.addView(stage)
//        addView(stageWrapper)
//        addView(indicatorWrapper)
//        indicatorWrapper.addView(indicator)
//        indicatorWrapper.addView(actionTextView)
//        initIndicator()
//
//        stageWrapper.onScrollListener = object : ObservableHorizontalScrollView.OnScrollListener {
//            override fun onEndScroll(scrollView: ObservableHorizontalScrollView, velocity: Int) {
//                if (abs(velocity.toDp()) < 1000) {
//                    val itemWidth: Float = (stage.measuredWidth / stage.childCount).toFloat()
//                    if (slideToScroll == 1) {
//                        activeIndex = if (centerMode && !infinite) {
//                            round(scrollView.scrollX.toFloat() / singleItemWidth).toInt()
//                        } else {
//                            if ((infinite && !centerMode)) {
//                                (scrollView.scrollX / itemWidth).roundToInt() - ceil(slideToShow).toInt()
//                            } else if (infinite && centerMode) {
//                                val centerModeItemWidth: Int = (stageWrapper.measuredWidth / slideToShow).toInt()
//
//                                round(scrollView.scrollX.toFloat() / centerModeItemWidth.toFloat()).toInt() - ceil(slideToShow).toInt()
//                            } else {
//                                (scrollView.scrollX / itemWidth).roundToInt()
//                            }
//                        }
//                    } else {
//                        activeIndex =
//                            (scrollView.scrollX / (itemWidth * slideToScroll)).roundToInt()
//                    }
//                } else {
//                    if (velocity <= 0) {
//                        nextSlide()
//                    } else {
//                        prevSlide()
//                    }
//                }
//
//                isSwiped = true
//            }
//
//            override fun onScrollChanged(
//                scrollView: ObservableHorizontalScrollView,
//                x: Int,
//                y: Int,
//                oldX: Int,
//                oldY: Int
//            ) {
////                if (!isSwiped && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) stageWrapper.scrollX = singleItemWidth * ceil(slideToShow).toInt()
//
//                val itemWidth: Float = (stage.measuredWidth / stage.childCount).toFloat()
//
//                if (freeMode) {
//                    activeIndex = (scrollView.scrollX / singleItemWidth.toFloat()).roundToInt()
//                }
//
//                if (scrollView.scrollX == (singleItemWidth * (ceil(slideToShow).toInt() - 1)) && infinite && !centerMode) {
//                    shouldSmoothScroll = false
//                    activeIndex = ceil(indicatorCount).toInt() - 1
//                }
//
//                var additionalScrollForCenterMode = 0
//
//                if (centerMode && infinite) {
//                    additionalScrollForCenterMode = ((stageWrapper.measuredWidth - itemWidth)).toInt()
//
//                    val centerModeItemWidth: Int = (stageWrapper.measuredWidth / slideToShow).toInt()
//
//                    if (scrollView.scrollX <= (ceil(slideToShow).toInt() - 1) * centerModeItemWidth) {
//                        shouldSmoothScroll = false
//                        activeIndex = ceil(indicatorCount).toInt() - 1
//                    }
//                }
//
//                if (stageWrapper.scrollX >= (indicatorCount.toInt() * itemWidth) + (ceil(slideToShow).toInt() * itemWidth) - additionalScrollForCenterMode && infinite) {
//                    shouldSmoothScroll = false
//                    activeIndex = 0
//                }
//            }
//        }
//    }
//
//    fun addItem(view: View) {
//        val itemWrapper = LinearLayout(context)
//        itemWrapper.addView(view)
//        stage.addView(itemWrapper)
//        indicatorCount = (((stage.childCount - slideToShow) / slideToScroll) + 1)
//        indicator.setIndicator(ceil(indicatorCount).toInt())
//    }
//
//    fun addItems(layoutRef: Int, data: ArrayList<Any>, listener: (view: View, data: Any) -> Unit) {
//        if (data.size == 1) {
//            infinite = false
//        }
//
//        stage.removeAllViews()
//
//        data.forEachIndexed { index, itemData ->
//            val view = View.inflate(context, layoutRef, null)
//            listener(view, itemData)
//
//            val itemWrapper = LinearLayout(context)
//            val lp = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
//            view.layoutParams = lp
//            itemWrapper.addView(view)
//            stage.addView(itemWrapper)
//
//            if (index == data.size - 1 && infinite) {
//
//                for (x in 0 until ceil(slideToShow).toInt()) {
//                    val cloneView = View.inflate(context, layoutRef, null)
//                    if (data.size - 1 - x < 0)  {
//                        listener(cloneView, data[0])
//                    } else {
//                        listener(cloneView, data[data.size - 1 - x])
//                    }
//                    val cloneItemWrapper = LinearLayout(context)
//
//                    cloneView.layoutParams = lp
//
//                    cloneItemWrapper.addView(cloneView)
//                    stage.addView(cloneItemWrapper, 0)
//                }
//
//                for (x in 0 until ceil(slideToShow).toInt()) {
//                    val cloneView2 = View.inflate(context, layoutRef, null)
//                    if (x > data.size - 1) {
//                        listener(cloneView2, data[data.size - 1])
//                    } else {
//                        listener(cloneView2, data[x])
//                    }
//                    cloneView2.layoutParams = lp
//                    val cloneItemWrapper2 = LinearLayout(context)
//                    cloneItemWrapper2.addView(cloneView2)
//                    stage.addView(cloneItemWrapper2)
//                }
//            }
//        }
//
//        if (slideToScroll != 1) {
//            if ((((data.size - slideToShow) / slideToScroll) + 1).toInt() > 1) {
//                indicatorCount = (((data.size - slideToShow) / slideToScroll) + 1)
//            }
//        } else {
//            indicatorCount = data.size.toFloat()
//        }
//        indicator.setIndicator(ceil(indicatorCount).toInt())
//    }
//
//    fun addBannerImages(data: ArrayList<String>) {
//        isBanner = true
//        stageWrapper.post {
//            slideToShow = (bannerItemMargin * 4).toFloat() / stageWrapper.measuredWidth.toFloat() + 1f
//            measureItems()
//        }
//        data.forEachIndexed { index, itemData ->
//            val view = ImageUnify(context)
//            view.setOnClickListener {
//                onItemClick?.invoke(index)
//            }
//
//            view.cornerRadius = 16
//            view.setImageUrl(itemData)
//            val itemWrapper = LinearLayout(context)
//            val lp = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
//            var leftMargin = bannerItemMargin / 2
//            var rightMargin = bannerItemMargin / 2
//            if (index == 0) leftMargin = 0
//            if (index == data.size - 1) rightMargin = 0
//            lp.setMargins(leftMargin, 0, rightMargin, 0)
//            view.layoutParams = lp
//            itemWrapper.addView(view)
//            stage.addView(itemWrapper)
//        }
//
//        if (slideToScroll != 1) {
//            if ((((data.size - slideToShow) / slideToScroll) + 1).toInt() > 1) {
//                indicatorCount = (((data.size - slideToShow) / slideToScroll) + 1)
//            }
//        } else {
//            indicatorCount = data.size.toFloat()
//        }
//        indicator.setIndicator(ceil(indicatorCount).toInt())
//    }
//
//    fun addImages(data: ArrayList<String>) {
//        data.forEachIndexed { index, itemData ->
//            val view = ImageView(context)
//            view.setOnClickListener {
//                onItemClick?.invoke(index)
//            }
//
//            view.setImage(itemData, 16f)
//            val itemWrapper = LinearLayout(context)
//            val lp = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
//            view.layoutParams = lp
//            itemWrapper.addView(view)
//            stage.addView(itemWrapper)
//        }
//
//        if (slideToScroll != 1) {
//            if ((((data.size - slideToShow) / slideToScroll) + 1).toInt() > 1) {
//                indicatorCount = (((data.size - slideToShow) / slideToScroll) + 1)
//            }
//        } else {
//            indicatorCount = data.size.toFloat()
//        }
//        indicator.setIndicator(ceil(indicatorCount).toInt())
//    }
//
//    fun refreshChild() {
//        for(x in 0 until stage.childCount) {
//            ((stage.getChildAt(x) as LinearLayout).getChildAt(0) as ImageView).requestLayout()
//        }
//    }
//
//    fun removeItem(idx: Int) {
//        stage.removeViewAt(idx)
//        if ((((stage.childCount - slideToShow) / slideToScroll) + 1).toInt() > 1) {
//            indicatorCount = (((stage.childCount - slideToShow) / slideToScroll) + 1)
//        }
//        indicator.setIndicator(ceil(indicatorCount).toInt())
//        forceLayout()
//    }
//
//    fun nextSlide() {
//        if (slideToScroll != 1) {
//            if (activeIndex < ceil(indicatorCount).toInt() - 1) {
//                activeIndex += 1
//            }
//        } else {
//            if (activeIndex < indicator.indicatorCount - 1 || infinite) {
//                activeIndex += 1
//            }
//        }
//    }
//
//    fun prevSlide() {
//        if (activeIndex != 0 || infinite) {
//            activeIndex -= 1
//        }
//    }
//
//    private fun calculateScrollX() {
//        if (activeIndex < 0 && infinite) {
//            val scrollTo = (ceil(slideToShow).toInt() + activeIndex) * (stageWrapper.measuredWidth / slideToShow).toInt()
//            smoothScrollTo(scrollTo)
//        } else {
//            val stageWidth = stageWrapper.measuredWidth
//            val itemWidth: Int = (stageWidth / slideToShow).toInt()
//
//            if (slideToScroll == 1) {
//                if (infinite) {
//                    if (stageWrapper.scrollX == itemWidth * ceil(slideToShow).toInt() - 1) {
//                        stageWrapper.scrollTo((activeIndex * stageWidth / slideToShow).toInt() + itemWidth * ceil(slideToShow).toInt(), 0)
//                    }
//                    else {
//                        if (shouldSmoothScroll) {
//                            smoothScrollTo((activeIndex * stageWidth / slideToShow).toInt() + itemWidth * ceil(slideToShow).toInt())
//                        } else {
//                            stageWrapper.scrollTo(
//                                (activeIndex * stageWidth / slideToShow).toInt() + itemWidth * ceil(slideToShow).toInt(),
//                                0
//                            )
//                            shouldSmoothScroll = true
//                        }
//                    }
//                }
//                else {
//                    smoothScrollTo((activeIndex * stageWidth / slideToShow).toInt())
//                }
//            }
//            else {
//                smoothScrollTo(((activeIndex * stageWidth / slideToShow).toInt() * slideToScroll))
//            }
//        }
//    }
//
//    private fun measureItems() {
//        val stageWidth = stageWrapper.measuredWidth
//        val itemWidth: Int = (stageWidth / slideToShow).toInt()
//        for(x in 0 until stage.childCount) {
//            val item = stage.getChildAt(x) as LinearLayout
//            val param = LayoutParams(itemWidth, LayoutParams.WRAP_CONTENT)
//            if (centerMode) {
//                if (x == 0) param.setMargins((stageWidth - itemWidth) / 2, 0, 0, 0)
//                if (x == stage.childCount - 1 && stage.childCount != 1) param.setMargins(0, 0, (stageWidth - itemWidth) / 2, 0)
//            }
//            item.layoutParams = param
//        }
//    }
//
//    private fun initIndicator() {
//
//        (indicator.parent as RelativeLayout).removeView(indicator)
//        if (actionTextView.parent != null) {
//            (actionTextView.parent as RelativeLayout).removeView(actionTextView)
//        }
//        removeView(indicatorWrapper)
//        indicatorWrapper.setPadding(bannerItemMargin * 2, 6.toPx(), bannerItemMargin * 2, 6.toPx())
//
//        indicatorWrapper.addView(indicator)
//
//        actionText?.let {
//            actionTextView.setBodyText(2, false)
//            actionTextView.text = actionText
//            actionTextView.setTextColor(ContextCompat.getColor(context, R.color.Unify_G500))
//        }
//
//        if (indicatorPosition.contains("T") && !indicatorPosition.contains("H")) {
//            addView(indicatorWrapper, 0)
//
//            if (actionTextPosition.contains("T")) {
//                if (actionTextView.parent == null) {
//                    indicatorWrapper.addView(actionTextView)
//                }
//            }
//
//            else if (actionTextPosition.contains("B")) {
//                if (actionTextView.parent != null) {
//                    (actionTextView.parent as RelativeLayout).removeView(actionTextView)
//                }
//
//                val actionTextWrapper = RelativeLayout(context)
//                actionTextWrapper.apply {
//                    setPadding(bannerItemMargin * 2, 0, bannerItemMargin * 2, 0)
//                }
//                addView(actionTextWrapper)
//
//                actionTextWrapper.addView(actionTextView)
//            }
//
//        } else if (indicatorPosition.contains("B") && !indicatorPosition.contains("H")) {
//            addView(indicatorWrapper)
//
//            if (actionTextPosition.contains("B")) {
//                if (actionTextView.parent == null) {
//                    indicatorWrapper.addView(actionTextView)
//                }
//            }
//
//            else if (actionTextPosition.contains("T")) {
//                if (actionTextView.parent != null) {
//                    (actionTextView.parent as RelativeLayout).removeView(actionTextView)
//                }
//
//                val actionTextWrapper = RelativeLayout(context)
//                actionTextWrapper.apply {
//                    setPadding(bannerItemMargin * 2, 0, bannerItemMargin * 2, 0)
//                }
//                addView(actionTextWrapper, 0)
//
//                actionTextWrapper.addView(actionTextView)
//            }
//        }
//        when {
//            indicatorPosition.contains("L") -> {
//                indicator.layoutParams = (indicator.layoutParams as RelativeLayout.LayoutParams).apply {
//                    addRule(RelativeLayout.ALIGN_PARENT_LEFT)
//                }
//            }
//            indicatorPosition.contains("C") -> {
//                indicator.layoutParams = (indicator.layoutParams as RelativeLayout.LayoutParams).apply {
//                    addRule(RelativeLayout.CENTER_IN_PARENT)
//                }
//            }
//            indicatorPosition.contains("R") -> {
//                indicator.layoutParams = (indicator.layoutParams as RelativeLayout.LayoutParams).apply {
//                    addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
//                }
//            }
//        }
//
//        when {
//            actionTextPosition.contains("L") -> {
//                actionTextView.layoutParams = (actionTextView.layoutParams as RelativeLayout.LayoutParams).apply {
//                    addRule(RelativeLayout.ALIGN_PARENT_LEFT)
//                }
//            }
//            actionTextPosition.contains("C") -> {
//                actionTextView.layoutParams = (actionTextView.layoutParams as RelativeLayout.LayoutParams).apply {
//                    addRule(RelativeLayout.CENTER_IN_PARENT)
//                }
//            }
//            actionTextPosition.contains("R") -> {
//                actionTextView.layoutParams = (actionTextView.layoutParams as RelativeLayout.LayoutParams).apply {
//                    addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
//                }
//            }
//        }
//        indicator.setCurrentIndicator(activeIndex)
//    }
//
//    private fun calculateIndicator() {
//        indicator.setCurrentIndicator(activeIndex)
//    }
//
//    private fun smoothScrollTo(x: Int) {
//        val anim = ObjectAnimator.ofInt(stageWrapper, "scrollX", x)
//        anim.duration = UnifyMotion.T3
//        anim.interpolator = UnifyMotion.EASE_IN_OUT
//
//        anim.start()
//    }
//
//    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
//
//        singleItemWidth = (stageWrapper.measuredWidth.toFloat() / slideToShow).toInt()
//
//        if (!isBanner) measureItems()
//        if (!freeMode) {
//            stageWrapper.isLoosy = false
//            calculateScrollX()
//        } else {
//            stageWrapper.isLoosy = true
//        }
//        calculateIndicator()
//
//        stageWrapper.onTouchListener = object : ObservableHorizontalScrollView.OnTouchListener {
//            var oldX: Int = 0
//            override fun onTouchDown() {
//                isSwiped = true
//                if (autoplay) {
//                    timer.cancel()
//                }
//
//                oldX = stageWrapper.scrollX
//            }
//
//            override fun onTouchMove() {
//                onDragEventListener?.onDrag((oldX - stageWrapper.scrollX) / singleItemWidth.toFloat())
//            }
//
//            override fun onTouchUp() {
//                if (autoplay) {
//                    timer = Timer()
//                    timer.scheduleAtFixedRate(object : TimerTask() {
//                        override fun run() {
//                            Handler(Looper.getMainLooper()).post {
//                                nextSlide()
//                            }
//                        }
//                    }, autoplayDuration, autoplayDuration)
//                }
//            }
//        }
//    }
//
//    override fun onAttachedToWindow() {
//        super.onAttachedToWindow()
//
//        if (infinite) {
//            stageWrapper.post {
//                stageWrapper.scrollX = singleItemWidth * ceil(slideToShow).toInt()
//            }
//        }
//    }
//
//    interface OnActiveIndexChangedListener {
//        fun onActiveIndexChanged(prev: Int, current: Int)
//    }
//
//    interface OnDragEventListener {
//        fun onDrag(progress: Float)
//    }
//
//    companion object {
//        const val INDICATOR_TL = "TL"
//        const val INDICATOR_TC = "TC"
//        const val INDICATOR_TR = "TR"
//        const val INDICATOR_BL = "BL"
//        const val INDICATOR_BC = "BC"
//        const val INDICATOR_BR = "BR"
//        const val INDICATOR_HIDDEN = "H"
//    }
//}
//
//class ObservableHorizontalScrollView @JvmOverloads constructor(
//    context: Context,
//    attrs: AttributeSet? = null,
//    defStyle: Int = 0
//) : HorizontalScrollView(context, attrs, defStyle) {
//
//    private var mVelocityTracker: VelocityTracker? = null
//    private var mIsScrolling: Boolean = false
//    private var mIsTouching: Boolean = false
//    private var mScrollingRunnable: Runnable? = null
//    var onScrollListener: OnScrollListener? = null
//    var onTouchListener: OnTouchListener? = null
//    var isLoosy: Boolean = false
//    var isActionDown = true
//
//    interface OnScrollListener {
//        fun onScrollChanged(
//            scrollView: ObservableHorizontalScrollView,
//            x: Int,
//            y: Int,
//            oldX: Int,
//            oldY: Int
//        )
//
//        fun onEndScroll(scrollView: ObservableHorizontalScrollView, velocity: Int = 0)
//    }
//
//    interface OnTouchListener {
//        fun onTouchDown()
//
//        fun onTouchMove()
//
//        fun onTouchUp()
//    }
//
//    override fun onTouchEvent(ev: MotionEvent): Boolean {
//        val action = ev.action
//
//        if (isActionDown) {
//            onTouchListener?.onTouchDown()
//            mVelocityTracker?.clear()
//            mVelocityTracker = mVelocityTracker ?: VelocityTracker.obtain()
//            mVelocityTracker?.addMovement(ev)
//            isActionDown = false
//        }
//
//        when (action) {
//            MotionEvent.ACTION_MOVE -> {
//                onTouchListener?.onTouchMove()
//                mIsTouching = true
//                mIsScrolling = true
//
//                mVelocityTracker?.apply {
//                    val pointerId: Int = ev.getPointerId(ev.actionIndex)
//                    addMovement(ev)
//                    computeCurrentVelocity(1000)
//                }
//            }
//            MotionEvent.ACTION_UP -> {
//                isActionDown = true
//                onTouchListener?.onTouchUp()
//                val velocity: Int = mVelocityTracker?.getXVelocity(ev.getPointerId(ev.actionIndex))!!.toInt()
//                onScrollListener!!.onEndScroll(this, velocity)
//
//                if (!isLoosy) return false
//
//                if (mIsTouching && !mIsScrolling) {
//                    if (onScrollListener != null) {
//                        onScrollListener!!.onEndScroll(this)
//                    }
//                }
//
//                mIsTouching = false
//                mVelocityTracker?.recycle()
//                mVelocityTracker = null
//            }
//            MotionEvent.ACTION_CANCEL -> {
//                isActionDown = true
//                mVelocityTracker?.recycle()
//                mVelocityTracker = null
//            }
//        }
//
//        return super.onTouchEvent(ev)
//    }
//
//    override fun onScrollChanged(x: Int, y: Int, oldX: Int, oldY: Int) {
//        super.onScrollChanged(x, y, oldX, oldY)
//
//        if (abs(oldX - x) > 0) {
//            if (mScrollingRunnable != null) {
//                removeCallbacks(mScrollingRunnable)
//            }
//
//            mScrollingRunnable = Runnable {
//                if (mIsScrolling && !mIsTouching) {
//                    if (onScrollListener != null) {
//                        onScrollListener!!.onEndScroll(this@ObservableHorizontalScrollView)
//                    }
//                }
//
//                mIsScrolling = false
//                mScrollingRunnable = null
//            }
//
//            postDelayed(mScrollingRunnable, 200)
//        }
//
//        if (onScrollListener != null) {
//            onScrollListener!!.onScrollChanged(this, x, y, oldX, oldY)
//        }
//    }
//}