package id.co.app.core.customview.listbottomsheet

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import id.co.app.core.R
import id.co.app.core.base.adapterdelegate.DelegateAdapter
import id.co.app.core.base.adapterdelegate.DelegateAdapterItem
import id.co.app.core.base.adapterdelegate.DelegateAdapterItemClick
import id.co.app.core.databinding.ItemListItemBinding


/**
 * Created by Lukas Kristianto on 4/29/2021.
 * App Sinarmas
 * lukas_kristianto@app.co.id
 */
class ListItemAdapter(
	private val listener: DelegateAdapterItemClick,
) : DelegateAdapter<ListModel, ListItemAdapter.OutstandingItemViewHolder>(
	ListModel::class.java
) {
	override fun createViewHolder(parent: ViewGroup): OutstandingItemViewHolder {
		return OutstandingItemViewHolder(
			DataBindingUtil.inflate(
				LayoutInflater.from(parent.context), R.layout.item_list_item, parent, false
			)
		)
	}

	override fun bindViewHolder(
		model: ListModel,
		viewHolder: OutstandingItemViewHolder,
		payloads: List<DelegateAdapterItem.Payloadable>,
	) {
		viewHolder.bind(model)
	}

	inner class OutstandingItemViewHolder(
		private val binding: ItemListItemBinding,
	) : RecyclerView.ViewHolder(binding.root) {
		fun bind(model: ListModel) {
			model.apply {
				binding.root.setOnClickListener {
					listener.onClick(this)
				}
				binding.titleValue = model.name
			}
		}
	}
}