package id.co.app.source.core.customview

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.widget.ProgressBar


/**
 * Created by Lukas Kristianto on 5/25/2021.
 * App Sinarmas
 * lukas_kristianto@app.co.id
 */

class MultipleProgressBar : ProgressBar {
	private var paint: Paint = Paint()
	var primaryProgress = 0
	var maxValue = 0

	constructor(context: Context?) : super(context) {
		init()
	}

	constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
		init()
	}

	@Synchronized
	override fun setMax(max: Int) {
		maxValue = max
		super.setMax(max)
	}

	@Synchronized
	override fun setProgress(progress: Int) {
		var progress = progress
		if (progress > maxValue) {
			progress = maxValue
		}
		primaryProgress = progress
		super.setProgress(progress)
	}

	@Synchronized
	override fun setSecondaryProgress(secondaryProgress: Int) {
		var secondaryProgress = secondaryProgress
		if (primaryProgress + secondaryProgress > maxValue) {
			secondaryProgress = maxValue - primaryProgress
		}
		super.setSecondaryProgress(primaryProgress + secondaryProgress)
	}

	private fun init() {
		paint.color = Color.BLACK
		primaryProgress = 0
		maxValue = 100
	}
}