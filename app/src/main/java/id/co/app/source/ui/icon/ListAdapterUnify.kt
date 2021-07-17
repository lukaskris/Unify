package id.co.app.source.ui.icon

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import id.co.app.components.checkbox.CheckboxUnify
import id.co.app.components.image.ImageUnify
import id.co.app.components.quantity.QuantityEditorUnify
import id.co.app.components.radio.RadioButtonUnify
import id.co.app.components.switchunify.SwitchUnify
import id.co.app.components.typography.Typography
import id.co.app.components.utils.setBodyText
import id.co.app.components.utils.setImage
import id.co.app.source.R

class ListAdapterUnify(context: Context, layoutReference: Int, items: ArrayList<ListItemUnify>) :
    ArrayAdapter<ListItemUnify>(context, layoutReference, items) {
    private var layoutReference = 0
    private var itemReference: ArrayList<ListItemUnify>

    private val GONE = View.GONE
    private val VISIBLE = View.VISIBLE

    private var isContainCustom = false

    private var TYPE_NORMAL = 0
    private var TYPE_CUSTOM = 1

    /**
     * Reference Variable
     */
    private lateinit var iconReference: ImageView
    private var listTitle: Typography? = null
    private var listDescription: Typography? = null

    private lateinit var listRightCheckbox: CheckboxUnify
    private lateinit var listLeftCheckbox: CheckboxUnify

    private lateinit var listLeftRadiobtn: RadioButtonUnify
    private lateinit var listRightRadiobtn: RadioButtonUnify

    private lateinit var listSwitch: SwitchUnify
    private lateinit var listChevron: ImageUnify
    private lateinit var listQuantityEditor: QuantityEditorUnify
    private lateinit var listAction: TextView

    private lateinit var listItemWrapper: LinearLayout

    init {
        this.layoutReference = layoutReference
        this.itemReference = items

        for (item in itemReference) {
            if (item.listCustomRow != null) {
                isContainCustom = true
                break
            }
        }
    }


    override fun getItemViewType(position: Int): Int {
        return if (itemReference[position].listCustomRow == null) TYPE_NORMAL else TYPE_CUSTOM
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val data = itemReference[position]

        val baseView: View = convertView ?: View.inflate(context, layoutReference, null)

        if (data.listCustomRow != null) {
            val contentView = View.inflate(context, data.listCustomRow!!, null)
            val contentWrapper = baseView.findViewById<LinearLayout>(R.id.list_content_wrapper)
            val resultView = data.listCustomAdapter(contentView, position)

            contentWrapper.removeAllViews()
            contentWrapper.addView(resultView)
        } else {
            val contentView = View.inflate(context, R.layout.unify_list_content_layout, null)
            val contentWrapper = baseView.findViewById<LinearLayout>(R.id.list_content_wrapper)

            contentWrapper.removeAllViews()
            contentWrapper.addView(contentView)
        }

        initReference(baseView)
        setFont(data)
        setLayout(data)

        return baseView
    }

    private fun setFont(data: ListItemUnify) {
        /**
         * Set font only if using default template
         */
        listTitle?.setType(Typography.BODY_2)
        listTitle?.setWeight(if (data.isBold) Typography.BOLD else Typography.REGULAR)
        listDescription?.setType(Typography.BODY_3)
        listAction.setBodyText(3, true)
        listAction.setTextColor(ContextCompat.getColor(context, R.color.Unify_B500))
    }

    private fun initReference(baseView: View) {
        iconReference = baseView.findViewById(R.id.list_icon)
        listTitle = baseView.findViewById(R.id.list_title) ?: null
        listDescription = baseView.findViewById(R.id.list_description) ?: null

        listRightCheckbox = baseView.findViewById(R.id.listitem_right_checkbox)
        listLeftCheckbox = baseView.findViewById(R.id.listitem_left_checkbox)

        listLeftRadiobtn = baseView.findViewById(R.id.listitem_left_radio)
        listRightRadiobtn = baseView.findViewById(R.id.listitem_right_radio)

        listSwitch = baseView.findViewById(R.id.listitem_right_switch)
        listChevron = baseView.findViewById(R.id.listitem_right_chevron)
        listQuantityEditor = baseView.findViewById(R.id.listitem_right_qty)
        listAction = baseView.findViewById(R.id.list_action)

        listItemWrapper = baseView.findViewById(R.id.list_item_wrapper)
    }

    private fun setLayout(data: ListItemUnify) {
        /**
         * Set layout
         */
        val itemVariant = data.getVariant()

        listTitle?.text = data.listTitleText

        listDescription?.visibility = GONE
        listRightRadiobtn.visibility = GONE
        listRightCheckbox.visibility = GONE
        listSwitch.visibility = GONE
        listQuantityEditor.visibility = GONE
        listChevron.visibility = GONE
        listAction.visibility = GONE
        listLeftCheckbox.visibility = GONE
        listLeftRadiobtn.visibility = GONE
        iconReference.visibility = GONE

        when (itemVariant[0]) {
            ListItemUnify.CHECKBOX -> listLeftCheckbox.visibility = VISIBLE
            ListItemUnify.RADIO_BUTTON -> listLeftRadiobtn.visibility = VISIBLE
        }

        when (itemVariant[1]) {
            ListItemUnify.RADIO_BUTTON -> listRightRadiobtn.visibility = VISIBLE
            ListItemUnify.CHECKBOX -> listRightCheckbox.visibility = VISIBLE
            ListItemUnify.SWITCH -> listSwitch.visibility = VISIBLE
            ListItemUnify.QUANTITY_EDITOR -> listQuantityEditor.visibility = VISIBLE
            ListItemUnify.CHEVRON -> listChevron.visibility = VISIBLE
            ListItemUnify.ACTION -> {
                listAction.visibility = VISIBLE
                listAction.text = data.listActionText
            }
        }

        if (data.listDrawable != null) {
            iconReference.visibility = VISIBLE
            iconReference.setImageDrawable(data.listDrawable)
        }

        if (data.listDrawableReference != null) {
            iconReference.visibility = VISIBLE
            iconReference.setImage(data.listDrawableReference!!, 6f)
        }

        if (data.listDrawableUrl != null) {
            iconReference.visibility = VISIBLE
            iconReference.setImage(data.listDrawableUrl!!, 6f)
        }

        if (!data.listDescriptionText.isNullOrEmpty()) {
            listDescription?.text = data.listDescriptionText
            listDescription?.visibility = View.VISIBLE
        }

        if (!data.isBorder) {
            listItemWrapper.background = null
        }

        val iconLayoutParam = iconReference.layoutParams
        iconLayoutParam.width = data.listIconWidth
        iconLayoutParam.height = data.listIconHeight
    }
}