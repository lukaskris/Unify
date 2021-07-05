package id.co.app.components.text

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import id.co.app.components.R

/**
 * Created by Lukas Kristianto on 7/3/2021.
 * App Sinarmas
 * lukas_kristianto@app.co.id
 */
class TextAreaUnify(context: Context, attrs: AttributeSet) : TextFieldUnify(context, attrs) {

    var maxLine = 3
        set(value) {
            field = value
            this.editText.maxLines = value
        }

    var minLine = 1
        set(value) {
            field = value
            this.editText.minLines = value

            editText.gravity = if (field == 1) Gravity.CENTER_VERTICAL else Gravity.TOP
        }

    init {
        editText.setSingleLine(false)
        
        initializeAttr(attrs)
    }

    fun initializeAttr(attrs: AttributeSet) {
        val attributeArray = context.obtainStyledAttributes(attrs, R.styleable.TextFieldUnify)
        maxLine = attributeArray.getInteger(R.styleable.TextFieldUnify_unify_text_field_maxline, 3)

        attributeArray.recycle()
    }
}