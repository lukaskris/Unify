package id.co.app.components.pagecontrol

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.transition.ChangeBounds
import androidx.transition.Fade
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import id.co.app.components.R
import id.co.app.components.utils.toPx
import java.util.*
import kotlin.math.max

class PageControl : LinearLayout {
    var indicatorType: Int = CIRCLE_INDICATOR_TYPE
        set(value) {
            field = value

            setIndicator(indicatorCount)
        }

    var indicatorColorType: Int = 0
        set(value) {
            field = value

            activeColor = when (value) {
                COLOR_BLUE -> ContextCompat.getColor(context, R.color.pagecontrolunify_select_blue)
                COLOR_YELLOW -> ContextCompat.getColor(context, R.color.pagecontrolunify_select_yellow)
                COLOR_RED -> ContextCompat.getColor(context, R.color.pagecontrolunify_select_red)
                COLOR_INVERTED -> ContextCompat.getColor(context, R.color.pagecontrolunify_selected_inverted_color)
                COLOR_BLACK -> ContextCompat.getColor(context, R.color.pagecontrolunify_selected_black_color)
                else -> ContextCompat.getColor(context, R.color.Unify_BN500)
            }

            inactiveColor = when (value) {
                COLOR_INVERTED -> {
                    val inactiveInvert = ContextCompat.getColor(context, R.color.pagecontrolunify_unselected_inverted_color)
                    val inactiveRed = Color.red(inactiveInvert)
                    val inactiveGreen = Color.green(inactiveInvert)
                    val inactiveBlue = Color.blue(inactiveInvert)

                    Color.argb(112, inactiveRed, inactiveGreen, inactiveBlue)
                }
                COLOR_BLACK -> {
                    val inactiveBlack = ContextCompat.getColor(context, R.color.pagecontrolunify_unselected_black_color)
                    val inactiveRed = Color.red(inactiveBlack)
                    val inactiveGreen = Color.green(inactiveBlack)
                    val inactiveBlue = Color.blue(inactiveBlack)

                    Color.argb(179, inactiveRed, inactiveGreen, inactiveBlue)
                }
                else -> ContextCompat.getColor(context, R.color.pagecontrolunify_unselected_color)
            }
        }

    var activeColor: Int = ContextCompat.getColor(context, R.color.Unify_B500)
        set(value) {
            field = value

            updateColor(getChildAt(indicatorCurrentPosition), STATE_SELECTED)

        }

    var inactiveColor: Int = ContextCompat.getColor(context, R.color.Unify_NN200)
        set(value) {
            field = value

            for(i in 0 until indicatorCount) {
                if(i != indicatorCurrentPosition)
                    updateColor(getChildAt(i), STATE_NORMAL)
            }
        }

    var indicatorCount: Int = 0
    var indicatorCurrentPosition: Int = 0
    private var indicatorDrawable: Drawable

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        val attributes = context.theme.obtainStyledAttributes(attrs, R.styleable.PageControl, defStyleAttr, 0)

        try {
            indicatorColorType = attributes.getInteger(R.styleable.PageControl_indicator_color, 0)
            indicatorType = attributes.getInteger(R.styleable.PageControl_indicator_type, 0)
        } finally {
            attributes.recycle()
        }
    }

    init {
        if(indicatorType == CIRCLE_INDICATOR_TYPE) {
            indicatorDrawable = GradientDrawable().apply {
                shape = GradientDrawable.OVAL
                setColor(inactiveColor)
            }
        } else {
            indicatorDrawable = GradientDrawable().apply {
                shape = GradientDrawable.RECTANGLE
                cornerRadius = 3.toPx().toFloat()
                setColor(inactiveColor)
            }
        }
    }

    fun setIndicator(size: Int) {
        removeAllViews()
        indicatorCount = size
        indicatorCurrentPosition = 0

        if (indicatorCount <= 1) return

        if(indicatorType == CIRCLE_INDICATOR_TYPE) {
            renderCircleIndicator()
        } else {
            renderOvalIndicator()
        }

        setCurrentIndicator(0)
    }


    fun setCurrentIndicator(currentPosition: Int) {
        indicatorCurrentPosition = currentPosition

        if(indicatorType == CIRCLE_INDICATOR_TYPE) {
            if (indicatorCount > MAX_INDICATORS) updateOverflowState(indicatorCurrentPosition)
            else updateSimpleState(indicatorCurrentPosition)
        }
        else {
            updateSimpleState(indicatorCurrentPosition)
        }
    }

    private fun renderOvalIndicator() {
        for (i in 0 until indicatorCount) {
            val view = View(context)
            view.background = indicatorDrawable

            val params = LayoutParams(50, 9)
            params.setMargins(5, 0, 5, 0)
            view.layoutParams = params

            addView(view)
        }
    }

    private fun renderCircleIndicator() {
        for (i in 0 until indicatorCount) {
            val view = View(context)
            view.background = indicatorDrawable

            animateViewScale(view, if (indicatorCount > MAX_INDICATORS) STATE_SMALL else STATE_NORMAL)

            val params = LayoutParams(6.toPx(), 6.toPx())
            params.setMargins(2.toPx(), 0, 2.toPx(), 0)
            view.layoutParams = params

            addView(view)
        }
    }

    private fun updateSimpleState(position: Int) {
        updateColor(getChildAt(position), STATE_SELECTED)

        for(i in 0 until indicatorCount) {
            if(i != indicatorCurrentPosition)
                updateColor(getChildAt(i), STATE_NORMAL)
        }
    }

    private fun updateOverflowState(position: Int) {
        if (indicatorCount == 0) return
        if (position < 0 || position > indicatorCount) return

        val transition = TransitionSet()
            .setOrdering(TransitionSet.ORDERING_TOGETHER)
            .addTransition(ChangeBounds())
            .addTransition(Fade())
            .setDuration(150)

        TransitionManager.beginDelayedTransition(this, transition)

        val positionStates = IntArray(indicatorCount + 1)
        Arrays.fill(positionStates, STATE_GONE)

        val bigDots = 3
        val start = position - MAX_INDICATORS + bigDots
        var realStart = max(0, start)

        if (realStart + MAX_INDICATORS > indicatorCount) {
            realStart = indicatorCount - MAX_INDICATORS
            positionStates[indicatorCount - 1] = STATE_NORMAL
            positionStates[indicatorCount - 2] = STATE_NORMAL
        } else {
            positionStates[realStart + MAX_INDICATORS - 2] = STATE_SMALL
            positionStates[realStart + MAX_INDICATORS - 1] = STATE_MINI
        }

        if (position > MAX_INDICATORS / 2) {
            for (i in realStart until realStart + MAX_INDICATORS) {
                positionStates[i] = STATE_NORMAL
            }
            positionStates[realStart] = STATE_MINI

            if(position > indicatorCount - (bigDots + 1)) {
                positionStates[realStart + 1] = STATE_SMALL
            }
            else {
                positionStates[realStart + 1] = STATE_SMALL
                positionStates[realStart + (bigDots + 1)] = STATE_MINI
                positionStates[realStart + (bigDots)] = STATE_SMALL
            }
        } else {
            for (i in realStart until realStart + MAX_INDICATORS - 2) {
                positionStates[i] = STATE_NORMAL
            }
        }

        positionStates[position] = STATE_SELECTED
        updateCircleIndicators(positionStates)
    }

    private fun updateCircleIndicators(positionStates: IntArray) {
        for (i in 0 until indicatorCount) {
            val v = getChildAt(i)

            when (val state = positionStates[i]) {
                STATE_GONE -> v.visibility = View.GONE
                else -> {
                    v.visibility = View.VISIBLE
                    animateViewScale(v, state)
                    updateColor(v, state)
                }
            }
        }
    }

    private fun updateColor(view: View?, state: Int) {
        val gd = GradientDrawable()

        if(indicatorType == CIRCLE_INDICATOR_TYPE) {
            gd.shape = GradientDrawable.OVAL
        } else {
            gd.shape = GradientDrawable.RECTANGLE
            gd.cornerRadius = 3.toPx().toFloat()
        }

        if(state == STATE_SELECTED) gd.setColor(activeColor)
        else gd.setColor(inactiveColor)

        view?.background = gd
    }

    private fun animateViewScale(view: View?, state: Int) {
        if (view == null) return

        val scale = when (state) {
            STATE_NORMAL -> 1f
            STATE_SELECTED -> 1f
            STATE_SMALL -> 0.67f
            STATE_MINI -> 0.33f
            else -> 0f
        }

        view.animate()
            .scaleX(scale)
            .scaleY(scale)
    }

    companion object {
        const val MAX_INDICATORS = 5

        const val CIRCLE_INDICATOR_TYPE = 0
        const val OVAL_INDICATOR_TYPE = 1

        const val STATE_GONE = 0
        const val STATE_NORMAL = 1
        const val STATE_SELECTED = 2
        const val STATE_SMALL = 3
        const val STATE_MINI = 4

        const val COLOR_DEFAULT = 0
        const val COLOR_BLUE = 1
        const val COLOR_YELLOW = 2
        const val COLOR_RED = 3
        const val COLOR_INVERTED = 4
        const val COLOR_BLACK = 5
    }
}
