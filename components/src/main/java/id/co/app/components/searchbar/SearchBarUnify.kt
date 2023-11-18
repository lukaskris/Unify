package id.co.app.components.searchbar

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.content.ContextCompat
import id.co.app.components.R
import id.co.app.components.image.ImageUnify
import id.co.app.components.utils.setImage
import id.co.app.components.utils.toPx

class SearchBarUnify: FrameLayout {
    var searchBarRoot: LinearLayout
    var searchBarTextField: EditText
    var searchBarIcon: ImageButton
    var searchBarPrefixIcon: ImageView
    var searchBarIconCollectionWrapper: LinearLayout

    /**
     * Show or hide the clear icon
     */
    var isClearable: Boolean = true
        set(value) {
            field = value

            setIconListener()

            if (!showIcon && !isClearable) {
                searchBarIcon.visibility = View.GONE
            }
        }

    /**
     * Set placeholder text to SearchBar
     */
    var searchBarPlaceholder: String = ""
        set(value) {
            field = value

            searchBarTextField.hint = value
        }

    /**
     * Place drawable resource to postfix icon
     */
    var iconDrawable: Drawable? = null
        set(value) {
            field = value

            if(iconDrawable != null) {
                searchBarIcon.setImageDrawable(iconDrawable)
            }
        }

    /**
     * Place url to postfix icon
     */
    var iconUrl: String? = null
        set(value) {
            field = value

            field?.let{
                searchBarIcon.setImage(it, 8f)
            }
        }

    /**
     * Bind listener to postfix icon
     */
    var iconListener: () -> Unit = {}
        set(value) {
            field = value

            setIconListener()
        }

    /**
     * Bind listener to clear icon
     */
    var clearListener: () -> Unit = {}
        set(value) {
            field = value

            setIconListener()
        }

    /**
     * Show or hide postfix icon
     */
    var showIcon: Boolean = true
        set(value) {
            field = value

            if(!showIcon) {
                searchBarIcon.setImageResource(R.drawable.unify_clear_ic)
                searchBarIcon.visibility = View.VISIBLE
                searchBarIcon.clearAnimation()
                searchBarIcon.post {
                    searchBarIcon.animate().scaleX(0f).scaleY(0f).setDuration(0).start()
                }
            }

            if (!showIcon && !isClearable) {
                searchBarIcon.visibility = View.GONE
            }
        }

    /**
     * Set SearchBar component to be enabled or disabled
     */
    var isSearchEnabled: Boolean = true
        set(value) {
            field = value

            searchBarTextField.isEnabled = value
            searchBarIcon.isEnabled = value

            if(!value) {
                searchBarPrefixIcon.alpha = 0.3f
                searchBarIcon.alpha = 0.3f
                searchBarTextField.alpha = 0.5f

                if(showIcon) {
                    if (iconDrawable != null) {
                        searchBarIcon.setImageDrawable(iconDrawable)
                    }

                    iconUrl?.let {
                        searchBarIcon.setImage(it, 8f)
                    }
                }
            } else {
                searchBarPrefixIcon.alpha = 1f
                searchBarIcon.alpha = 1f
                searchBarTextField.alpha = 1f

                if(isClearable && searchBarTextField.text.isNotEmpty()) {
                    searchBarIcon.setImageResource(R.drawable.unify_clear_ic)
                }
            }
        }

    var staticIconCollection: ArrayList<SearchBarItemUnify>? = null
        set(value) {
            field = value
            searchBarIconCollectionWrapper.removeAllViews()

            val iconSize = 24.toPx()
            val paddingSize = 4.toPx()

            field?.forEach {item ->
                val newIcon = ImageUnify(context)
                val newLayoutParams = LinearLayout.LayoutParams(iconSize, iconSize)
                newLayoutParams.gravity = Gravity.CENTER

                newIcon.layoutParams = newLayoutParams
                newIcon.setImageDrawable(item.asset)
                newIcon.setPadding(paddingSize,paddingSize,paddingSize,paddingSize)
                newIcon.scaleType = ImageView.ScaleType.CENTER_CROP
                newIcon.isClickable = true

                newIcon.setOnClickListener {
                    item.listener()
                }
                item.iconRef = newIcon

                searchBarIconCollectionWrapper.addView(newIcon)
            }
        }

    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        initializeAttr(context, attributeSet)
    }
    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr) {
        initializeAttr(context, attributeSet)
    }

    init {
        View.inflate(context, R.layout.unify_searchbar_layout,this)

        searchBarRoot = findViewById(R.id.search_bar_root)
        searchBarTextField = findViewById(R.id.searchbar_textfield)
        searchBarIcon = findViewById(R.id.searchbar_icon)
        searchBarPrefixIcon = findViewById(R.id.searchbar_prefix_icon)
        searchBarIconCollectionWrapper = findViewById(R.id.searchbar_custom_icon_wrapper)

        searchBarIcon.visibility = View.VISIBLE

        textListener()
    }

    private fun initializeAttr(context: Context, attributeSet: AttributeSet) {
        val attributeArray = context.obtainStyledAttributes(attributeSet, R.styleable.SearchBarUnify)

        isClearable = attributeArray.getBoolean(R.styleable.SearchBarUnify_search_bar_clearable, true)
        searchBarPlaceholder = attributeArray.getString(R.styleable.SearchBarUnify_search_bar_placeholder) ?: ""
        iconUrl = attributeArray.getString(R.styleable.SearchBarUnify_search_bar_url_icon) ?: null
        try {
            val referencesRes = attributeArray.getResourceId(R.styleable.SearchBarUnify_search_bar_drawable_icon, R.drawable.iconunify_camera)
            iconDrawable = ContextCompat.getDrawable(context, referencesRes)
        } catch(e: Exception){
        }
        showIcon = attributeArray.getBoolean(R.styleable.SearchBarUnify_search_bar_show_icon, true)
        isSearchEnabled = attributeArray.getBoolean(R.styleable.SearchBarUnify_search_bar_enable, true)

        attributeArray.recycle()
    }

    private fun textListener() {
        setIconListener()

        val scale = ObjectAnimator.ofPropertyValuesHolder(
            searchBarIcon,
            PropertyValuesHolder.ofFloat("scaleX", 0f),
            PropertyValuesHolder.ofFloat("scaleY", 0f)
        )
        scale.duration = 150
        scale.repeatCount = 1
        scale.repeatMode = ObjectAnimator.REVERSE

        searchBarTextField.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(showIcon) {
                    if (isClearable) {
                        if (TextUtils.isEmpty(p0)) {
                            searchBarIcon.clearAnimation()
                            scale.start()
                            scale.addListener(
                                object : AnimatorListenerAdapter() {
                                    override fun onAnimationEnd(animation: Animator) {
                                        super.onAnimationEnd(animation)
                                        searchBarIcon.setImageResource(R.drawable.unify_clear_ic)
                                    }
                                })
                        } else {
                            searchBarIcon.setImageResource(R.drawable.unify_clear_ic)
                        }
                    } else {
                        if (iconDrawable != null) {
                            searchBarIcon.setImageDrawable(iconDrawable)
                        }

                        iconUrl?.let {
                            searchBarIcon.setImage(it, 8f)
                        }
                    }
                }
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(showIcon) {
                    if (isClearable) {
                        if (TextUtils.isEmpty(p0)) {
                            searchBarIcon.clearAnimation()
                            scale.start()
                            scale.addListener(
                                object : AnimatorListenerAdapter() {
                                    override fun onAnimationEnd(animation: Animator) {
                                        super.onAnimationEnd(animation)
                                        if (iconDrawable != null) {
                                            searchBarIcon.setImageDrawable(iconDrawable)
                                        }

                                        iconUrl?.let {
                                            searchBarIcon.setImage(it, 8f)
                                        }
                                    }
                                })
                        }
                    } else {
                        if (iconDrawable != null) {
                            searchBarIcon.setImageDrawable(iconDrawable)
                        }

                        iconUrl?.let {
                            searchBarIcon.setImage(it, 8f)
                        }
                    }

                } else {
                    if(TextUtils.isEmpty(p0)) {
                        searchBarIcon.clearAnimation()
                        searchBarIcon.animate().scaleX(0f).scaleY(0f).setDuration(200).start()
                    } else {
                        searchBarIcon.clearAnimation()
                        searchBarIcon.animate().scaleX(1f).scaleY(1f).setDuration(200).start()
                    }
                }
            }
        })
    }

    private fun setIconListener() {
        if(isClearable) {
            searchBarIcon.setOnClickListener {
                if (searchBarTextField.text.isEmpty()) {
                    iconListener()
                } else {
                    clearListener()
                    searchBarTextField.text.clear()
                }
            }
        } else {
            searchBarIcon.setOnClickListener {
                iconListener()
            }
        }
    }

    /**
     * clear focus on search bar and hide active keyboard
     */
    override fun clearFocus() {
        super.clearFocus()

        searchBarTextField.clearFocus()
        val input = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        input.hideSoftInputFromWindow(searchBarTextField.windowToken, 0)
    }
}