package id.co.app.core.customview.listbottomsheet

import id.co.app.core.base.adapterdelegate.DelegateAdapterItem

data class ListModel(
    val id: String,
    val name: String,
    val data: Any
) : DelegateAdapterItem {
    override fun id(): Any {
        return id
    }

    override fun content(): Any {
        return name + data
    }
}