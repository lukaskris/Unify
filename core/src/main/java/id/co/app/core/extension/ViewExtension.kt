package id.co.app.core.extension

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import es.dmoral.toasty.Toasty
import id.co.app.components.text.TextFieldUnify
import id.co.app.core.R
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*


/**
 * Created by Lukas Kristianto on 4/26/2021.
 * App Sinarmas
 * lukas_kristianto@app.co.id
 */
fun Activity.makeStatusBarTransparent() {
	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
		window.apply {
			clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
			addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
				decorView.systemUiVisibility =
					View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
			} else {
				decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
			}
			statusBarColor = Color.TRANSPARENT
		}
	}
}

fun View.setMarginTop(marginTop: Int) {
	val menuLayoutParams = this.layoutParams as ViewGroup.MarginLayoutParams
	menuLayoutParams.setMargins(0, marginTop, 0, 0)
	this.layoutParams = menuLayoutParams
}

fun Activity.makeStatusBarNotTransparent() {
	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
		window.apply {
			decorView.systemUiVisibility = 0
			statusBarColor = ContextCompat.getColor(this@makeStatusBarNotTransparent, R.color.color_primary_dark)
		}
	}
}

fun Context.showToast(message: String, duration: Int = Toast.LENGTH_LONG) {
	Toasty.success(this, message, duration).show()
}

fun Context.showInformationToast(message: String, duration: Int = Toast.LENGTH_LONG) {
	Toasty.info(this, message, duration).show()
}

fun Context.showErrorToast(message: String, duration: Int = Toast.LENGTH_LONG) {
	Toasty.error(this, message, duration).show()
}

fun Activity.closeKeyboard(view: View) {
	(getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
		view.windowToken,
		0
	)
}

fun Context.closeKeyboard(view: View) {
	val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
	inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun View.show() {
	visibility = View.VISIBLE
}

fun View.gone() {
	visibility = View.GONE
}

fun Fragment.getNavigationResult(key: String = "result") =
	findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>(key)

fun Fragment.setNavigationResult(result: String, key: String = "result") {
	findNavController().previousBackStackEntry?.savedStateHandle?.set(key, result)
}

fun View?.fitSystemWindowsAndAdjustResize() = this?.let { view ->
	ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
		view.fitsSystemWindows = true
		val bottom = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom

		WindowInsetsCompat
			.Builder()
			.setInsets(
				WindowInsetsCompat.Type.systemBars(),
				Insets.of(0, 0, 0, bottom)
			)
			.build()
			.apply {
				ViewCompat.onApplyWindowInsets(v, this)
			}
	}
}

fun TextFieldUnify.addDoneButton(activity: Activity){
	editText.setOnKeyListener { _, keyCode, event ->
		if (event.action == KeyEvent.ACTION_DOWN) {
			when (keyCode) {
				KeyEvent.KEYCODE_DPAD_CENTER, KeyEvent.KEYCODE_ENTER -> {
					activity.closeKeyboard(this)
					return@setOnKeyListener true
				}
				else -> {
					return@setOnKeyListener false
				}
			}
		} else {
			return@setOnKeyListener false
		}
	}
}

fun TextFieldUnify.addDoneButton(){
	editText.setOnKeyListener { _, keyCode, event ->
		if (event.action == KeyEvent.ACTION_DOWN) {
			when (keyCode) {
				KeyEvent.KEYCODE_DPAD_CENTER, KeyEvent.KEYCODE_ENTER -> {
					context?.closeKeyboard(this)
					return@setOnKeyListener true
				}
				else -> {
					return@setOnKeyListener false
				}
			}
		} else {
			return@setOnKeyListener false
		}
	}
}

fun TextFieldUnify.addThousandSeparator(){
	editText.addTextChangedListener(object : TextWatcher {
		val dfs = DecimalFormatSymbols.getInstance(Locale.GERMANY)
		val dec = DecimalFormat("#,###", dfs)
		override fun afterTextChanged(p0: Editable?) {
			editText.removeTextChangedListener(this)

			try {
				val string = editText.text.toString()
				if (!TextUtils.isEmpty(string)) {
					val textWC = string.replace(".", "")
					val number = textWC.toDouble()
					editText.setText(dec.format(number))
					editText.setSelection(dec.format(number).length)
				}
			} catch (nfe: NumberFormatException) {
				nfe.printStackTrace()
			}

			editText.addTextChangedListener(this)
		}

		override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
		}

		override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
		}
	})
}