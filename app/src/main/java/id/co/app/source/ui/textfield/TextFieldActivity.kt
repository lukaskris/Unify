package id.co.app.source.ui.textfield

import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.InputType
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import id.co.app.components.bottomsheet.BottomSheetUnify
import id.co.app.components.button.UnifyButton
import id.co.app.components.text.TextAreaUnify
import id.co.app.components.text.TextFieldUnify
import id.co.app.components.utils.HtmlLinkHelper
import id.co.app.source.R
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

class TextFieldActivity : AppCompatActivity() {
    private var fruits = listOf<String>("apple", "banana", "orange")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_textfield)
        val htmlString = HtmlLinkHelper(this, "Support <b>HTML</b> by using <i>HtmlLinkHelper</i> class <a href='https://www.facebook.com'>facebook</a> <a href='https://www.google.com'>google</a>")
        findViewById<TextFieldUnify>(R.id.input_email).let { input_email ->
            input_email.setLabelStatic(true)
            input_email.setLabelStaticBackground(ContextCompat.getColor(this, R.color.Unify_B400))
            htmlString.urlList[0].setOnClickListener {
                Toast.makeText(this, "Link url clicked",
                    Toast.LENGTH_LONG).show()
            }
            input_email.setMessage(htmlString.spannedString!!)
    //        input_email.setCounter(15)
            input_email.setPlaceholder("Input Your Name")

            // autocomplete
            var adapter = ArrayAdapter<String>(this, android.R.layout.select_dialog_item, fruits)
            input_email.editText.threshold = 1
            input_email.editText.setAdapter(adapter)
            input_email.editText.addTextChangedListener((object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if(p0 != null) {
                        if(p0.length < 3) {
                            input_email.isInputError = true
                            input_email.setMessage("Minimum 3 karakter")
                        } else {
                            input_email.isInputError = false
                            input_email.setMessage(htmlString.spannedString!!)
                        }
                    }
                }
            }))
            input_email.setFirstIcon(R.drawable.png_sample)
            input_email.setSecondIcon(R.drawable.cricket)
            input_email.isClearable = true
            input_email.isLoading = false
        }

        findViewById<TextFieldUnify>(R.id.input_disabled).isEnabled = false
        findViewById<TextFieldUnify>(R.id.input_disabled).editText.setText("Test")



        findViewById<TextFieldUnify>(R.id.input_name).getFirstIcon().setOnClickListener {
            var bottomSheet = BottomSheetUnify()
            var childHalf = View.inflate(this,R.layout.bottomsheet_sample_half,null)
            bottomSheet.setTitle("Title")
            bottomSheet.setFullPage(false)
            bottomSheet.setChild(childHalf)
            bottomSheet.clearAction()
            bottomSheet.show(supportFragmentManager,"BottomSheet Show")
        }

        findViewById<TextAreaUnify>(R.id.input_price_area).prependText("Prepent 1")
        findViewById<TextFieldUnify>(R.id.input_age).editText.text = Editable.Factory.getInstance().newEditable("25")
        findViewById<TextFieldUnify>(R.id.input_age).addOnFocusChangeListener = { _, hasFocus ->
            if(hasFocus) {
                println("input_age hasFocus")
            }
        }

        findViewById<TextFieldUnify>(R.id.input_password).setCounter(10)

        findViewById<TextFieldUnify>(R.id.input_price).setInputType(InputType.TYPE_CLASS_NUMBER)
        Handler().postDelayed({
            findViewById<TextFieldUnify>(R.id.input_email).isLoading = true
        }, 5000)
        findViewById<TextFieldUnify>(R.id.input_price).setFirstIcon(R.drawable.sample_drawable)
        findViewById<TextFieldUnify>(R.id.input_price).setSecondIcon(R.drawable.sample_drawable2)
        findViewById<TextFieldUnify>(R.id.input_price).getFirstIcon().setOnClickListener {
            Toast.makeText(this, "First Icon Clicked", Toast.LENGTH_SHORT).show()
        }
        findViewById<TextFieldUnify>(R.id.input_price).getSecondIcon().setOnClickListener {
            Toast.makeText(this, "Second Icon Clicked", Toast.LENGTH_SHORT).show()
        }
        findViewById<TextFieldUnify>(R.id.input_price).prependText("$")
        findViewById<TextFieldUnify>(R.id.input_price).prependText("$")
        findViewById<TextFieldUnify>(R.id.input_price).appendText("USD")
//        thousand separator from https://stackoverflow.com/a/38802287/8624588
        findViewById<TextFieldUnify>(R.id.input_price).editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                findViewById<TextFieldUnify>(R.id.input_price).editText.removeTextChangedListener(this)

                try {
                    var originalString = p0.toString()

                    val longval: Long?
                    if (originalString.contains(",")) {
                        originalString = originalString.replace(",".toRegex(), "")
                    }
                    longval = java.lang.Long.parseLong(originalString)

                    val formatter = NumberFormat.getInstance(Locale.US) as DecimalFormat
                    formatter.applyPattern("#,###,###,###")
                    val formattedString = formatter.format(longval)

                    //setting text after format to EditText
                    findViewById<TextFieldUnify>(R.id.input_price).editText.setText(formattedString)
                    findViewById<TextFieldUnify>(R.id.input_price).editText.setSelection(findViewById<TextFieldUnify>(R.id.input_price).editText.text.length)
                }
                catch (nfe: NumberFormatException) {
                    nfe.printStackTrace()
                }

                findViewById<TextFieldUnify>(R.id.input_price).editText.addTextChangedListener(this)
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        findViewById<UnifyButton>(R.id.submit).setOnClickListener {
            var emailValue = findViewById<TextFieldUnify>(R.id.input_email).getEditableValue().toString()
            var passwordValue = findViewById<TextFieldUnify>(R.id.input_password).getEditableValue().toString()
            var nameValue = findViewById<TextFieldUnify>(R.id.input_name).getEditableValue().toString()

            if(TextUtils.isEmpty(emailValue)) {
                findViewById<TextFieldUnify>(R.id.input_email).setMessage("E-mail must be filled in")
                findViewById<TextFieldUnify>(R.id.input_email).isInputError = true
            } else {
                if(findViewById<TextFieldUnify>(R.id.input_email).editText.text.length < 3) {
                    findViewById<TextFieldUnify>(R.id.input_email).isInputError = true
                    findViewById<TextFieldUnify>(R.id.input_email).setMessage("Minimum 3 karakter")
                } else {
                    findViewById<TextFieldUnify>(R.id.input_email).setMessage(htmlString.spannedString!!)
                    findViewById<TextFieldUnify>(R.id.input_email).isInputError = false
                }
            }

            if(TextUtils.isEmpty(passwordValue)) {
                findViewById<TextFieldUnify>(R.id.input_password).setMessage("Password must be filled in")
                findViewById<TextFieldUnify>(R.id.input_password).isInputError = true
            } else {
                findViewById<TextFieldUnify>(R.id.input_password).setMessage("")
                findViewById<TextFieldUnify>(R.id.input_password).isInputError = false
            }

            if(TextUtils.isEmpty(nameValue)) {
                findViewById<TextFieldUnify>(R.id.input_name).setMessage("Name must be filled in")
                findViewById<TextFieldUnify>(R.id.input_name).isInputError = true
            } else {
                findViewById<TextFieldUnify>(R.id.input_name).isInputError = false
            }
        }
    }
}