package id.co.app.core.binding

import android.text.Editable
import android.text.TextWatcher
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import id.co.app.components.text.TextFieldUnify


object TextFieldUnifyBinding {
    @JvmStatic
    @BindingAdapter("android:textAttrChanged")
    fun setListener(textFieldUnify: TextFieldUnify, textAttrChanged: InverseBindingListener?) {
        textAttrChanged?.let {
            textFieldUnify.editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    charSequence: CharSequence,
                    i: Int,
                    i1: Int,
                    i2: Int
                ) {
                }

                override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
                override fun afterTextChanged(editable: Editable) {
                    textAttrChanged.onChange()
                }
            })
        }
    }

    @JvmStatic
    @BindingAdapter("android:text")
    fun bindStringInText(textFieldUnify: TextFieldUnify, value: String?) {
        if(value?.isNotEmpty() == true && value != textFieldUnify.editText.text.toString()) {
            textFieldUnify.editText.setText(value)

            // Set the cursor to the end of the text
            textFieldUnify.editText.setSelection(textFieldUnify.editText.text.length)
        }
    }

    @JvmStatic
    @InverseBindingAdapter(attribute = "android:text")
    fun getStringFromBinding(textFieldUnify: TextFieldUnify): String {
        return textFieldUnify.editText.text.toString()
    }
}