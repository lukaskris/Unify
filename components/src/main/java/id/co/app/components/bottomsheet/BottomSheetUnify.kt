package id.co.app.components.bottomsheet

/**
 * Created by Lukas Kristianto on 15/07/21 09.30.
 * App Sinarmas
 * lukas_kristianto@app.co.id
 */

import android.app.Activity
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import id.co.app.components.R
import id.co.app.components.utils.toPx


open class BottomSheetUnify : BottomSheetDialogFragment() {
    private var titleText = ""
    private var actionText = ""
    private var actionIcon: Drawable? = null
    private var child: View? = null
    private var isShow = false
    private var displayMetrix = DisplayMetrics()
    private var whiteContainerBackground: Drawable? = null

    var isDragable: Boolean = false
    var isHideable: Boolean = false
    var isFullpage: Boolean = false
    var customPeekHeight: Int = 200
    var showKnob: Boolean = false
    var showHeader: Boolean = true
    var showCloseIcon: Boolean = true
    var overlayClickDismiss: Boolean = true
    var clearContentPadding: Boolean = false
    var isKeyboardOverlap: Boolean = true
    var isSkipCollapseState: Boolean = false
    var bottomSheetBehaviorDefaultState = BottomSheetBehavior.STATE_COLLAPSED

    /**
     * stored listener, to make listener is easy to update without set new listener on component
     */
    private var actionListener: (View) -> Unit = {}
    private var closeListener: (View) -> Unit = {
        dismiss()
    }
    private var dismissDialogListener: () -> Unit = {}
    private var showDialogListener: () -> Unit = {}

    private lateinit var paramLayoutTitle: RelativeLayout.LayoutParams
    private lateinit var paramLayoutClose: RelativeLayout.LayoutParams
    private lateinit var baseView: View

    /**
     * View variable
     */
    lateinit var bottomSheetTitle: TextView
    lateinit var bottomSheetWrapper: ViewGroup
    lateinit var bottomSheetClose: ImageView
    lateinit var bottomSheetAction: TextView
    lateinit var bottomSheetHeader: ViewGroup
    lateinit var frameDialogView: View
    lateinit var knobView: View
    var bottomSheet = BottomSheetBehavior<View>()

    // -------------------- Override --------------------

    /**
     * Initialize variable
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!isKeyboardOverlap) {
            setStyle(DialogFragment.STYLE_NORMAL, R.style.UnifyBottomSheetNotOverlapStyle)
        } else {
            setStyle(DialogFragment.STYLE_NORMAL, R.style.UnifyBottomSheetOverlapStyle)
        }
    }

    /**
     *  Inflate unify bottom sheet design
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        baseView = inflater.inflate(R.layout.bottom_sheet_layout, container, false)

        /**
         * Text set on this lifecycle, to get the real height when layouting process
         */
        baseView.findViewById<TextView>(R.id.bottom_sheet_title).text = titleText

        if (child != null) {
            (baseView as ViewGroup).addView(child)
        } else if (child == null && !isShow) {
            dismiss()
        }

        dialog?.setOnShowListener {
            showListener()
        }

        /**
         * initialize all view variable
         */
        bottomSheetWrapper = baseView.findViewById(R.id.bottom_sheet_wrapper)
        bottomSheetClose = baseView.findViewById(R.id.bottom_sheet_close)
        bottomSheetTitle = baseView.findViewById(R.id.bottom_sheet_title)
        bottomSheetAction = baseView.findViewById(R.id.bottom_sheet_action)
        bottomSheetHeader = baseView.findViewById(R.id.bottom_sheet_header)
        knobView = baseView.findViewById(R.id.bottom_sheet_knob)

        /**
         * load the background only when bottomsheet need to be show
         */
        if(whiteContainerBackground == null){
            whiteContainerBackground = AppCompatResources.getDrawable(inflater.context, R.drawable.bottomsheet_background)
        }

        /**
         * bottomsheet have 2 background
         * 1. bottomsheet_background.xml contain white rounded background for dialog fragment
         * 2. bottomsheet_background_and_shadow.xml contain layer list combine bottomsheet_background.xml & bottomsheet_shadow.9patch
         * 2nd background is used for BottomSheetBehavior when included directly to activity (bottomsheet without overlay)
         * 1nd background is used for BottomSheetDialogFragment (bottomsheet with overlay)
         * to avoid the conditional on bottomsheet without overlay so the background default from bottom_sheet_layout.xml using 2nd background and change to 1st background
         */
        bottomSheetWrapper.background = whiteContainerBackground

        bottomSheetHeader.visibility = if (showHeader) View.VISIBLE else View.GONE
        bottomSheetClose.visibility = if (showCloseIcon) View.VISIBLE else View.GONE
        knobView.visibility = if (showKnob) View.VISIBLE else View.GONE
        if (clearContentPadding) {
            bottomSheetWrapper.setPadding(0, 16.toPx(), 0, 0)
            (bottomSheetHeader.layoutParams as LinearLayout.LayoutParams).setMargins(
                16.toPx(),
                0,
                16.toPx(),
                16.toPx()
            )
        }

        (requireContext() as Activity).windowManager.defaultDisplay.getMetrics(displayMetrix)

        return baseView
    }

    /**
     * Invoke initDesign for every created to calculate component, show mean created
     */
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        initDesign()
        super.onActivityCreated(savedInstanceState)

        dialog?.window?.setWindowAnimations(R.style.UnifyBottomsheetWindowAnimation)
    }

    /**
     * Clear all view onDestroy, dismiss means destroy
     */
    override fun onDestroy() {
        if (::baseView.isInitialized && baseView is ViewGroup) {
            (baseView as ViewGroup).removeAllViews()
        }
        super.onDestroy()
    }

    /**
     * Call the dismiss listener when bottomsheet is close
     */
    override fun onStop() {
        isShow = false
        super.onStop()
    }

    override fun onDismiss(dialog: DialogInterface) {
        dismissDialogListener()

        super.onDismiss(dialog)
    }

    /**
     * Prevent bottomsheet added fragment twice
     */
    override fun show(manager: FragmentManager, tag: String?) {
        if (!isShow) {
            isShow = true
            val ft = manager.beginTransaction()
            ft.add(this, tag)
            ft.commitAllowingStateLoss()
        }
    }

    /**
     * Prevent bottomsheet added fragment twice
     */
    override fun show(transaction: FragmentTransaction, tag: String?): Int {
        if (!isShow) {
            isShow = true
            return super.show(transaction, tag)
        }
        return -1
    }

    /**
     * Make all dismiss become stateless
     */
    override fun dismiss() {
        super.dismissAllowingStateLoss()
    }

    // -------------------- Private Method --------------------

    /**
     * Listener that used to determine bottomsheet variant every time bottomsheet is calling / invoke to show
     * @param dialogInterface interface from the dialog fragment
     */
    private fun showListener() {
        dialog?.setCanceledOnTouchOutside(overlayClickDismiss)

        bottomSheetClose.setOnClickListener(closeListener)

        actionLayout(bottomSheetAction, actionText, actionIcon, actionListener)

        frameDialogView = bottomSheetWrapper.parent as View
        frameDialogView.setBackgroundColor(Color.TRANSPARENT)

        frameDialogView.bringToFront()

        bottomSheet = BottomSheetBehavior.from(frameDialogView)

        /**
         * set peekheight so user cant drag down the bottomsheet
         */
        if (isFullpage && !isDragable) {    // full page & not dragable
            frameDialogView.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT

            bottomSheet.peekHeight = displayMetrix.heightPixels
        } else if (!isFullpage && !isDragable) { // not full page & not dragable
            bottomSheet.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(p0: View, p1: Float) {

                }

                override fun onStateChanged(p0: View, p1: Int) {
                    /**
                     * Set peekheight here because need to get view height, view height can be get after rendered
                     * peek height obtained from wrapper parent height because parent background have another padding from 9patch
                     */
                    bottomSheet.peekHeight = (bottomSheetWrapper.parent as View).height

                    if (isHideable && p1 == BottomSheetBehavior.STATE_HIDDEN) {
                        dismiss()
                    }
                }
            })
        } else { // draggable
            var mPeekHeight = customPeekHeight.toPx()
            if (isSkipCollapseState) {
                mPeekHeight = if (isFullpage) {
                    displayMetrix.heightPixels
                } else {
                    (bottomSheetWrapper.parent as View).height
                }
            }
            bottomSheet.peekHeight = mPeekHeight
            bottomSheet.state = bottomSheetBehaviorDefaultState

            if (isFullpage) {
                frameDialogView.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
            }

            bottomSheet.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(p0: View, p1: Float) {

                }

                override fun onStateChanged(p0: View, p1: Int) {
                    if (p1 == BottomSheetBehavior.STATE_HIDDEN) {
                        dismiss()
                    }
                }
            })
        }

        bottomSheet.isHideable = isHideable
        bottomSheet.state = if (!isDragable || isFullpage) {
            BottomSheetBehavior.STATE_EXPANDED
        } else {
            BottomSheetBehavior.STATE_COLLAPSED
        }

        // ======================================

        showDialogListener()
    }

    /**
     * Initialize design on layout
     */
    private fun initDesign() {
        paramLayoutClose = bottomSheetClose.layoutParams as RelativeLayout.LayoutParams
        paramLayoutTitle = bottomSheetTitle.layoutParams as RelativeLayout.LayoutParams
    }

    /**
     * -------------------- Public Method --------------------
     */

    /**
     * Set title for bottomsheet
     * @param newTitle title for bottomsheet, use empty string to clear it
     */
    fun setTitle(newTitle: String) {
        titleText = newTitle

        if (::bottomSheetTitle.isInitialized) {
            bottomSheetTitle.text = newTitle
        }
    }

    /**
     * Set bottomsheet to have string action on right of title
     * @param actionTextParam text for action
     * @param actionListenerParam onClickListener for text action
     */
    fun setAction(actionTextParam: String, actionListenerParam: (View) -> Unit) {
        actionText = actionTextParam
        actionListener = actionListenerParam
        actionIcon = null
    }

    /**
     * Set bottomsheet to have drawable icon action on right of title
     * @param actionIconParam drawable for action
     * @param actionListenerParam onClickListener for icon action
     */
    fun setAction(actionIconParam: Drawable?, actionListenerParam: (View) -> Unit) {
        actionIcon = actionIconParam
        actionListener = actionListenerParam
        actionText = ""
    }

    /**
     * Clear bottomsheet action include text / drawable & actionListener
     */
    fun clearAction() {
        actionText = ""
        actionIcon = null
    }

    /**
     * Set the bottomsheet to full page
     * @param state true mean bottomsheet will full from bottom to top
     */
    @Deprecated(
        replaceWith = ReplaceWith("isFullpage"),
        message = "Please use isFullpage attribute"
    )
    fun setFullPage(state: Boolean) {
        isFullpage = state
    }

    /**
     * Set onClickListener to close button
     * @param listener onClickListener that will be bind to close button
     */
    fun setCloseClickListener(listener: (View) -> Unit) {
        closeListener = listener
    }

    /**
     * Place child (View) to bottomsheet, child position will be below the bottomsheet header
     * @param childParam view that will be placed inside the bottomsheet
     */
    fun setChild(childParam: View?) {
        child = childParam
    }

    /**
     * Listener that will be invoke when the bottomsheet is dismiss
     * @param paramDismisListener function that will be called when bottomsheet dismiss
     */
    fun setOnDismissListener(paramDismisListener: () -> Unit) {
        dismissDialogListener = paramDismisListener
    }

    /**
     * Set the overlay onClick state, true mean overlay clicked will invoke bottomsheet dismiss
     * @param paramState true mean clicked overlay will dismiss bottomsheet
     */
    @Deprecated(
        replaceWith = ReplaceWith("overlayClickDismiss"),
        message = "Please use overlayClickDismiss attribute"
    )
    fun setOverlayClose(paramState: Boolean) {
        overlayClickDismiss = paramState
    }

    /**
     * Set listener after the bottomsheet is visible
     * @param showListener listener that will run
     */
    fun setShowListener(showListener: () -> Unit) {
        showDialogListener = showListener
    }

    /**
     * Unlock version of bottomsheet, discard the header (close button, title, action)
     * @param state true mean bottomsheet without header
     */
    @Deprecated(
        replaceWith = ReplaceWith("showHeader"),
        message = "Please use showHeader attribute"
    )
    fun clearHeader(state: Boolean) {
        showHeader = !state
    }

    /**
     * Discard the close button
     * @param state true mean bottomsheet without close button
     */
    @Deprecated(
        replaceWith = ReplaceWith("showCloseIcon"),
        message = "Please use showCloseIcon attribute"
    )
    fun clearClose(state: Boolean) {
        showCloseIcon = !state
    }

    /**
     * ===== BottomSheetBehavior Helper =====
     */
    companion object {
        fun bottomSheetBehaviorKnob(viewContent: View, isShow: Boolean) {
            viewContent.findViewById<View>(R.id.bottom_sheet_knob).visibility =
                if (isShow) View.VISIBLE else View.GONE
        }

        fun bottomSheetBehaviorHeader(viewContent: View, isShow: Boolean) {
            viewContent.findViewById<View>(R.id.bottom_sheet_header).visibility =
                if (isShow) View.VISIBLE else View.GONE
        }

        fun bottomsheetBehavCustomRoundedShadow(viewContent: View, isRounded: Boolean = true, hasShadow: Boolean = true) {
            var shadowDrawable: Drawable? = null
            var roundedDrawable: Drawable? = null

            /**
             * Set the background between rounded asset / solid rect shape
             */
            roundedDrawable = if (isRounded) {
                ContextCompat.getDrawable(viewContent.context, R.drawable.bottomsheet_background)
            } else {
                ColorDrawable(ContextCompat.getColor(viewContent.context, R.color.bottomsheetunify_background))
            }

            /**
             * Choose shadow 9patch asset between sharp edge / rounded edge
             */
            if (hasShadow) {
                shadowDrawable = if (isRounded) ContextCompat.getDrawable(viewContent.context, R.drawable.bottomsheet_shadow)
                else ContextCompat.getDrawable(viewContent.context, R.drawable.bottomsheet_shadow_no_rounded)
            }

            val padding = viewContent.context.resources.getDimension(R.dimen.bottom_sheet_wrapper_padding_ltr).toInt()
            val bottomsheetWrapper: LinearLayout = viewContent.findViewById(R.id.bottom_sheet_wrapper)

            bottomsheetWrapper.background = LayerDrawable(arrayOf(shadowDrawable ?: ColorDrawable(Color.TRANSPARENT), roundedDrawable))

            /**
             * When 9patch asset is attached to view it will use the asset padding,
             * so we need to set it again with value that we want
             */
            bottomsheetWrapper.setPadding(padding, padding, padding, bottomsheetWrapper.paddingBottom)
        }

        fun bottomSheetBehaviorTitle(
            viewContent: View,
            title: CharSequence? = null,
            actionText: CharSequence? = null,
            actionDrawable: Drawable? = null,
            actionListener: (View) -> Unit = {}
        ) {
            bottomSheetBehaviorHeader(viewContent, true)
            viewContent.findViewById<ImageView>(R.id.bottom_sheet_close).visibility = View.GONE

            val titleView = viewContent.findViewById<TextView>(R.id.bottom_sheet_title)
            titleView.text = title

            val actionView = viewContent.findViewById<TextView>(R.id.bottom_sheet_action)

            actionLayout(actionView, actionText, actionDrawable, actionListener)
        }

        /**
         * not announced function to get main view references from bottomsheet template
         */
        fun getBottomSheetUnifyViewReference(view: View): ArrayList<View> {
            val result = ArrayList<View>(4)

            val headerGroup = view.findViewById<ViewGroup>(R.id.bottom_sheet_header)
            val closeView = view.findViewById<ImageView>(R.id.bottom_sheet_close)
            val actionView = view.findViewById<TextView>(R.id.bottom_sheet_action)
            val knobView = view.findViewById<View>(R.id.bottom_sheet_knob)

            result.add(headerGroup)
            result.add(closeView)
            result.add(actionView)
            result.add(knobView)

            return result
        }

        /**
         * ===== View initializer for both BottomSheetFragment & BottomSheetBehavior =====
         */

        /**
         * Centralise action logic between fragment & behavior
         * @param actionView action view reference
         * @param actionText charsequence action, only applied once between text / drawable
         * @param actionDrawable drawable action, only applied once between text / drawable
         * @param actionClickListener click listener that will be bind on action view
         */
        private fun actionLayout(
            actionView: TextView,
            actionText: CharSequence? = null,
            actionDrawable: Drawable? = null,
            actionClickListener: (View) -> Unit
        ) {
            if (!actionText.isNullOrEmpty() || actionDrawable !== null) {
                actionView.visibility = View.VISIBLE
                actionView.text = ""

                if (!actionText.isNullOrEmpty()) {
                    actionView.text = actionText
                    actionView.background = null
                }

                if (actionDrawable !== null) {
                    actionView.background = actionDrawable
                }

                actionView.setOnClickListener(actionClickListener)
            } else {
                actionView.visibility = View.GONE
            }
        }
    }
}