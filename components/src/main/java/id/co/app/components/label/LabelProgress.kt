package id.co.app.components.label

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import id.co.app.components.R
/**
 * Created by Lukas Kristianto on 7/15/2021 00:12.
 * App Sinarmas
 * lukas_kristianto@app.co.id
 */
class LabelProgress : FrameLayout {

    private var tvLabelProgress: TextView? = null
    private var progressBar: ProgressBar? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet) {
        View.inflate(context, R.layout.label_layout_progress, this)
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.LabelProgress, 0, 0)
        tvLabelProgress = findViewById(R.id.txt_label_progressbar)
        progressBar = findViewById(R.id.progressbar_label)

        try {
            val labelText = typedArray.getString(R.styleable.LabelProgress_labelProgressTitle)
            val progressNumber = typedArray.getInt(R.styleable.LabelProgress_labelProgress, 0)
            setLabel(labelText, progressNumber)
        } finally {
            typedArray.recycle()
        }
    }

    fun setLabel(label: String?, progress: Int) {
        tvLabelProgress!!.text = label
        progressBar!!.progress = progress
    }
}
