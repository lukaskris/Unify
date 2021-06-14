package com.forestry.plantation.core.extension

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import es.dmoral.toasty.Toasty


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
			decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
			statusBarColor = Color.TRANSPARENT
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