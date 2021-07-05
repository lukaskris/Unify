package id.co.app.components.text

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Handler
import android.text.*
import android.text.TextUtils.isEmpty
import android.text.method.LinkMovementMethod
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.google.android.material.textfield.TextInputLayout
import id.co.app.components.R
import id.co.app.components.icon.IconUnify
import id.co.app.components.loader.LoaderUnify
import id.co.app.components.utils.*
import kotlinx.android.synthetic.main.textfield2_layout.view.*
import java.lang.reflect.Method

/**
 * Created by Lukas Kristianto on 7/3/2021.
 * App Sinarmas
 * lukas_kristianto@app.co.id
 */
open class TextFieldUnify(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {
    /**
     * get wrapper/layout reference of the TextField
     */
    var textInputLayout: TextInputLayout
    /**
     * get edit text reference of the TextField
     */
    var editText: AutoCompleteTextView
    /**
     * get icon container reference of the TextField
     */
    var iconContainer: LinearLayout
    /**
     * get first icon reference of the TextField
     */
    var icon1: ImageView
    /**
     * get second icon reference of the TextField
     */
    var icon2: ImageView
    /**
     * get label reference of the TextField
     */
    var labelText: TextView
    /**
     * add focus changed listener
     */
    var addOnFocusChangeListener: ((view: View, hasFocus: Boolean) -> Unit)? = null

    var loaderView: LoaderUnify

    var clearIconView: IconUnify

    var isClearable: Boolean = false
        set(value) {
            field = value

            if(value) {
                clearIconView.visibility = View.VISIBLE
                clearIconView.clearAnimation()
                clearIconView.animate().scaleX(0f).scaleY(0f).setDuration(0).start()

                editText.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(p0: Editable?) {
                    }

                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    }

                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        if(TextUtils.isEmpty(p0)) {
                            clearIconView.clearAnimation()
                            clearIconView.animate().scaleX(0f).scaleY(0f).setDuration(200).start()
                        } else {
                            clearIconView.clearAnimation()
                            clearIconView.animate().scaleX(1f).scaleY(1f).setDuration(200).start()
                        }
                    }
                })

                clearIconView.setOnClickListener {
                    editText.text.clear()
                }
            } else {
                clearIconView.visibility = View.GONE
            }
        }

    var isLoading: Boolean = false
        set(value) {
            field = value

            if (value) {
                loaderView.visibility = View.VISIBLE
                text_field_custom_icon_container.visibility = View.GONE
            } else {
                loaderView.visibility = View.GONE
                text_field_custom_icon_container.visibility = View.VISIBLE
            }
        }

    var disabledStateList = arrayOf(
        IntArray(1) { android.R.attr.state_enabled },
        IntArray(1) { -android.R.attr.state_enabled }
    )

    /**
     * static label / helper (below box) / counter text  color disabled/enabled
     * 0 = enable | 1 = disabled
     */
    var secondaryColorList = IntArray(2).apply {
        this[0] = ContextCompat.getColor(context, R.color.Unify_NN600)
        this[1] = ContextCompat.getColor(context, R.color.Unify_NN400)
    }

    /**
     * AutoComplete / typing text (input) color disabled/enabled
     * 0 = enable | 1 = disabled
     */
    var primaryColorList = IntArray(2).apply {
        this[0] = ContextCompat.getColor(context, R.color.Unify_NN950)
        this[1] = ContextCompat.getColor(context, R.color.Unify_NN400)
    }

    /**
     * AutoComplete / typing text (placeholder) color disabled/enabled
     * 0 = enable | 1 = disabled
     */
    var hintEmptyColorList = IntArray(2).apply {
        this[0] = ContextCompat.getColor(context, R.color.Unify_NN600)
        this[1] = ContextCompat.getColor(context, R.color.Unify_NN400)
    }

    var secondaryColorStateList = ColorStateList(disabledStateList, secondaryColorList)
    var primaryColorStateList = ColorStateList(disabledStateList, primaryColorList)
    var hintEmptyColorStateList = ColorStateList(disabledStateList, hintEmptyColorList)

    private val errorBorderTextColor = ColorStateList(disabledStateList, IntArray(2){ContextCompat.getColor(context, R.color.Unify_RN500)})

    private val standbyBorderColor = ContextCompat.getColorStateList(context, R.color.textfieldunify_stroke_color)

    /**
     * pre & post text label color disabled/enabled
     * 0 = enable | 1 = disabled
     */
    private val textPrePostColor = IntArray(2).apply {
        this[0] = ContextCompat.getColor(context, R.color.Unify_NN600)
        this[1] = ContextCompat.getColor(context, R.color.Unify_NN400)
    }

    var counterView: TextView? = null

    private var textFieldLabel: CharSequence = ""
    private var textFieldMessage: CharSequence = ""
    private var textFieldCounter: Int = 0
    private var textFieldPrependText: String  = ""
    private var textFieldAppendText: String = ""
    private var textFieldAppendDrawableIcon1: Drawable? = null
    private var textFieldAppendDrawableIcon2: Drawable? = null
    private var textFieldAppendUrlIcon1: String = ""
    private var textFieldAppendUrlIcon2: String = ""
    private var textFieldType: Int = 0
    private var textFieldPlaceholder: String =""

    private var collapsingTextHelper: Any? = null
    private var bounds: Rect? = null
    private var recalculateMethod: Method? = null
    private var iconContainerWidth: Int = 0
    private var isLabelStatic = false
    private var prependTextDrawable: Drawable? = null
    private var appendTextDrawable: Drawable? = null
    var isInputError: Boolean = false
    set(value) {
        if (field == value) return
        field = value
        setError(value)
    }

    init {
        View.inflate(context, R.layout.textfield2_layout,this)

        textInputLayout = findViewById(R.id.text_field_wrapper)
        editText = findViewById(R.id.text_field_input)
        iconContainer = findViewById(R.id.text_field_icon_container)
        icon1 = findViewById(R.id.text_field_icon_1)
        icon2 = findViewById(R.id.text_field_icon_2)
        labelText = findViewById(R.id.text_field_label)
        clearIconView = findViewById(R.id.text_field_icon_close)
        loaderView = findViewById(R.id.text_field_loader)

        editText.setSingleLine(true)
        editText.setTextColor(primaryColorStateList)
        textInputLayout.helperText = " "

        initializeAttr(attrs)
        configLabel()
        configPrefixSuffix()
        configIcon()

        if(textFieldCounter > 0) {
            setCounter(textFieldCounter)
        }

        if(!isEmpty(textFieldMessage)) {
            setMessage(textFieldMessage)
        }

        if(textFieldType != 0) {
            setInputType(textFieldType)
        }

        setLabelStatic(isLabelStatic)
        setPlaceholder(textFieldPlaceholder)
        textInputLayout.setHelperTextColor(secondaryColorStateList)
        editText.setDropDownBackgroundDrawable(ColorDrawable(Color.WHITE))
        textInputLayout.setBoxCornerRadii(8.toPx().toFloat(), 8.toPx().toFloat(), 8.toPx().toFloat(), 8.toPx().toFloat())
        textInputLayout.defaultHintTextColor = hintEmptyColorStateList
    }

    /**
     * Set counter to TextField
     * @param length Int that will be displayed
     */
    fun setCounter(length: Int) {
        textFieldCounter = length
        textInputLayout.isCounterEnabled = true
        textInputLayout.counterMaxLength = length
        editText.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(length))

        counterView = ((textInputLayout.getChildAt(1) as ViewGroup?)?.getChildAt(1)) as TextView?

        counterView?.gravity = Gravity.RIGHT
        counterView?.width = StaticLayout.getDesiredWidth("$length / $length", counterView?.getPaint()).toInt()
        counterView?.text = context.getString(R.string.textfield_counter, 0, length)
        counterView?.setTextColor(secondaryColorStateList)

        configHelper()
    }

    /**
     * Set message (helper) below TextField
     * @param text CharSequence that will be displayed
     */
    fun setMessage(text: CharSequence) {
        textInputLayout.isHelperTextEnabled = true
        val textView: TextView? = ((textInputLayout.getChildAt(1) as ViewGroup?)?.getChildAt(0) as ViewGroup?)?.getChildAt(0) as TextView?
        textView?.movementMethod = LinkMovementMethod.getInstance()
        textView?.highlightColor = Color.TRANSPARENT

        configHelper()

        if(isEmpty(text)) {
            textInputLayout.helperText = " "
        } else {
            textInputLayout.helperText = text
        }
    }

    /**
     * Set error state to TextField
     * @param boolean true to set error, false to set default state
     */
    private fun setError(isError: Boolean) {
        val textView: TextView? =
            ((textInputLayout.getChildAt(1) as ViewGroup?)?.getChildAt(0) as ViewGroup?)?.getChildAt(
                0
            ) as TextView?

        if (isError) {
            errorBorderTextColor?.let {
                textInputLayout.setBoxStrokeColorStateList(it)
            }

            val helperTextColor = errorBorderTextColor
            textView?.setTextColor(helperTextColor)

            slideDown(textView as View, 200)
        } else {
            if(!isEmpty(textFieldMessage)) {
                textInputLayout.helperText = textFieldMessage
            }

            standbyBorderColor?.let {
                textInputLayout.setBoxStrokeColorStateList(it)
            }

            val helperTextColor = secondaryColorStateList
            textView?.setTextColor(helperTextColor)
        }
    }

    /**
     * Set prefix text to TextField
     * @param prefix String for prepend text
     */
    fun prependText(prefix: String) {
        textFieldPrependText = prefix
    }

    /**
     * Set postfix/suffix text to TextField
     * @param suffix String for append text
     */
    fun appendText(suffix: String) {
        textFieldAppendText = suffix
    }


    /**
     * Return the text that TextField is displaying
     * @return editable value from TextField
     */
    fun getEditableValue(): Editable{
        return editText.text
    }

    /**
     * Set input type to TextField
     * @param type int value from InputType class
     * @see android.text.InputType
     */
    fun setInputType(type: Int) {
        if(type == InputType.TYPE_TEXT_VARIATION_PASSWORD) {
            editText.inputType = type or InputType.TYPE_CLASS_TEXT
            editText.typeface = Typeface.DEFAULT
            configPasswordInput()
        } else {
            editText.inputType = type
        }
    }

    /**
     * Return the first icon from TextField, useful for setOnClickListener
     * @return Return ImageView Class
     */
    fun getFirstIcon(): ImageView {
        return icon1
    }

    /**
     * Return the second icon from TextField, useful for setOnClickListener
     * @return Return ImageView Class
     */
    fun getSecondIcon(): ImageView {
        return icon2
    }

    /**
     * Set first custom icon displayed on TextField
     * @param drawable Drawable ID that will be loaded on TextField
     */
    fun setFirstIcon(drawable: Int) {
        icon1.visibility = View.VISIBLE
        icon1.setImageDrawable(AppCompatResources.getDrawable(context, drawable))
    }

    /**
     * Set first custom icon displayed on TextField
     * @param url URL String that will be loaded on TextField
     */
    fun setFirstIcon(url: String) {
        icon1.visibility = View.VISIBLE
        icon1.setImage(url, context.resources.getDimensionPixelSize(R.dimen.spacing_lvl2).toFloat())
    }

    /**
     * Set second custom icon displayed on TextField
     * @param drawable Drawable ID that will be loaded on TextField
     */
    fun setSecondIcon(drawable: Int) {
        icon2.visibility = View.VISIBLE
        icon2.setImageDrawable(AppCompatResources.getDrawable(context, drawable))
    }

    /**
     * Set second custom icon displayed on TextField
     * @param url URL String that will be loaded on TextField
     */
    fun setSecondIcon(url: String) {
        icon2.visibility = View.VISIBLE
        icon2.setImage(url, context.resources.getDimensionPixelSize(R.dimen.spacing_lvl2).toFloat())
    }


    fun setLabel(label: CharSequence) {
        textFieldLabel = label

        configLabel()
    }


    /**
     * Set TextField label to use or ignore floating label
     * @param staticLabelState Boolean value true will ignore floating label, value false will use floating label
     */
    fun setLabelStatic(staticLabelState: Boolean) {
        isLabelStatic = staticLabelState

        if(isLabelStatic) {
            textInputLayout.hint = ""
            labelText.visibility = View.VISIBLE
            labelText.text = textFieldLabel
            labelText.setTextColor(secondaryColorStateList)
            val layoutParams = textInputLayout.layoutParams as FrameLayout.LayoutParams
            layoutParams.setMargins(0, 4, 0, 0)
            textInputLayout.layoutParams = layoutParams
        } else {
            textInputLayout.hint = textFieldLabel
            labelText.visibility = View.GONE
            editText.hint = ""
        }
    }


    /**
     * Set placeholder text to TextField
     * @param text String that will be loaded as placeholder
     */
    fun setPlaceholder(text: String) {
        if(isLabelStatic) {
            editText.hint = text
        } else {
            editText.hint = ""
        }
    }

    fun setLabelStaticBackground(color: Int) {
        labelText.setBackgroundColor(color)
    }

    override fun setEnabled(enabled: Boolean) {
        if (!enabled) {
            prependTextDrawable?.let {
                (it as TextDrawable).paint.color = textPrePostColor[1]
            }
            appendTextDrawable?.let {
                (it as TextDrawable).paint.color = textPrePostColor[1]
            }
        } else {
            prependTextDrawable?.let {
                (it as TextDrawable).paint.color = textPrePostColor[0]
            }
            appendTextDrawable?.let {
                (it as TextDrawable).paint.color = textPrePostColor[0]
            }
        }

        textInputLayout.isEnabled = enabled
        editText.isEnabled = enabled
        labelText.isEnabled = enabled
        clearIconView.isEnabled = enabled

        clearIconView.alpha = if (enabled) 1f else 0.44f

        super.setEnabled(enabled)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        // 46px (edittext height for 1 line) - 24px (icon height) / 2 + 4px (gap between edittext and helper text) for centering the icons vertically
        (iconContainer.layoutParams as FrameLayout.LayoutParams).setMargins(0, 0, 0, (textInputLayout.getChildAt(1).measuredHeight) + (46.toPx() - 24.toPx()) / 2 + 4.toPx())
    }

    private fun initializeAttr(attributeSet: AttributeSet) {
        val attributeArray = context.obtainStyledAttributes(attributeSet, R.styleable.TextFieldUnify)

        textFieldLabel = attributeArray.getString(R.styleable.TextFieldUnify_unify_text_field_label) ?: ""
        textFieldMessage = attributeArray.getString(R.styleable.TextFieldUnify_unify_text_field_message) ?: ""
        textFieldCounter = attributeArray.getInt(R.styleable.TextFieldUnify_unify_text_field_counter, 0)
        textFieldPrependText = attributeArray.getString(R.styleable.TextFieldUnify_unify_text_field_prepend_text) ?: ""
        textFieldAppendText = attributeArray.getString(R.styleable.TextFieldUnify_unify_text_field_append_text) ?: ""
        try {
            var referencesRes = attributeArray.getResourceId(R.styleable.TextFieldUnify_unify_text_field_append_drawable_icon_1,-1)
            textFieldAppendDrawableIcon1 = ContextCompat.getDrawable(context,referencesRes)
        } catch(e: Exception){
        }
        try{
            var referencesRes = attributeArray.getResourceId(R.styleable.TextFieldUnify_unify_text_field_append_drawable_icon_2,-1)
            textFieldAppendDrawableIcon2 = ContextCompat.getDrawable(context,referencesRes)
        } catch(e: Exception){
        }
        textFieldAppendUrlIcon1 = attributeArray.getString(R.styleable.TextFieldUnify_unify_text_field_append_url_icon_1) ?: ""
        textFieldAppendUrlIcon2 = attributeArray.getString(R.styleable.TextFieldUnify_unify_text_field_append_url_icon_2) ?: ""
        textFieldType = attributeArray.getInt(R.styleable.TextFieldUnify_unify_text_field_input_type, 0)
        isLabelStatic = attributeArray.getBoolean(R.styleable.TextFieldUnify_unify_text_field_label_static, false)
        textFieldPlaceholder =  attributeArray.getString(R.styleable.TextFieldUnify_unify_text_field_placeholder) ?: ""
        isClearable =  attributeArray.getBoolean(R.styleable.TextFieldUnify_unify_text_field_clearable, false)

        attributeArray.recycle()
    }

    private fun configLabel(){
        textInputLayout.hint = textFieldLabel

        editText.setOnFocusChangeListener { view, hasFocus ->
            if(hasFocus) {
                editText.setSelection(editText.text.length)

                Handler().postDelayed({
                    textInputLayout.defaultHintTextColor = secondaryColorStateList
                }, 10)
            } else {
                if (isEmpty(editText.editableText)) {
                    Handler().postDelayed({
                        textInputLayout.defaultHintTextColor = hintEmptyColorStateList
                    }, 10)
                } else {
                    Handler().postDelayed({
                        textInputLayout.defaultHintTextColor = secondaryColorStateList
                    }, 10)
                }
            }

            addOnFocusChangeListener?.invoke(view, hasFocus)
        }

        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if(!isEmpty(p0)) {
                    textInputLayout.defaultHintTextColor = secondaryColorStateList
                }

                if (textFieldCounter > 0) {
                    counterView?.text = context.getString(R.string.textfield_counter, p0?.length, textFieldCounter)
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        textInputLayout.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            adjustBounds()
        }

        try {
            val field = textInputLayout.javaClass.getDeclaredField("collapsingTextHelper")
            field.isAccessible = true
            collapsingTextHelper = field.get(textInputLayout)

            val boundsField = collapsingTextHelper?.javaClass?.getDeclaredField("collapsedBounds")
            boundsField?.isAccessible = true
            bounds = boundsField?.get(collapsingTextHelper) as Rect

            recalculateMethod = collapsingTextHelper?.javaClass?.getDeclaredMethod("recalculate")
        } catch (e: Exception) {
            collapsingTextHelper = null
            bounds = null
            recalculateMethod = null
            e.printStackTrace()
        }
    }

    private fun configPrefixSuffix() {
        iconContainer.post {
            iconContainerWidth = iconContainer.width
            prependTextDrawable = TextDrawable(textFieldPrependText, "left")
            appendTextDrawable = TextDrawable(textFieldAppendText, "right")
            editText.setCompoundDrawables(
                prependTextDrawable,
                null,
                appendTextDrawable,
                null
            )
        }
    }

    private fun configIcon() {
        if(textFieldAppendDrawableIcon1 != null) {
            icon1.visibility = View.VISIBLE
            icon1.setImageDrawable(textFieldAppendDrawableIcon1)
        }

        if(textFieldAppendDrawableIcon2 != null) {
            icon2.visibility = View.VISIBLE
            icon2.setImageDrawable(textFieldAppendDrawableIcon2)
        }

        if(!isEmpty(textFieldAppendUrlIcon1)) {
            setFirstIcon(textFieldAppendUrlIcon1)
        }

        if(!isEmpty(textFieldAppendUrlIcon2)) {
            setSecondIcon(textFieldAppendUrlIcon2)
        }
    }

    private fun configPasswordInput() {
        var toggleVisibility = false
        val hiddenToShown = AnimatedVectorDrawableCompat.create(context, R.drawable.unify_password_hide)
        val shownToHidden = AnimatedVectorDrawableCompat.create(context, R.drawable.unify_password_show)
        icon1.visibility = View.VISIBLE
        icon1.setImageDrawable(hiddenToShown)

        icon1.setOnClickListener {
            val drawable = if (toggleVisibility) shownToHidden else hiddenToShown
            icon1.setImageDrawable(drawable)
            drawable?.start()
            toggleVisibility = !toggleVisibility

            if (toggleVisibility) {
                editText.transformationMethod = null
            } else {
                editText.transformationMethod = PasswordTransformationMethod()
            }
            editText.setSelection(editText.text.length)
        }
    }

    private fun configHelper() {
        val textView: TextView? = ((textInputLayout.getChildAt(1) as ViewGroup?)?.getChildAt(0) as ViewGroup?)?.getChildAt(0) as TextView?

        textView?.post {
            textView.width = textInputLayout.width
            textView.setBodyText(3)

            val helperTextColor = if(isInputError) errorBorderTextColor else secondaryColorStateList
            textView.setTextColor(helperTextColor)
        }

        counterView?.post {
            counterView?.let {
                textView?.width = textInputLayout.width - it.width
            }
        }
    }

    private fun adjustBounds() {
        try {
            bounds?.left = editText.left + editText.paddingLeft
            recalculateMethod?.invoke(collapsingTextHelper)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private inner class TextDrawable(text: String, type: String) : Drawable() {
        val paint: Paint = Paint()
        private var fontType: Typeface = Typeface.createFromAsset(context.assets, "NunitoSansExtraBold.ttf")
        private val text: String = text

        init {
            if (!isEnabled) {
                paint.color = textPrePostColor[1]
            } else {
                paint.color = textPrePostColor[0]
            }

            paint.textSize = resources.getDimension(R.dimen.textfield2_text_size)
            paint.typeface = fontType
            paint.isAntiAlias = true
            paint.textAlign = Paint.Align.LEFT
            if(type === "left" && !isEmpty(text)) {
                setBounds(0, 0, paint.measureText(text).toInt() + 16, 24.toDp())
            }
            else if(type === "right") {
                setBounds(-(iconContainerWidth - 10.toPx()), 0, paint.measureText(text).toInt(), 24.toDp())
            }
        }

        override fun draw(canvas: Canvas) {
            canvas.drawText(text, 0f, 18f, paint)
        }

        override fun setAlpha(alpha: Int) {
            paint.alpha = alpha
        }

        override fun setColorFilter(cf: ColorFilter?) {
            paint.colorFilter = cf
        }

        override fun getOpacity(): Int {
            return PixelFormat.TRANSLUCENT
        }
    }
}