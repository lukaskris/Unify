package id.co.app.source.ui.icon

import android.content.Context
import android.util.AttributeSet
import android.view.ViewTreeObserver
import android.widget.ListView
import id.co.app.source.R


class ListUnify(context: Context, attributeSet: AttributeSet) : ListView(context, attributeSet) {
    private var array = ArrayList<ListItemUnify>(0)
    private var onLoadFinish = {}

    public override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val expandSpec = MeasureSpec.makeMeasureSpec(
            Integer.MAX_VALUE shr 2,
            MeasureSpec.AT_MOST
        )
        super.onMeasure(widthMeasureSpec, expandSpec)
    }

    private fun applyAdapter() {
        val adapter = ListAdapterUnify(context, R.layout.unify_list_layout, array)

        setPadding(16, 0, 0, 0)
        dividerHeight = 0

        setAdapter(adapter)
        viewTreeObserver.addOnPreDrawListener(object: ViewTreeObserver.OnPreDrawListener{
            override fun onPreDraw(): Boolean {
                setReference()
                onLoadFinish()

                viewTreeObserver.removeOnPreDrawListener(this)

                return false
            }
        })

    }

    fun setData(dataList: ArrayList<ListItemUnify>) {
        array = dataList
        applyAdapter()
    }

    private fun setReference() {
        array.forEachIndexed { index, listItemUnify ->
            val child = getChildAt(index)

            listItemUnify.listIcon = child?.findViewById(R.id.list_icon)

            listItemUnify.listTitle = child?.findViewById(R.id.list_title)
            listItemUnify.listDescription = child?.findViewById(R.id.list_description)

            listItemUnify.listRightCheckbox = child?.findViewById(R.id.listitem_right_checkbox)
            listItemUnify.listLeftCheckbox = child?.findViewById(R.id.listitem_left_checkbox)

            listItemUnify.listLeftRadiobtn = child?.findViewById(R.id.listitem_left_radio)
            listItemUnify.listRightRadiobtn = child?.findViewById(R.id.listitem_right_radio)

            listItemUnify.listSwitch = child?.findViewById(R.id.listitem_right_switch)
            listItemUnify.listChevron = child?.findViewById(R.id.listitem_right_chevron)
            listItemUnify.listQuantityEditor = child?.findViewById(R.id.listitem_right_qty)
            listItemUnify.listAction = child?.findViewById(R.id.list_action)

            /**
             * Clear the gap between checkbox / radiobutton to the text
             * Clear after the component rendered
             */
            listItemUnify.listRightRadiobtn?.setPadding(0,0,0,0)
            listItemUnify.listRightCheckbox?.setPadding(0,0,0,0)
        }
    }

    fun onLoadFinish(action: () -> Unit){
        onLoadFinish = action
    }
}