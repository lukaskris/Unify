package id.co.app.components.datetimepicker.picker

import android.content.Context
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.MotionEvent
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import id.co.app.components.R
import id.co.app.components.utils.fontTypeLoader
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.round


class PickerUnify : RecyclerView {
    var infiniteMode: Boolean = true
    var onValueChanged: ((value: String, index: Int) -> Unit)? = null
    var onItemClickListener: ((value: String, index: Int) -> Unit)? = null
    var textAlign = PickerAdapter.ALIGN_CENTER
    var isUsingPlaceholder: Boolean = false
    var disabledItems: MutableList<Int> = mutableListOf()
        set(value) {
            field = value

            if (this.adapter != null) {
                (this.adapter as PickerAdapter).disabledItems = value

                this.adapter?.notifyDataSetChanged()

                goToPosition(
                    mActiveIndex, false, 0, fromItemClick = false, fromNewData = false,
                    fromBounced = true
                )
            }
        }
    var selectableRangeItems: IntRange? = null
        set(value) {
            field = value

            val tempList: MutableList<Int> = mutableListOf()

            if (value != null) {
                for (i in 0 until value.first) {
                    tempList.add(i)
                }

                var x = itemSize
                if (!infiniteMode && !isUsingPlaceholder) {
                    x -= ceil(itemToShow.toFloat() / 2).toInt() * 2
                }

                for (i in value.last + 1 until x) {
                    tempList.add(i)
                }
            }

            disabledItems = tempList
        }
    var itemToShow: Int = 3
        set(value) {
            field = if (value % 2 == 0) {
                value - 1
            } else {
                value
            }
        }

    var pickerDecorator: PickerDecorator

    private var itemSize: Int = 0
    private var itemHeight: Int = 0
    var stringData: MutableList<String> = mutableListOf()
        set(value) {
            if (field != value && value.size != 0) {
                field = value

                initData(value)
            }
        }

    var newStringData: MutableList<String> = mutableListOf()
        set(value) {
            if (field != value && value.size != 0) {
                field = value

                updateData(value)
            }
        }

    private var bodyFontLevel = 2

    var activeValue: String = ""
        set(value) {
            val prev = field
            if (prev != value && value != "null" && value != "") {
                field = value
                onValueChanged?.invoke(value, activeIndex)
            }
        }

    var activeIndex: Int = 0
        get() {
            if (infiniteMode) return centerActivePosition % itemSize

            return centerActivePosition - ceil(itemToShow.toFloat() / 2).toInt()
        }

    private var centerActivePosition = 0
        set(value) {
            field = value

            activeValue = (layoutManager?.findViewByPosition(value) as TextView?)?.text.toString()
        }

    // actual index of recyclerview position
    private var mActiveIndex: Int = 0

    private var mActivePosition = 0
        set(value) {
            field = value
            centerActivePosition = mActivePosition + floor(itemToShow.toFloat() / 2).toInt()
            mActiveIndex =
                if (infiniteMode) centerActivePosition % itemSize else centerActivePosition - ceil(
                    itemToShow.toFloat() / 2
                ).toInt()
        }
    private var dy = 0
    private var isTouched = false
    private var isBounced = false
    private var isScrollingBounced = false
    private var scrollSpeed = 150f

    private var rvScrollState: Int = 0

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        this.layoutManager = LinearLayoutManager(context)
        val defaultColor = ContextCompat.getColor(context, R.color.Unify_N700_96)
        this.pickerDecorator = PickerDecorator(Pair(14f, 14f), Pair(defaultColor, defaultColor), Pair(true, false), ContextCompat.getColor(context, R.color.Unify_N700_20))

        this.addOnScrollListener(object : OnScrollListener() {
            var mLayoutManager: LinearLayoutManager = layoutManager as LinearLayoutManager

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                rvScrollState = newState

                if (newState == SCROLL_STATE_DRAGGING) isTouched = true

                if (newState == SCROLL_STATE_IDLE && isTouched) {
                    val posBefore = mActivePosition
                    if (mLayoutManager.findFirstVisibleItemPosition() == 0) {
                        mActivePosition = 1
                    }

                    if (infiniteMode) {
                        mActivePosition += round(dy.toDouble() / itemHeight).toInt()
                    } else {
                        if (mLayoutManager.findFirstVisibleItemPosition() == 0) {
                            mActivePosition = 1
                        } else if (mLayoutManager.findFirstVisibleItemPosition() >= itemSize - ceil(
                                itemToShow.toFloat() / 2
                            ).toInt() * 2
                        ) {
                            mActivePosition =
                                itemSize - ceil(itemToShow.toFloat() / 2).toInt() * 2
                        } else {
                            mActivePosition += round((dy.toDouble() - DY_OFFSET) / itemHeight).toInt()
                        }
                    }

                    disabledChecker(true)

                    val smoothScroller = object : LinearSmoothScroller(context) {
                        override fun getVerticalSnapPreference(): Int {
                            return SNAP_TO_START
                        }

                        override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics?): Float {
                            if (displayMetrics != null) {
                                return scrollSpeed / displayMetrics.densityDpi
                            }
                            return super.calculateSpeedPerPixel(displayMetrics)
                        }

                        override fun calculateDtToFit(
                            viewStart: Int,
                            viewEnd: Int,
                            boxStart: Int,
                            boxEnd: Int,
                            snapPreference: Int
                        ): Int {
                            return super.calculateDtToFit(
                                viewStart - itemHeight / 2,
                                viewEnd,
                                boxStart,
                                boxEnd,
                                snapPreference
                            )
                        }
                    }

                    smoothScroller.targetPosition = mActivePosition

                    mLayoutManager.startSmoothScroll(smoothScroller)
                    scrollSpeed = 150f

                    isTouched = false
                }

                if (newState == SCROLL_STATE_IDLE) {
                    dy = 0
                    isLayoutFrozen = false

                    for (i in mLayoutManager.findFirstVisibleItemPosition()..mLayoutManager.findLastVisibleItemPosition()) {
                        configFont(mLayoutManager.findViewByPosition(i) as TextView?, false, i)
                    }

                    configFont(mLayoutManager.findViewByPosition(
                        centerActivePosition
                    ) as TextView?, true, centerActivePosition)

                    if (!isBounced) {
                        activeValue = (mLayoutManager.findViewByPosition(
                            centerActivePosition
                        ) as TextView?)?.text.toString()

                        isScrollingBounced = false
                    }

                    isBounced = false
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                this@PickerUnify.dy += dy

                if (mActivePosition != 0 || !infiniteMode) {
                    if (isTouched) {
                        val ceilIndex =
                            centerActivePosition + ceil(this@PickerUnify.dy.toFloat() / itemHeight).toInt()
                        val floorIndex =
                            centerActivePosition + floor(this@PickerUnify.dy.toFloat() / itemHeight).toInt()
                        val prevTv = if (dy > 0) (mLayoutManager.findViewByPosition(
                            ceilIndex - 1
                        ) as TextView?) else (mLayoutManager.findViewByPosition(
                            floorIndex + 1
                        ) as TextView?)

                        val nextTv = if (dy > 0) (mLayoutManager.findViewByPosition(
                            ceilIndex
                        ) as TextView?) else (mLayoutManager.findViewByPosition(
                            floorIndex
                        ) as TextView?)

                        val prevIndex = if (dy > 0) ceilIndex - 1 else floorIndex + 1
                        val nextIndex = if (dy > 0) ceilIndex else floorIndex

                        if (itemHeight != 0) {
                            if (abs(this@PickerUnify.dy % itemHeight) > itemHeight / 2) {
                                configFont(prevTv, false, prevIndex)
                                configFont(nextTv, true, nextIndex)

                                var addIndex = 0
                                if (!infiniteMode && !isUsingPlaceholder) addIndex =
                                    ceil(itemToShow.toDouble() / 2).toInt()

                                var i = floorIndex - addIndex
                                if (dy >= 0) {
                                    i = ceilIndex - addIndex
                                }

                                if (disabledItems.size != 0) {
                                    if (disabledItems.indexOf(i % itemSize) != -1) {
                                        configFont(nextTv, false, i)
                                    }
                                }
                            }
                        }

                        configFont(mLayoutManager.findViewByPosition(mLayoutManager.findFirstVisibleItemPosition()) as TextView, false, mLayoutManager.findFirstVisibleItemPosition())
                        configFont(mLayoutManager.findViewByPosition(mLayoutManager.findLastVisibleItemPosition()) as TextView, false, mLayoutManager.findLastVisibleItemPosition())
                    }
                }
            }
        })

        addOnItemTouchListener(object : OnItemTouchListener {
            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {

            }

            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                val action = e.action
                if (action == MotionEvent.ACTION_MOVE || action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_SCROLL) {
                    rv.parent.requestDisallowInterceptTouchEvent(true)
                }

                return false
            }

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {

            }

        })
    }

    fun goToPosition(
        index: Int,
        isSmooth: Boolean = false,
        rawIndex: Int = 0,
        fromItemClick: Boolean = false,
        fromNewData: Boolean = false,
        fromBounced: Boolean = false
    ) {
        val posBefore = mActivePosition
        var mLayoutManager = (layoutManager as LinearLayoutManager)
        var k = index
        if (index > itemSize - 1) {
            k = itemSize - 1
            if (newStringData.size > 0 && !infiniteMode) {
                k -= ceil(
                    itemToShow.toFloat() / 2
                ).toInt() * 2
            }
        }

        if (infiniteMode) {
            mActivePosition =
                (((Int.MAX_VALUE / 2) - (Int.MAX_VALUE / 2) % itemSize) - floor(
                    itemToShow.toDouble() / 2
                ).toInt()) + k

            if (isSmooth) {
                mActivePosition = rawIndex - ceil(
                    itemToShow.toFloat() / 2
                ).toInt() + 1
            }
        } else {
            mActivePosition = k + 1
            if (isSmooth) {
                mActivePosition = rawIndex - ceil(
                    itemToShow.toFloat() / 2
                ).toInt() + 1
            }
        }

        disabledChecker()

        for (i in mLayoutManager.findFirstVisibleItemPosition()..mLayoutManager.findLastVisibleItemPosition()) {
            configFont(mLayoutManager.findViewByPosition(i) as TextView?, false, i)
        }

        (this.adapter as PickerAdapter).centerActiveIndex = centerActivePosition
        (this.adapter as PickerAdapter).notifyDataSetChanged()

        if (isSmooth) {
            val smoothScroller = object : LinearSmoothScroller(context) {
                override fun getVerticalSnapPreference(): Int {
                    return SNAP_TO_START
                }

                override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics?): Float {
                    if (displayMetrics != null) {
                        return scrollSpeed / displayMetrics.densityDpi
                    }
                    return super.calculateSpeedPerPixel(displayMetrics)
                }

                override fun calculateDtToFit(
                    viewStart: Int,
                    viewEnd: Int,
                    boxStart: Int,
                    boxEnd: Int,
                    snapPreference: Int
                ): Int {
                    return super.calculateDtToFit(
                        viewStart - itemHeight / 2,
                        viewEnd,
                        boxStart,
                        boxEnd,
                        snapPreference
                    )
                }
            }
            smoothScroller.targetPosition = mActivePosition
            mLayoutManager.startSmoothScroll(smoothScroller)
            scrollSpeed = 150f
        } else {
            mLayoutManager.scrollToPositionWithOffset(mActivePosition, itemHeight / 2)
        }

        this.post {
            activeValue = (mLayoutManager.findViewByPosition(centerActivePosition) as TextView?)?.text.toString()
            if (fromItemClick) {
                this@PickerUnify.onItemClickListener?.invoke(activeValue, activeIndex)
            }
        }
    }

    private fun initData(value: MutableList<String>) {
        if (!infiniteMode && !isUsingPlaceholder) {
            for (i in 0.until(ceil(itemToShow.toFloat() / 2).toInt())) {
                stringData.add(0, "")
                stringData.add("")
            }
        }

        itemSize = stringData.size

        this.adapter =
            PickerAdapter(
                stringData,
                itemToShow,
                bodyFontLevel,
                infiniteMode,
                textAlign,
                isUsingPlaceholder,
                selectableRangeItems,
                disabledItems,
                pickerDecorator
            ).apply {
                onItemClickListener = { position ->
                    if (position == centerActivePosition && rvScrollState == 0) {
                        this@PickerUnify.onItemClickListener?.invoke(activeValue, activeIndex)
                    }
                    if (((layoutManager as LinearLayoutManager).findViewByPosition(position) as TextView).text.isNotEmpty()
                        && position != centerActivePosition
                        && rvScrollState == 0
                    ) {
                        isLayoutFrozen = true
                        goToPosition(position % itemSize, true, position, true)
                    }
                }
            }

        this.post {
            if (infiniteMode) {
                if (mActiveIndex >= itemSize) {
                    mActiveIndex = itemSize - 1
                }
            } else {
                if (mActiveIndex >= itemSize - ceil(
                        itemToShow.toFloat() / 2
                    ).toInt() * 2
                ) {
                    mActiveIndex = itemSize - ceil(
                        itemToShow.toFloat() / 2
                    ).toInt() * 2 - 1
                }
            }

            goToPosition(mActiveIndex)
        }

        if (infiniteMode) {
            // add default value to activeValue
            activeValue = value[centerActivePosition % itemSize]
        }
    }

    private fun updateData(value: MutableList<String>) {
        isLayoutFrozen = true

        if (!infiniteMode && !isUsingPlaceholder) {
            for (i in 0.until(ceil(itemToShow.toFloat() / 2).toInt())) {
                newStringData.add(0, "")
                newStringData.add("")
            }
        }

        itemSize = newStringData.size

        val adapter =
            PickerAdapter(
                newStringData,
                itemToShow,
                bodyFontLevel,
                infiniteMode,
                textAlign,
                isUsingPlaceholder,
                selectableRangeItems,
                disabledItems,
                pickerDecorator
            ).apply {
                onItemClickListener = { position ->
                    if (position == centerActivePosition && rvScrollState == 0) {
                        this@PickerUnify.onItemClickListener?.invoke(activeValue, activeIndex)
                    }
                    if (((layoutManager as LinearLayoutManager).findViewByPosition(position) as TextView).text.isNotEmpty()
                        && position != centerActivePosition
                        && rvScrollState == 0
                    ) {
                        isLayoutFrozen = true
                        goToPosition(position % itemSize, true, position, true)
                    }
                }
            }

        this.swapAdapter(
            adapter,
            true
        )

        if (infiniteMode) {
            if (mActiveIndex >= itemSize) {
                mActiveIndex = itemSize - 1
            }
        } else {
            if (mActiveIndex >= itemSize - ceil(
                    itemToShow.toFloat() / 2
                ).toInt() * 2
            ) {
                mActiveIndex = itemSize - ceil(
                    itemToShow.toFloat() / 2
                ).toInt() * 2 - 1
            }
        }

        goToPosition(mActiveIndex, false, 0, fromItemClick = false, fromNewData = true)

        if (infiniteMode) {
            activeValue = value[centerActivePosition % itemSize]
        }
    }

    private fun disabledChecker(isFromScrolling: Boolean = false) {
        val beforePos = mActivePosition
        if (disabledItems.size != 0) {
            var addIndex = 0
            if (!infiniteMode && !isUsingPlaceholder) addIndex = ceil(itemToShow.toDouble() / 2).toInt()

            if (dy >= 0) {
                while (disabledItems.size != 0 && disabledItems.indexOf((centerActivePosition % itemSize) - addIndex) != -1) {
                    mActivePosition += 1
                }
            } else {
                while (disabledItems.size != 0 && disabledItems.indexOf((centerActivePosition % itemSize) - addIndex) != -1) {
                    mActivePosition -= 1
                }
            }

            if (!infiniteMode) {
                var index = (centerActivePosition % itemSize)
                if (mActivePosition > itemSize - ceil(itemToShow.toDouble() / 2).toInt() * 2) {
                    index--
                    mActivePosition -= 1
                    while (disabledItems.size != 0 && disabledItems.indexOf(index - addIndex) != -1) {
                        mActivePosition -= 1
                        index--
                    }
                }

                if (mActivePosition == 0) {
                    index++
                    mActivePosition += 1
                    while (disabledItems.size != 0 && disabledItems.indexOf(index - addIndex) != -1) {
                        mActivePosition += 1
                        index++
                    }
                }
            }
        }

        if (isFromScrolling) {
            isBounced = true
            isScrollingBounced = true
        }

        if ((abs(mActivePosition - beforePos)) > 1) {
            this.scrollSpeed = 100f
        }
    }

    private fun configFont(tv: TextView?, isSelected: Boolean, index: Int) {
        if (isSelected) {
            tv?.setTextSize(TypedValue.COMPLEX_UNIT_SP, pickerDecorator.fontSize.first)
            if (pickerDecorator.fontBold.first) {
                tv?.typeface = fontTypeLoader(context, true)
            } else {
                tv?.typeface = fontTypeLoader(context, false)
            }
            tv?.setTextColor(pickerDecorator.fontColor.first)
        } else  {
            tv?.setTextSize(TypedValue.COMPLEX_UNIT_SP, pickerDecorator.fontSize.second)
            if (pickerDecorator.fontBold.second) {
                tv?.typeface = fontTypeLoader(context, true)
            } else {
                tv?.typeface = fontTypeLoader(context, false)
            }

            if (disabledItems.indexOf(index % itemSize) > -1 ||
                (isUsingPlaceholder && index % itemSize >= itemSize - (itemToShow + 1) / 2) ||
                (isUsingPlaceholder && index % itemSize < (itemToShow + 1) / 2)) {
                tv?.setTextColor(pickerDecorator.disabledColor)
            } else {
                tv?.setTextColor(pickerDecorator.fontColor.second)
            }
        }
    }

    override fun fling(velocityX: Int, velocityY: Int): Boolean {
        var mVelocityY = velocityY

        mVelocityY /= 3
        return super.fling(velocityX, mVelocityY)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (isScrollingBounced && rvScrollState == SCROLL_STATE_SETTLING) {
            return true
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        this.itemHeight = this.measuredHeight / (itemToShow + 1)
    }

    companion object {
        private const val DY_OFFSET = 10
    }
}