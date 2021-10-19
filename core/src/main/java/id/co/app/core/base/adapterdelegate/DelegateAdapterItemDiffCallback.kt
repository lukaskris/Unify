package id.co.app.core.base.adapterdelegate

import androidx.recyclerview.widget.DiffUtil


/**
 * Created by Lukas Kristianto on 4/28/2021.
 * App Sinarmas
 * lukas_kristianto@app.co.id
 */

internal class DelegateAdapterItemDiffCallback: DiffUtil.ItemCallback<DelegateAdapterItem>() {

	override fun areItemsTheSame(oldItem: DelegateAdapterItem, newItem: DelegateAdapterItem): Boolean =
		oldItem::class == newItem::class && oldItem.id() == newItem.id()

	override fun areContentsTheSame(oldItem: DelegateAdapterItem, newItem: DelegateAdapterItem): Boolean =
		oldItem.content() == newItem.content()

	override fun getChangePayload(oldItem: DelegateAdapterItem, newItem: DelegateAdapterItem): Any =
		oldItem.payload(newItem)
}