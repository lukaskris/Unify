package id.co.app.source.core.base.base.adapterdelegate

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


/**
 * Created by Lukas Kristianto on 4/28/2021.
 * App Sinarmas
 * lukas_kristianto@app.co.id
 */
abstract class DelegateAdapter<M, in VH : RecyclerView.ViewHolder>(val modelClass: Class<out M>) {

	abstract fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder
	abstract fun bindViewHolder(model: M, viewHolder: VH, payloads: List<DelegateAdapterItem.Payloadable>)

	open fun onViewRecycled(viewHolder: VH) = Unit
	open fun onViewDetachedFromWindow(viewHolder: VH) = Unit
	open fun onViewAttachedToWindow(viewHolder: VH) = Unit
}