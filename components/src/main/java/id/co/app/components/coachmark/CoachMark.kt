package id.co.app.components.coachmark

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import id.co.app.components.R
import id.co.app.components.coachmark.util.ViewHelper
import id.co.app.components.typography.UnifyMotion
import id.co.app.components.utils.toPx

/**
 * Created by Lukas Kristianto on 08/08/21 18.22.
 * Sinarmas APP
 * lukas_kristianto@app.co.id
 */
class CoachMark(private val context: Context) : PopupWindow(context) {
    private lateinit var view: View
    private var layoutSimpleCoachMark: View? = null
    private var layoutStepCoachMark: View? = null
    var container: LinearLayout? = null
    var content: LinearLayout? = null

    var arrowIconPositionTop: ImageView? = null
    var arrowIconPositionBottom: ImageView? = null

    var simpleTitleView: TextView? = null
    var simpleDescriptionView: TextView? = null
    var simpleCloseIcon: ImageView? = null

    var stepContent: LinearLayout? = null
    var stepTitleView: TextView? = null
    var stepDescriptionView: TextView? = null
    var stepNext: TextView? = null
    var stepPrev: TextView? = null
    var stepCloseIcon: ImageView? = null
    var stepPagination: TextView? = null

    private val maxWidth = 312.toPx()
    private val maxWidthTablet = 360.toPx()
    private var displayWidth = 0
    private var displayHeight = 0
    private var parentScrollView: ViewGroup? = null

    /**
     * get current index of step coachMark
     */
    var currentIndex: Int = -1
    var coachMarkItem: ArrayList<CoachMarkItem> = arrayListOf()
    /**
     * set listener that will be invoked when the CoachMark Step is finished
     */
    var onFinishListener: () -> Unit = {}
    /**
     * set listener that will be invoked when the CoachMark is dismissed
     */
    var onDismissListener: () -> Unit = {}
    /**
     * set margin left for simple coachMark, before function [showCoachMark]
     */
    var simpleMarginLeft: Int = 8.toPx()
    /**
     * set margin right for simple coachMark, before function [showCoachMark]
     */
    var simpleMarginRight: Int = 8.toPx()
    /**
     * get coachMark width
     */
    var contentWidth = 0
    /**
     * get coachMark height
     */
    var contentHeight = 0
    var isDismissed = false
    /**
     * set coachMark clipping
     */
    var enableClipping = false
        set(value) {
            field = value

            isClippingEnabled = field
        }

    private var listener: OnStepListener? = null
    private var isDark = false
    private var isMeasured = false

    var stepButtonTextLastChild = "Mengerti"
    var stepButtonText = "Lanjut"

    init {
        initLayout()
        initCloseListener()
    }

    private fun initLayout() {
        view = LayoutInflater.from(context).inflate(R.layout.layout_coachmark2, null)
        container = view.findViewById(R.id.container)
        layoutSimpleCoachMark = view.findViewById(R.id.layout_simple_coachmark)
        layoutStepCoachMark = view.findViewById(R.id.layout_step_coachmark)

        arrowIconPositionTop = view.findViewById(R.id.arrow_position_top)
        arrowIconPositionBottom = view.findViewById(R.id.arrow_position_bottom)

        content = view.findViewById(R.id.content)

        simpleTitleView = view.findViewById(R.id.simple_title)
        simpleDescriptionView = view.findViewById(R.id.simple_description)
        simpleCloseIcon = view.findViewById(R.id.simple_ic_close)

        stepContent = view.findViewById(R.id.step_content)
        stepTitleView = view.findViewById(R.id.step_title)
        stepDescriptionView = view.findViewById(R.id.step_description)
        stepNext = view.findViewById(R.id.step_next)
        stepPrev = view.findViewById(R.id.step_prev)
        stepCloseIcon = view.findViewById(R.id.step_ic_close)
        stepPagination = view.findViewById(R.id.step_pagination)

        contentView = view

        val displayMetrics = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
        displayWidth = displayMetrics.widthPixels
        displayHeight = displayMetrics.heightPixels

        isDark = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES

        if(isDark) {
            arrowIconPositionTop?.setColorFilter(ContextCompat.getColor(context, R.color.Unify_N700))
            arrowIconPositionBottom?.setColorFilter(ContextCompat.getColor(context, R.color.Unify_N700))
            simpleCloseIcon?.setColorFilter(ContextCompat.getColor(context, R.color.Unify_N200))
            stepCloseIcon?.setColorFilter(ContextCompat.getColor(context, R.color.Unify_N200))
            stepPagination?.setTextColor(ContextCompat.getColor(context, R.color.Unify_N150))
        }

        arrowIconPositionBottom?.let {
            ViewCompat.setTranslationZ(it, 1f)
        }

        arrowIconPositionTop?.let {
            ViewCompat.setTranslationZ(it, 1f)
        }

        setBackgroundDrawable(null)
    }

    private fun initCloseListener() {
        simpleCloseIcon?.setOnClickListener {
            dismissCoachMark()
        }
        stepCloseIcon?.setOnClickListener {
            dismissCoachMark()
        }
    }

    private fun setSize(width: Int, height: Int) {
        setWidth(width)
        setHeight(height)
    }

    private fun isTablet(context: Context): Boolean {
        return ((context.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE)
    }

    private fun getLocation(anchorView: View): IntArray {
        val location = IntArray(2)
        anchorView.getLocationInWindow(location)
        return location
    }

    private fun getArrowWidth(): Int {
        arrowIconPositionBottom?.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        arrowIconPositionBottom?.let {
            return it.measuredWidth
        }
        return 0
    }

    private fun getStatusBarHeight(context: Context): Int {
        var height = 0
        val resId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resId > 0) {
            height = context.resources.getDimensionPixelSize(resId)
        }
        return height
    }

    private fun calculateStepContentSize(title: CharSequence= "", description: CharSequence= "") {
        if(title!= "") {
            stepTitleView?.visibility = View.VISIBLE
            stepTitleView?.text = title
        } else {
            stepTitleView?.visibility = View.GONE
        }

        if(description!= "") {
            stepDescriptionView?.visibility = View.VISIBLE
            stepDescriptionView?.text = description
        } else {
            stepDescriptionView?.visibility = View.GONE
        }

        //set coachMark view width
        contentWidth = if (isTablet(context)) maxWidthTablet else displayWidth

        //get content height based on width
        view.measure(View.MeasureSpec.makeMeasureSpec(contentWidth, View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
        contentHeight = view.measuredHeight
    }

    private fun setUpStepCoachMark(anchorView: View, title: CharSequence= "", description: CharSequence= "", pos: Int = -1) {
        layoutSimpleCoachMark?.visibility = View.GONE
        layoutStepCoachMark?.visibility = View.VISIBLE

        calculateStepContentSize(title, description)
        setSize(contentWidth, WindowManager.LayoutParams.WRAP_CONTENT)

        anchorView.post {
            //get location of anchorView
            val anchorLeft = getLocation(anchorView)[0]
            val anchorTop = getLocation(anchorView)[1] - getStatusBarHeight(context)

            //get anchor view width & height
            val anchorWidth: Int = anchorView.measuredWidth
            val anchorHeight: Int = anchorView.measuredHeight

            //calculate center location of anchorView
            val anchorHorizontalCenter = anchorLeft + anchorWidth / 2
            //calculate left location of coachMark view based on the center location of anchorView
            val left = anchorHorizontalCenter - contentWidth / 2
            //calculate right location of coachMark view based on the center location of anchorView
            val right = left + contentWidth
            //calculate left distance of coachMark view from edge screen
            val leftPadding =  Math.max(0, if (right > displayWidth) displayWidth - contentWidth else left)

            // setup position
            var position: Int
            if (pos == -1) {
                // set position programmatically, default bottom
                position = POSITION_BOTTOM
                // calculate bottom space based on anchor y position, anchor view height, and coachMark view height
                val highlightBottom = displayHeight - anchorTop + anchorHeight + contentHeight

                // if there's no space in bottom for coachMark, position move to top
                if (anchorTop > highlightBottom) {
                    position = POSITION_TOP
                }
            } else {
                // use client defined position
                position = pos
            }

            if(!isShowing) {
                // setup pivot for animation
                val pivotX = (anchorHorizontalCenter - leftPadding - getArrowWidth() / 2).toFloat()
                val pivotY = if (position == POSITION_TOP) contentHeight.toFloat() else 0f
                contentView.pivotX = pivotX  + 8.toPx()
                contentView.pivotY = pivotY

                if (position == POSITION_TOP) {
                    container?.setPadding(0, 16.toPx(), 0, 0)
                    if(!(context as Activity).isFinishing){
                        showAsDropDown(anchorView, leftPadding - anchorLeft, 0 - contentHeight - anchorHeight + 4.toPx())
                    }

                    // set arrow location based on pivotX
                    val layoutParams = arrowIconPositionTop?.layoutParams as ViewGroup.MarginLayoutParams
                    layoutParams.leftMargin = pivotX.toInt()
                    arrowIconPositionTop?.layoutParams = layoutParams

                    arrowIconPositionTop?.visibility = View.VISIBLE
                    arrowIconPositionBottom?.visibility = View.GONE

                } else {
                    container?.setPadding(0, 0, 0, 16.toPx())
                    if(!(context as Activity).isFinishing) {
                        showAsDropDown(anchorView, leftPadding - anchorLeft, 4.toPx())
                    }

                    // set arrow location based on pivotX
                    val layoutParams = arrowIconPositionBottom?.layoutParams as ViewGroup.MarginLayoutParams
                    layoutParams.leftMargin = pivotX.toInt()
                    arrowIconPositionBottom?.layoutParams = layoutParams

                    arrowIconPositionTop?.visibility = View.GONE
                    arrowIconPositionBottom?.visibility = View.VISIBLE
                }
            }
        }

    }

    private fun setUpSingleCoachMark(anchorView: View, title: CharSequence= "", description: CharSequence= "", pos: Int = -1) {
        layoutSimpleCoachMark?.visibility = View.VISIBLE
        layoutStepCoachMark?.visibility = View.GONE

        if(title!= "") {
            simpleTitleView?.visibility = View.VISIBLE
            simpleTitleView?.text = title
        } else {
            simpleTitleView?.visibility = View.GONE
        }

        if(description!= "") {
            simpleDescriptionView?.visibility = View.VISIBLE
            simpleDescriptionView?.text = description
        } else {
            simpleDescriptionView?.visibility = View.GONE
        }

        if(!isMeasured) {
            view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
            //get coachMark view width
            contentWidth = view.measuredWidth
            if (contentWidth > maxWidth) {
                //if the coachMark view width is more than max width, set to defined max width
                contentWidth = maxWidth
            }
            setSize(contentWidth + simpleMarginRight + simpleMarginLeft + 5.toPx(), WindowManager.LayoutParams.WRAP_CONTENT)

            container?.setPadding(0, 14.toPx(), 0, 14.toPx())
            view.measure(View.MeasureSpec.makeMeasureSpec(contentWidth, View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
            //get content height
            contentHeight = view.measuredHeight
            isMeasured = true
        }

        anchorView.post {
            //get location of anchorView
            val anchorLeft = getLocation(anchorView)[0]
            val anchorTop = getLocation(anchorView)[1] - getStatusBarHeight(context)

            //get anchor view width & height
            val anchorWidth: Int = anchorView.measuredWidth
            val anchorHeight: Int = anchorView.measuredHeight

            //calculate center location of anchorView
            val anchorHorizontalCenter = anchorLeft + anchorWidth / 2
            //calculate left location of coachMark view based on the center location of anchorView
            val left = anchorHorizontalCenter - contentWidth / 2
            //calculate right location of coachMark view based on the center location of anchorView
            val right = left + contentWidth
            //calculate left distance of coachMark view from edge screen
            val leftPadding =  Math.max(0, if (right > displayWidth) displayWidth - contentWidth else left)

            // setup position
            var position: Int
            if (pos == -1) {
                // set position programmatically, default bottom
                position = POSITION_BOTTOM
                // calculate bottom space based on anchor y position, anchor view height, and coachMark view height
                val highlightBottom = displayHeight - anchorTop + anchorHeight + contentHeight

                // if there's no space in bottom for coachMark, position move to top
                if (anchorTop > highlightBottom) {
                    position = POSITION_TOP
                }
            } else {
                // use client defined position
                position = pos
            }

            // setup margin left & right based on client
            if(right > displayWidth)
                // if the position is on the right
                content?.setPadding(0, 0, simpleMarginRight - 16.toPx(), 0)
            else if(right == displayWidth / 2)
                //if the position is on the center
                content?.setPadding(simpleMarginLeft - 8.toPx(), 0, simpleMarginRight - 8.toPx(), 0)
            else
                //if the position is on the left
                content?.setPadding(simpleMarginLeft - 16.toPx(), 0, 0, 0)

            if (!isShowing) {
                // setup pivot for animation
                val pivotX = (anchorHorizontalCenter - leftPadding + if(right > displayWidth) simpleMarginRight else - getArrowWidth() / 2).toFloat()
                val pivotY = if (position == POSITION_TOP) contentHeight.toFloat() - 32.toPx() else 0f
                contentView.pivotX = pivotX + 8.toPx()
                contentView.pivotY = pivotY

                var arrowIconPositionAdjustment = 0
                val xOff = leftPadding - anchorLeft
                val contentScreenOffset = anchorLeft + anchorView.measuredWidth - xOff
                var realXOff = if (contentScreenOffset - displayWidth > 0) (anchorLeft + contentWidth - displayWidth) * -1 - 30.toPx() else xOff
                if (contentScreenOffset - displayWidth > 0) {
                    arrowIconPositionAdjustment = 8.toPx()
                }
                if (leftPadding < 8.toPx()) {
                    realXOff += 8.toPx()
                    arrowIconPositionAdjustment = (-8).toPx()
                }

                if (position == POSITION_TOP) {
                    container?.setPadding(0, 16.toPx(), 0, 0)
                    if(!(context as Activity).isFinishing) {
                        var yOff = 0 - contentHeight - anchorHeight + 32.toPx()
                        showAsDropDown(
                            anchorView,
                            realXOff,
                            yOff
                        )
                    }

                    // set arrow location based on pivotX
                    val layoutParams = arrowIconPositionTop?.layoutParams as ViewGroup.MarginLayoutParams
                    layoutParams.leftMargin = pivotX.toInt() + arrowIconPositionAdjustment
                    arrowIconPositionTop?.layoutParams = layoutParams

                    arrowIconPositionTop?.visibility = View.VISIBLE
                    arrowIconPositionBottom?.visibility = View.GONE

                } else {
                    container?.setPadding(0, 0, 0, 16.toPx())
                    if(!(context as Activity).isFinishing) {
                        showAsDropDown(anchorView, realXOff, 4.toPx())
                    }

                    // set arrow location based on pivotX
                    val layoutParams = arrowIconPositionBottom?.layoutParams as ViewGroup.MarginLayoutParams
                    layoutParams.leftMargin = pivotX.toInt() + arrowIconPositionAdjustment
                    arrowIconPositionBottom?.layoutParams = layoutParams

                    arrowIconPositionTop?.visibility = View.GONE
                    arrowIconPositionBottom?.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun scrollTo(item: CoachMarkItem) {
        parentScrollView?.let {
            item.anchorView.post {
                val relativeLocation = IntArray(2)
                ViewHelper.getRelativePositionRec(item.anchorView, it, relativeLocation)
                calculateStepContentSize(item.title, item.description)
                val smoothScroll = ObjectAnimator.ofInt(it, "scrollY", relativeLocation[1] - contentHeight)
                smoothScroll.apply {
                    duration = UnifyMotion.T3
                    addListener(object: Animator.AnimatorListener {
                        override fun onAnimationRepeat(p0: Animator?) {

                        }

                        override fun onAnimationEnd(p0: Animator?) {
                            if(!isDismissed)
                                setUpStepCoachMark(item.anchorView, item.title, item.description, item.position).let {
                                    animateShow()
                                }
                        }

                        override fun onAnimationCancel(p0: Animator?) {

                        }

                        override fun onAnimationStart(p0: Animator?) {

                        }

                    })
                    start()
                }
            }
        }

    }

    /**
     * show [CoachMarkItem] to CoachMark2
     * @param step step == 1 item will make simple coachMark, step > 1 item will create step coachMark
     * @param viewGroup fill this parameter with scrollView id so that the coachMark can move according to the target in the scrollView
     * @param index set start index
     */
    fun showCoachMark(step: ArrayList<CoachMarkItem>, viewGroup: ViewGroup? = null, index: Int = 0) {
        if (!isCoachmmarkShowAllowed) {
            Toast.makeText(context, "Coachmark show is disabled via isCoachmmarkShowAllowed", Toast.LENGTH_LONG).show()
            return
        }

        /**
         * validate target collection not empty & index is between 0 and size - 1
         */
        if(step.size == 0 || index !in 0 until step.size) return

        if(!isShowing) {
            contentView.alpha = 0.0f
            contentView.scaleX = 0.7f
            contentView.scaleY = 0.7f
        }

        currentIndex = index
        coachMarkItem = step
        parentScrollView = viewGroup
        enableClipping = false

        if(step.size > 1) {
            if(!isDismissed) {
                setUpStepCoachMark(step[currentIndex].anchorView, step[currentIndex].title, step[currentIndex].description, step[currentIndex].position)
                Handler(Looper.getMainLooper()).postDelayed({

                    /**
                     * set text to `Mengerti` if user show last index directly
                     */
                    if(currentIndex == step.size - 1) {
                        stepNext?.text = stepButtonTextLastChild
                    }

                    animateShow()
                }, 300)
            }

            stepPagination?.text = "${currentIndex + 1} of ${step.size}"

            if(currentIndex == 0) {
                stepPrev?.visibility = View.GONE
            } else {
                stepPrev?.visibility = View.VISIBLE
            }

            stepNext?.setOnClickListener {
                if(currentIndex == step.size - 1) {
                    dismissCoachMark()
                    onFinishListener()
                } else {
                    hideCoachMark()
                }
                currentIndex++

                if (currentIndex < step.size) {
                    stepPagination?.text = "${currentIndex + 1} of ${step.size}"

                    if(currentIndex == 0) {
                        stepPrev?.visibility = View.GONE
                    } else {
                        stepPrev?.visibility = View.VISIBLE
                    }

                    if(currentIndex == step.size - 1) {
                        stepNext?.text = stepButtonTextLastChild
                    }

                    listener?.onStep(currentIndex, step[currentIndex])

                    if (parentScrollView != null) {
                        scrollTo(step[currentIndex])
                    } else {
                        if(!isDismissed)
                            setUpStepCoachMark(step[currentIndex].anchorView, step[currentIndex].title, step[currentIndex].description, step[currentIndex].position).let {
                                animateShow()
                            }
                    }
                }
            }

            stepPrev?.setOnClickListener {
                hideCoachMark()
                currentIndex--

                if (currentIndex >= 0) {
                    stepPagination?.text = "${currentIndex + 1} of ${step.size}"

                    if(currentIndex == 0) {
                        stepPrev?.visibility = View.GONE
                    } else {
                        stepPrev?.visibility = View.VISIBLE
                    }

                    if(currentIndex < step.size) {
                        stepNext?.text = stepButtonText
                    }

                    listener?.onStep(currentIndex, step[currentIndex])

                    if (parentScrollView != null) {
                        scrollTo(step[currentIndex])
                    } else {
                        if(!isDismissed)
                            setUpStepCoachMark(step[currentIndex].anchorView, step[currentIndex].title, step[currentIndex].description, step[currentIndex].position).let {
                                animateShow()
                            }
                    }
                }
            }
        } else {
            if(!isDismissed) {
                setUpSingleCoachMark(
                    step[0].anchorView,
                    step[0].title,
                    step[0].description,
                    step[0].position
                ).let {
                    animateShow()
                }
            }
        }

        setTouchInterceptor(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent): Boolean {
                if (event.action == MotionEvent.ACTION_OUTSIDE) {
                    if(isOutsideTouchable) dismissCoachMark()
                    return true
                }
                return false
            }
        })
    }

    /**
     * trigger dismiss coachMark
     */
    fun dismissCoachMark() {
        if(!isShowing) return
        if(!isDismissed) {
            contentView.animate().scaleX(0.7f).scaleY(0.7f).alpha(0.0f).setInterpolator(UnifyMotion.EASE_OUT).setDuration(UnifyMotion.T3)
                .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    dismiss()
                    onDismissListener.invoke()
                    isDismissed = true
                }
            })
        }

    }

    /**
     * trigger hide coachMark
     */
    fun hideCoachMark() {
        if(!isShowing) return
        animateHide()
        dismiss()
        isDismissed = false
    }

    fun animateShow() {
        contentView.animate().scaleX(1.0f).scaleY(1.0f).alpha(1.0f).setInterpolator(UnifyMotion.EASE_OVERSHOOT).setDuration(UnifyMotion.T3).setListener(null)
    }

    fun animateHide() {
        contentView.animate().scaleX(0.7f).scaleY(0.7f).alpha(0.0f).setInterpolator(UnifyMotion.EASE_OUT).setDuration(UnifyMotion.T3).setListener(null)
    }

    fun setStepListener(listener: OnStepListener) {
        this.listener = listener
    }

    interface OnStepListener {
        /**
         * @param currentIndex start from 0
         * @param coachMarkItem current item
         */
        fun onStep(currentIndex: Int, coachMarkItem: CoachMarkItem)
    }

    companion object {
        const val POSITION_BOTTOM = 0
        const val POSITION_TOP = 1

        /**
         * Coachmark module variable for disabled / enabled all coachmark2 obj show method
         */
        var isCoachmmarkShowAllowed = true
    }
}