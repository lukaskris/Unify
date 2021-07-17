package id.co.app.source.ui.icon

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import id.co.app.components.quantity.QuantityEditorUnify
import id.co.app.components.radio.RadioButtonUnify
import id.co.app.components.switchunify.SwitchUnify
import id.co.app.components.utils.toPx

class ListItemUnify {
    /**
     * state variant
     */
    private var isLeftCheckbox = false
    private var isLeftRadio = false
    private var isRightCheckbox = false
    private var isRightRadio = false
    private var isRightChevron = false
    private var isRightQty = false
    private var isRightSwitch = false
    var isBold = true
    var isBorder = true

    var listTitleText: String = ""
    var listDescriptionText: String = ""
    var listActionText: String? = null

    var listDrawable: Drawable? = null
    var listDrawableReference: Int? = null
    var listDrawableUrl: String? = null

    var listIconWidth: Int = 48.toPx()
    var listIconHeight: Int = 48.toPx()

    var listCustomRow: Int? = null
    var listCustomAdapter: (view: View, index: Int) -> View? = {_,_ -> null}

    var listIcon: ImageView? = null

    var listRightCheckbox: CheckBox? = null
    var listLeftCheckbox: CheckBox? = null

    var listLeftRadiobtn: RadioButtonUnify? = null
    var listRightRadiobtn: RadioButtonUnify? = null

    var listSwitch: SwitchUnify? = null
    var listChevron: ImageView? = null
    var listQuantityEditor: QuantityEditorUnify? = null
    var listAction: TextView? = null
    var listTitle: TextView? = null
    var listDescription: TextView? = null

    constructor(title: String, description: String){
        listTitleText = title
        listDescriptionText = description
    }

    constructor(customLayout: Int? = null, adapterBind: (view: View, index: Int) -> View? = {_,_ -> null}){
        listCustomRow = customLayout
        listCustomAdapter = adapterBind
    }

    fun setVariant(leftComponent: Int? = null, rightComponent: Int? = null, actionText: String? = null){
        /**
         * Reset Variant
         */
        isLeftCheckbox = false
        isLeftRadio = false
        isRightCheckbox = false
        isRightRadio = false
        isRightSwitch = false
        isRightChevron = false
        isRightQty = false
        listActionText = ""

        when(leftComponent){
            CHECKBOX -> isLeftCheckbox = true
            RADIO_BUTTON -> isLeftRadio = true
        }

        when(rightComponent){
            CHECKBOX -> isRightCheckbox = true
            RADIO_BUTTON -> isRightRadio = true
            SWITCH -> isRightSwitch = true
            CHEVRON -> isRightChevron = true
            QUANTITY_EDITOR -> isRightQty = true
            ACTION -> listActionText = actionText
        }
    }

    fun getVariant():ArrayList<Int>{
        /**
         * Result always return 2 value
         * index 0 for left component
         * index 1 for right component
         */
        val result = ArrayList<Int>(0)

        when {
            isLeftCheckbox -> {
                result.add(CHECKBOX)
            }
            isLeftRadio -> {
                result.add(RADIO_BUTTON)
            }
            else -> {
                result.add(-1)
            }
        }

        if(isRightCheckbox){
            result.add(CHECKBOX)
        }else if(isRightChevron){
            result.add(CHEVRON)
        }else if(isRightQty){
            result.add(QUANTITY_EDITOR)
        }else if(isRightRadio){
            result.add(RADIO_BUTTON)
        }else if(isRightSwitch){
            result.add(SWITCH)
        }else if(!listActionText.isNullOrEmpty()){
            result.add(ACTION)
        }else {
            result.add(-1)
        }

        return result
    }

    companion object {
        /**
         * Const variable for left & right container
         */
        const val CHECKBOX = 0
        const val RADIO_BUTTON = 1

        /**
         * Const variable for right container
         */
        const val SWITCH = 2
        const val CHEVRON = 3
        const val QUANTITY_EDITOR = 4
        const val ACTION = 5
    }
}