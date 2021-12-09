package id.co.app.core.base.adapterdelegate.adapter.model

import id.co.app.core.base.adapterdelegate.DelegateAdapterItem


/**
 * Created by Lukas Kristianto on 6/1/2021.
 * App Sinarmas
 * lukas_kristianto@app.co.id
 */
class EmptyItemModel(
    val title: String = "Tidak ada data tersedia",
    val subtitle: String = ""
) : DelegateAdapterItem {
    override fun id(): Any = EMPTY_ITEM_MODEL

    override fun content(): Any = title + subtitle

    companion object {
        private const val EMPTY_ITEM_MODEL = "EmptyItemModel"
    }
}