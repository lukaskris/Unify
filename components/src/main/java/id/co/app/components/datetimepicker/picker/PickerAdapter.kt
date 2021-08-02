package id.co.app.components.datetimepicker.picker

import android.content.Context
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import id.co.app.components.utils.fontTypeLoader
import id.co.app.components.utils.setBodyText
import id.co.app.components.utils.toPx
import kotlin.math.ceil

class PickerAdapter(
    private val mDataset: MutableList<String>,
    private var itemToShow: Int,
    private var bodyFontLevel: Int,
    private var infiniteMode: Boolean,
    private var textAlign: Int,
    private var isPlaceholder: Boolean,
    var selectableRangeItems: IntRange?,
    var disabledItems: MutableList<Int>,
    var pickerDecorator: PickerDecorator
) :
    RecyclerView.Adapter<PickerAdapter.PickerViewHolder>() {
    var centerActiveIndex: Int = 0
    lateinit var mRecyclerView: RecyclerView
    var onItemClickListener: ((position: Int) -> Unit)? = null
    lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PickerViewHolder {
        context = parent.context
        val mTextView = TextView(context)
        mTextView.setPadding(10.toPx(), 10.toPx(), 10.toPx(), 10.toPx())
        when (textAlign) {
            ALIGN_LEFT -> mTextView.gravity = Gravity.CENTER_VERTICAL
            ALIGN_CENTER -> mTextView.gravity = Gravity.CENTER
            ALIGN_RIGHT -> mTextView.gravity = Gravity.RIGHT or Gravity.CENTER_VERTICAL
        }

        if (::mRecyclerView.isInitialized) {
            mTextView.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                mRecyclerView.measuredHeight / (itemToShow + 1)
            )
        }

        return PickerViewHolder(mTextView)
    }

    override fun getItemCount(): Int {
        if (infiniteMode) return Int.MAX_VALUE
        return mDataset.size
    }

    override fun onBindViewHolder(holder: PickerViewHolder, position: Int) {
        holder.mTextView.apply {
            text = mDataset[position % mDataset.size]
            setBodyText(bodyFontLevel, false)
            setTextSize(TypedValue.COMPLEX_UNIT_SP, pickerDecorator.fontSize.second)
            if (pickerDecorator.fontBold.second) {
                typeface = fontTypeLoader(this.context, true)
            } else {
                typeface = fontTypeLoader(this.context, false)
            }
            setOnClickListener {
                onItemClickListener?.invoke(position)
            }

            setTextColor(pickerDecorator.fontColor.second)
        }

        if (!infiniteMode) {
            when (position) {
                0 -> {
                    if (::mRecyclerView.isInitialized && (mDataset[position % mDataset.size].isEmpty() || isPlaceholder)) {
                        holder.mTextView.layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            mRecyclerView.measuredHeight / (itemToShow + 1) - 5
                        )
                    }
                }

                mDataset.size - 1 -> {
                    if (::mRecyclerView.isInitialized && (mDataset[position % mDataset.size].isEmpty() || isPlaceholder)) {
                        holder.mTextView.layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            mRecyclerView.measuredHeight / (itemToShow + 1) - 5
                        )
                    }
                }

                else -> {
                    if (::mRecyclerView.isInitialized) {
                        holder.mTextView.layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            mRecyclerView.measuredHeight / (itemToShow + 1)
                        )
                    }
                }
            }

            if (isPlaceholder) {
                when {
                    position % mDataset.size >= mDataset.size - (itemToShow + 1) / 2 -> {
                        holder.mTextView.apply {
                            setTextColor(pickerDecorator.disabledColor)
                            setOnClickListener { }
                        }
                    }

                    position % mDataset.size < (itemToShow + 1) / 2 -> {
                        holder.mTextView.apply {
                            setTextColor(pickerDecorator.disabledColor)
                            setOnClickListener { }
                        }
                    }
                }
            }
        }

        if (position == centerActiveIndex) {
            if (pickerDecorator.fontBold.first) {
                holder.mTextView.typeface = fontTypeLoader(holder.mTextView.context, true)
            } else {
                holder.mTextView.typeface = fontTypeLoader(holder.mTextView.context, false)
            }
            holder.mTextView.setTextColor(pickerDecorator.fontColor.first)
            holder.mTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, pickerDecorator.fontSize.first)
        }

        var addIndex = 0
        if (!infiniteMode && !isPlaceholder) addIndex = ceil(itemToShow.toDouble() / 2).toInt()

        if (disabledItems.size != 0 && disabledItems.indexOf((position - addIndex) % mDataset.size) != -1) {
            holder.mTextView.apply {
                setTextColor(pickerDecorator.disabledColor)
                setOnClickListener { }
            }
        }
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position % mDataset.size)
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position % mDataset.size)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

        mRecyclerView = recyclerView
    }

    companion object {
        const val ALIGN_LEFT = 0
        const val ALIGN_CENTER = 1
        const val ALIGN_RIGHT = 2
    }

    class PickerViewHolder(val mTextView: TextView) : RecyclerView.ViewHolder(mTextView)
}